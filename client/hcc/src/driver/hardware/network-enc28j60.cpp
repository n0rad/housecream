#include "../network.h"
#include "../../lib/ethernetShield-enc28j60/etherShield.h"

#define BUFFER_SIZE 1000
uint8_t buf[BUFFER_SIZE + 1];

EtherShield es = EtherShield();

uint16_t port;

void (*resetFunc)(void) = 0; //declare reset function @ address 0

void networkSetup() {

    uint8_t mac[6];
    configBoardGetMac(mac);

    /*initialize enc28j60*/
    es.ES_enc28j60Init((uint8_t *) mac);
    es.ES_enc28j60clkout(2); // change clkout from 6.25MHz to 12.5MHz
    delay(10);
    /* Magjack leds configuration, see enc28j60 datasheet, page 11 */
    // LEDA=greed LEDB=yellow
    //
    // 0x880 is PHLCON LEDB=on, LEDA=on
    // enc28j60PhyWrite(PHLCON,0b0000 1000 1000 00 00);
    es.ES_enc28j60PhyWrite(PHLCON, 0x880);
    delay(500);
    // 0x990 is PHLCON LEDB=off, LEDA=off
    // enc28j60PhyWrite(PHLCON,0b0000 1001 1001 00 00);
    es.ES_enc28j60PhyWrite(PHLCON, 0x990);
    delay(500);
    // 0x880 is PHLCON LEDB=on, LEDA=on
    // enc28j60PhyWrite(PHLCON,0b0000 1000 1000 00 00);
    es.ES_enc28j60PhyWrite(PHLCON, 0x880);
    delay(500);
    // 0x990 is PHLCON LEDB=off, LEDA=off
    // enc28j60PhyWrite(PHLCON,0b0000 1001 1001 00 00);
    es.ES_enc28j60PhyWrite(PHLCON, 0x990);
    delay(500);
    // 0x476 is PHLCON LEDA=links status, LEDB=receive/transmit
    // enc28j60PhyWrite(PHLCON,0b0000 0100 0111 01 10);
    es.ES_enc28j60PhyWrite(PHLCON, 0x476);
    delay(100);

    uint8_t ip[4];
    settingsBoardGetIP(ip);
    port = settingsBoardGetPort();
    //init the ethernet/ip layer:
    es.ES_init_ip_arp_udp_tcp((uint8_t *) mac, (uint8_t *) ip, port);
}

static uint16_t srcPort = 1200;
static uint8_t syn_ack_timeout = 0;
static uint8_t dest_mac[6];
enum CLIENT_STATE {
    IDLE, ARP_SENT, ARP_REPLY, SYNC_SENT
};
static CLIENT_STATE clientState;

extern boolean needReboot;

