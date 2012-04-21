#include "etherShield.h"

// please modify the following lines. mac and ip have to be unique
// in your local area network. You can not have the same numbers in
// two devices:
static uint8_t mymac[6] = { 0x54, 0x55, 0x58, 0x10, 0x00, 0x24 };
static uint8_t myip[4] = { 192, 168, 1, 88 };
static uint16_t my_port = 1200; // client port

// client_ip - modify it when you have multiple client on the network
// for server to distinguish each ethershield client
static char client_ip[] = "192.168.1.88";

// server settings - modify the service ip to your own server
static uint8_t dest_ip[4] = { 192, 168, 1, 4 };
static uint8_t dest_mac[6];

enum CLIENT_STATE {
    IDLE, ARP_SENT, ARP_REPLY, SYNC_SENT
};

static CLIENT_STATE client_state;

static uint8_t client_data_ready;

static uint8_t syn_ack_timeout = 0;

#define BUFFER_SIZE 500
static uint8_t buf[BUFFER_SIZE + 1];

char sensorData[10];

// prepare the webpage by writing the data to the tcp send buffer
uint16_t print_webpage(uint8_t *buf);
int8_t analyse_cmd(char *str);
// get current temperature
#define TEMP_PIN  3
void getCurrentTemp(char *temperature);
void client_process(void);

void setup() {

    // intialize varible;
    syn_ack_timeout = 0;
    client_data_ready = 0;
    client_state = IDLE;
    // initialize DS18B20 datapin
    digitalWrite(TEMP_PIN, LOW);
    pinMode(TEMP_PIN, INPUT); // sets the digital pin as input (logic 1)

}

void loop() {

    if (client_data_ready == 0) {
        delay(60000UL); // delay 60s
        getCurrentTemp(sensorData);
        client_data_ready = 1;
    }
    client_process();
}

void client_process(void) {
    uint16_t plen;
    uint8_t i;

    if (client_data_ready == 0)
        return; // nothing to send

    if (client_state == IDLE) { // initialize ARP
        es.ES_make_arp_request(buf, dest_ip);

        client_state = ARP_SENT;
        return;
    }

    if (client_state == ARP_SENT) {

        plen = es.ES_enc28j60PacketReceive(BUFFER_SIZE, buf);

        // destination ip address was found on network
        if (plen != 0) {
            if (es.ES_arp_packet_is_myreply_arp(buf)) {
                client_state = ARP_REPLY;
                syn_ack_timeout = 0;
                return;
            }

        }
        delay(10);
        syn_ack_timeout++;

        if (syn_ack_timeout == 100) { //timeout, server ip not found
            client_state = IDLE;
            client_data_ready = 0;
            syn_ack_timeout = 0;
            return;
        }
    }

    // send SYN packet to initial connection
    if (client_state == ARP_REPLY) {
        // save dest mac
        for (i = 0; i < 6; i++) {
            dest_mac[i] = buf[ETH_SRC_MAC + i];
        }

        es.ES_tcp_client_send_packet(buf, 80, 1200, TCP_FLAG_SYN_V, // flag
                1, // (bool)maximum segment size
                1, // (bool)clear sequence ack number
                0, // 0=use old seq, seqack : 1=new seq,seqack no data : new seq,seqack with data
                0, // tcp data length
                dest_mac, dest_ip);

        client_state = SYNC_SENT;
    }
    // get new packet
    if (client_state == SYNC_SENT) {
        plen = es.ES_enc28j60PacketReceive(BUFFER_SIZE, buf);

        // no new packet incoming
        if (plen == 0) {
            return;
        }

        // check ip packet send to avr or not?
        // accept ip packet only
        if (es.ES_eth_type_is_ip_and_my_ip(buf, plen) == 0) {
            return;
        }

        // check SYNACK flag, after AVR send SYN server response by send SYNACK to AVR
        if (buf[TCP_FLAGS_P] == (TCP_FLAG_SYN_V | TCP_FLAG_ACK_V)) {

            // send ACK to answer SYNACK

            es.ES_tcp_client_send_packet(buf, 80, 1200, TCP_FLAG_ACK_V, // flag
                    0, // (bool)maximum segment size
                    0, // (bool)clear sequence ack number
                    1, // 0=use old seq, seqack : 1=new seq,seqack no data : new seq,seqack with data
                    0, // tcp data length
                    dest_mac, dest_ip);
            // setup http request to server
            plen = gen_client_request(buf);
            // send http request packet
            // send packet with PSHACK
            es.ES_tcp_client_send_packet(buf, 80, // destination port
                    1200, // source port
                    TCP_FLAG_ACK_V | TCP_FLAG_PUSH_V, // flag
                    0, // (bool)maximum segment size
                    0, // (bool)clear sequence ack number
                    0, // 0=use old seq, seqack : 1=new seq,seqack no data : >1 new seq,seqack with data
                    plen, // tcp data length
                    dest_mac, dest_ip);
            return;
        }
        // after AVR send http request to server, server response by send data with PSHACK to AVR
        // AVR answer by send ACK and FINACK to server
        if (buf[TCP_FLAGS_P] == (TCP_FLAG_ACK_V | TCP_FLAG_PUSH_V)) {
            plen = es.ES_tcp_get_dlength((uint8_t*) &buf);

            // send ACK to answer PSHACK from server
            es.ES_tcp_client_send_packet(buf, 80, // destination port
                    1200, // source port
                    TCP_FLAG_ACK_V, // flag
                    0, // (bool)maximum segment size
                    0, // (bool)clear sequence ack number
                    plen, // 0=use old seq, seqack : 1=new seq,seqack no data : >1 new seq,seqack with data
                    0, // tcp data length
                    dest_mac, dest_ip);
            ;
            // send finack to disconnect from web server

            es.ES_tcp_client_send_packet(buf, 80, // destination port
                    1200, // source port
                    TCP_FLAG_FIN_V | TCP_FLAG_ACK_V, // flag
                    0, // (bool)maximum segment size
                    0, // (bool)clear sequence ack number
                    0, // 0=use old seq, seqack : 1=new seq,seqack no data : >1 new seq,seqack with data
                    0, dest_mac, dest_ip);

            return;

        }
        // answer FINACK from web server by send ACK to web server
        if (buf[TCP_FLAGS_P] == (TCP_FLAG_ACK_V | TCP_FLAG_FIN_V)) {
            // send ACK with seqack = 1
            es.ES_tcp_client_send_packet(

            buf, 80, // destination port
                    1200, // source port
                    TCP_FLAG_ACK_V, // flag
                    0, // (bool)maximum segment size
                    0, // (bool)clear sequence ack number
                    1, // 0=use old seq, seqack : 1=new seq,seqack no data : >1 new seq,seqack with data
                    0, dest_mac, dest_ip);
            client_state = IDLE; // return to IDLE state
            client_data_ready = 0; // client data sent
        }
    }
}

