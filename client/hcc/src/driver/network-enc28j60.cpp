#include "../hcc.h"
#include "../../lib/ethernetShield-enc28j60/etherShield.h"

#define BUFFER_SIZE 500
static uint8_t buf[BUFFER_SIZE + 1];

EtherShield es = EtherShield();

char globalErrorBuffer[100] = {0};
extern t_boardDescription p_boardDescription;








/**
 * Add a char * to buf buffer.
 * @return new position in buf
 */
uint16_t addToBuffer(uint8_t *buf, unsigned long pos, char *value, unsigned int len) {
  memcpy(&buf[TCP_CHECKSUM_L_P + 3 + pos], value, len);
  return pos + len;
}

/**
 * Add a char * to buf buffer.
 * @return new position in buf
 */
uint16_t addToBuffer(uint8_t *buf, unsigned long pos, char *value) {
  size_t size = strlen(value);
  memcpy(&buf[TCP_CHECKSUM_L_P + 3 + pos], value, size);
  return pos + size;
}

/**
 * Add a long char* representation to buf buffer.
 * @return new position in buf
 */
uint16_t addToBuffer(uint8_t *buf, unsigned long pos, uint16_t value) {
   static char value_to_add[16] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
   int j = 0;

   sprintf(value_to_add, "%d", value);
   while (value_to_add[j]) {
     buf[TCP_CHECKSUM_L_P + 3 + pos] = value_to_add[j++];
     pos++;
   }
   return pos;
}

/**
 * Add a long char* representation to buf buffer.
 * @return new position in buf
 */
uint16_t addToBufferHex(uint8_t *buf, unsigned long pos, uint16_t value) {
   static char value_to_add[3] = {0,0,0};
   int j = 0;

   sprintf(value_to_add, "%02X", value);
   while (value_to_add[j]) {
     buf[TCP_CHECKSUM_L_P + 3 + pos] = value_to_add[j++];
     pos++;
   }
   return pos;
}
























void networkSetup() {
    /*initialize enc28j60*/
    es.ES_enc28j60Init(p_boardDescription.mac);
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
    //init the ethernet/ip layer:
    es.ES_init_ip_arp_udp_tcp(p_boardDescription.mac, config.boardInfo.ip, config.boardInfo.port);
}

void networkManage() {
    uint16_t plen, dat_p;

    plen = es.ES_enc28j60PacketReceive(BUFFER_SIZE, buf);

    /*plen will not equal zero if there is a valid packet (without crc error) */
    if (plen != 0) {

      // arp is broadcast if unknown but a host may also verify the mac address by sending it to a unicast address.
      if (es.ES_eth_type_is_arp_and_my_ip(buf, plen)) {
        DEBUG_PRINTLN("Send response to ARP request");
        es.ES_make_arp_answer_from_request(buf);
        return;
      }

      // check if packet is for us
      if (es.ES_eth_type_is_ip_and_my_ip(buf,plen) == 0) {
        DEBUG_PRINTLN("Packet is not for me");
        return;
      }

      if (buf[IP_PROTO_P] == IP_PROTO_ICMP_V && buf[ICMP_TYPE_P] == ICMP_TYPE_ECHOREQUEST_V) {
        DEBUG_PRINTLN("Sending ICMP response");
        es.ES_make_echo_reply_from_request(buf,plen);
        return;
      }

      // tcp port www start, compare only the lower byte
      if (buf[IP_PROTO_P] == IP_PROTO_TCP_V && buf[TCP_DST_PORT_H_P] == 0 && buf[TCP_DST_PORT_L_P] == config.boardInfo.port) {
        if (buf[TCP_FLAGS_P] & TCP_FLAGS_SYN_V) {
           DEBUG_PRINTLN("Received syn, sending synack");
           es.ES_make_tcp_synack_from_syn(buf); // make_tcp_synack_from_syn does already send the syn,ack
           return;
        }
        if (buf[TCP_FLAGS_P] & TCP_FLAGS_ACK_V) {
          es.ES_init_len_info(buf); // init some data structures
          dat_p = es.ES_get_tcp_data_pointer();
          if (dat_p == 0) { // we can possibly have no data, just ack:
            if (buf[TCP_FLAGS_P] & TCP_FLAGS_FIN_V) {
              DEBUG_PRINTLN("Received no data, sending ack");
              es.ES_make_tcp_ack_from_any(buf);
            }
            return;
          }

          if (globalErrorBuffer[0]) {
            DEBUG_PRINTLN("Fatal error found");
            plen = es.ES_fill_tcp_data_p(buf, 0, PSTR("HTTP/1.0 500 OK\r\nContent-Type: application/json\r\n\r\n"));
            plen = addToBuffer(buf, plen, "{\"message\":\"");
            plen = addToBuffer(buf, plen, globalErrorBuffer);
            plen = addToBuffer(buf, plen, "\"}");
          } else {
              plen = es.ES_fill_tcp_data_p(buf, 0, PSTR("HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\r\n"));
              plen = addToBuffer(buf, plen, "OK");
//            boolean managed = false;
//            for (int i = 0; p_resource[i].id; i++) {
//              int reslen = strlen(p_resource[i].method);
//              if (strncmp(p_resource[i].method, (char *) & (buf[dat_p]), reslen) == 0
//                && strncmp(p_resource[i].query, (char *) & (buf[dat_p + reslen]), strlen(p_resource[i].query)) == 0) {
//                  plen = p_resource[i].func(buf, dat_p + reslen, plen);
//                  managed = true;
//                  break;
//              }
//            }
//            if (!managed) {
//              plen = es.ES_fill_tcp_data_p(buf, 0, PSTR("HTTP/1.0 404 OK\r\nContent-Type: application/json\r\n\r\n{\"message\":\"404 No resource for this method & url\"}"));
//            }
          }

          es.ES_make_tcp_ack_from_any(buf); // send ack for http get
          //DEBUG_HEXDUMP(buf, plen);
          es.ES_make_tcp_ack_with_data(buf, plen); // send data
        }
      }
    }
}

