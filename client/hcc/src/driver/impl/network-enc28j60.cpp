#include "../../hcc.h"
#include "../../../lib/ethernetShield-enc28j60/etherShield.h"

#define BUFFER_SIZE 1000
uint8_t buf[BUFFER_SIZE + 1];

EtherShield es = EtherShield();

uint16_t port;

void(* resetFunc) (void) = 0; //declare reset function @ address 0


void networkSetup() {

    uint8_t mac[6];
    getConfigMac(mac);

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
    getConfigIP(ip);
    port = getConfigPort();
    DEBUG_PRINTLN(port);
    //init the ethernet/ip layer:
    es.ES_init_ip_arp_udp_tcp((uint8_t *) mac, (uint8_t *)ip, port);
}

void networkManage() {
    uint16_t plen, dat_p;

    plen = es.ES_enc28j60PacketReceive(BUFFER_SIZE, buf);

    /*plen will not equal zero if there is a valid packet (without crc error) */
    if (plen != 0) {

      // arp is broadcast if unknown but a host may also verify the mac address by sending it to a unicast address.
      if (es.ES_eth_type_is_arp_and_my_ip(buf, plen)) {
        es.ES_make_arp_answer_from_request(buf);
        return;
      }

      // check if packet is for us
      if (es.ES_eth_type_is_ip_and_my_ip(buf,plen) == 0) {
        return;
      }

      if (buf[IP_PROTO_P] == IP_PROTO_ICMP_V && buf[ICMP_TYPE_P] == ICMP_TYPE_ECHOREQUEST_V) {
        es.ES_make_echo_reply_from_request(buf,plen);
        return;
      }

      DEBUG_PRINT("receive ");
      DEBUG_PRINTLN(port);
      // tcp port www start, compare only the lower byte
      if (buf[IP_PROTO_P] == IP_PROTO_TCP_V && buf[TCP_DST_PORT_H_P] == 0 && buf[TCP_DST_PORT_L_P] == port) {
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

          plen = handleWebRequest((char *)buf, dat_p, plen);

          es.ES_make_tcp_ack_from_any(buf); // send ack for http get
          es.ES_make_tcp_ack_with_data(buf, plen); // send data
          extern boolean needReboot;
          if (needReboot) {
              resetFunc();
          }
        }
      }
    }
}