void networkManage() {
    uint16_t plen, dat_p;
    uint8_t i;

    if (notification != 0 && clientState == IDLE) { // initialize ARP
        DEBUG_P(PSTR("idle"));
        es.ES_make_arp_request(buf, NotifyDstIp);
        clientState = ARP_SENT;
        return;
    }
    if (clientState == ARP_SENT) {
        // DEBUG_p(PSTR("arp"));
        plen = es.ES_enc28j60PacketReceive(BUFFER_SIZE, buf);
        // destination ip address was found on network
        if (plen != 0) {
            if (es.ES_arp_packet_is_myreply_arp(buf)) {
                clientState = ARP_REPLY;
                syn_ack_timeout = 0;
                return;
            }
        }
        delay(10);
        syn_ack_timeout++;
        if (syn_ack_timeout == 100) { //timeout, server ip not found
            DEBUG_P(PSTR("arpnotfound"));
            clientState = IDLE;

            clientBuildNextQuery((char *) buf); // build next request to remove event
            syn_ack_timeout = 0;
            return;
        }
    }
    // send SYN packet to initial connection
    if (clientState == ARP_REPLY) {
        DEBUG_P(PSTR("arprep"));
        // save dest mac
        for (i = 0; i < 6; i++) {
            dest_mac[i] = buf[ETH_SRC_MAC + i];
        }
        es.ES_tcp_client_send_packet(buf, notifyDstPort, srcPort, TCP_FLAG_SYN_V, // flag
                1, // (bool)maximum segment size
                1, // (bool)clear sequence ack number
                0, // 0=use old seq, seqack : 1=new seq,seqack no data : new seq,seqack with data
                0, // tcp data length
                dest_mac, NotifyDstIp);
        clientState = SYNC_SENT;
    }

    if (clientState == ARP_SENT || clientState == ARP_REPLY) {
        return;
    }

    plen = es.ES_enc28j60PacketReceive(BUFFER_SIZE, buf);
    /*plen will not equal zero if there is a valid packet (without crc error) */
    if (plen == 0) {
        return;
    }

    // arp is broadcast if unknown but a host may also verify the mac address by sending it to a unicast address.
    if (es.ES_eth_type_is_arp_and_my_ip(buf, plen)) {
        es.ES_make_arp_answer_from_request(buf);
        return;
    }
    // check if packet is for me
    if (es.ES_eth_type_is_ip_and_my_ip(buf, plen) == 0) {
        return;
    }
    if (buf[IP_PROTO_P] == IP_PROTO_ICMP_V
            && buf[ICMP_TYPE_P] == ICMP_TYPE_ECHOREQUEST_V) {
        es.ES_make_echo_reply_from_request(buf, plen);
        return;
    }
    // tcp port www start, compare only the lower byte
    if (buf[IP_PROTO_P] == IP_PROTO_TCP_V && buf[TCP_DST_PORT_H_P] == 0
            && buf[TCP_DST_PORT_L_P] == port) {
        if (buf[TCP_FLAGS_P] & TCP_FLAGS_SYN_V) {
            es.ES_make_tcp_synack_from_syn(buf); // make_tcp_synack_from_syn does already send the syn,ack
            return;
        }
        if (buf[TCP_FLAGS_P] & TCP_FLAGS_ACK_V) {
            es.ES_init_len_info(buf); // init some data structures
            dat_p = es.ES_get_tcp_data_pointer();
            if (dat_p == 0) { // we can possibly have no data, just ack:
                if (buf[TCP_FLAGS_P] & TCP_FLAGS_FIN_V) {
                    es.ES_make_tcp_ack_from_any(buf);
                }
                return;
            }
            plen = handleWebRequest((char *) buf, dat_p, plen);
            es.ES_make_tcp_ack_from_any(buf); // send ack for http get
            if (plen != 0) { // we need more data send no response
                es.ES_make_tcp_ack_with_data(buf, plen); // send data
            }
            if (needReboot) {
                resetFunc();
            }
        }
    } else {
        // check SYNACK flag, after AVR send SYN server response by send SYNACK to AVR
        if (buf[TCP_FLAGS_P] == (TCP_FLAG_SYN_V | TCP_FLAG_ACK_V)) {
            DEBUG_P(PSTR("synack"));
            // send ACK to answer SYNACK
            es.ES_tcp_client_send_packet(buf, notifyDstPort, srcPort, TCP_FLAG_ACK_V, // flag
                    0, // (bool)maximum segment size
                    0, // (bool)clear sequence ack number
                    1, // 0=use old seq, seqack : 1=new seq,seqack no data : new seq,seqack with data
                    0, // tcp data length
                    dest_mac, NotifyDstIp);
            // setup http request to server
            plen = clientBuildNextQuery((char *) buf);
            // send http request packet
            // send packet with PSHACK
            es.ES_tcp_client_send_packet(buf, notifyDstPort, // destination port
                    srcPort, // source port
                    TCP_FLAG_ACK_V | TCP_FLAG_PUSH_V, // flag
                    0, // (bool)maximum segment size
                    0, // (bool)clear sequence ack number
                    0, // 0=use old seq, seqack : 1=new seq,seqack no data : >1 new seq,seqack with data
                    plen, // tcp data length
                    dest_mac, NotifyDstIp);
            return;
        }
        // after AVR send http request to server, server response by send data with PSHACK to AVR
        // AVR answer by send ACK and FINACK to server
        if (buf[TCP_FLAGS_P] == (TCP_FLAG_ACK_V | TCP_FLAG_PUSH_V)) {
            DEBUG_P(PSTR("pshack"));
            plen = es.ES_tcp_get_dlength((uint8_t*) &buf);
            // send ACK to answer PSHACK from server
            es.ES_tcp_client_send_packet(buf, notifyDstPort, // destination port
                    srcPort, // source port
                    TCP_FLAG_ACK_V, // flag
                    0, // (bool)maximum segment size
                    0, // (bool)clear sequence ack number
                    plen, // 0=use old seq, seqack : 1=new seq,seqack no data : >1 new seq,seqack with data
                    0, // tcp data length
                    dest_mac, NotifyDstIp);
            // send finack to disconnect from web server
            es.ES_tcp_client_send_packet(buf, notifyDstPort, // destination port
                    srcPort, // source port
                    TCP_FLAG_FIN_V | TCP_FLAG_ACK_V, // flag
                    0, // (bool)maximum segment size
                    0, // (bool)clear sequence ack number
                    0, // 0=use old seq, seqack : 1=new seq,seqack no data : >1 new seq,seqack with data
                    0, dest_mac, NotifyDstIp);
            return;
        }
        // answer FINACK from web server by send ACK to web server
        if (buf[TCP_FLAGS_P] == (TCP_FLAG_ACK_V | TCP_FLAG_FIN_V)) {
            DEBUG_P(PSTR("finack"));
            // send ACK with seqack = 1
            es.ES_tcp_client_send_packet(buf, notifyDstPort, // destination port
                    srcPort, // source port
                    TCP_FLAG_ACK_V, // flag
                    0, // (bool)maximum segment size
                    0, // (bool)clear sequence ack number
                    1, // 0=use old seq, seqack : 1=new seq,seqack no data : >1 new seq,seqack with data
                    0, dest_mac, NotifyDstIp);
            clientState = IDLE; // return to IDLE state
            return;
        }
        DEBUG_PRINTLN("OTHER CLIENT");
    }
}
