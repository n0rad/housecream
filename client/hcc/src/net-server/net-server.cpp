#include "net-server.h"
#include "../hcc.h"
#include <avr/pgmspace.h>

#define TCP_CHECKSUM_L_P    0x33

uint16_t addToBufferTCP_p(uint8_t *buf, uint16_t pos, const prog_char *progmem_s) {
    char c;
    while ((c = pgm_read_byte(progmem_s++))) {
            buf[TCP_CHECKSUM_L_P + 3 + pos] = c;
            pos++;
    }
    return pos;
}

uint16_t handleWebRequest(uint8_t *buf, uint16_t dataPointer) {
    uint16_t plen;
    plen = addToBufferTCP_p(buf, 0, PSTR("HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\r\nOK"));
    return plen;
}




//
//
///**
// * Add a char * to buf buffer.
// * @return new position in buf
// */
//uint16_t addToBuffer(uint8_t *buf, unsigned long pos, char *value, unsigned int len) {
//  memcpy(&buf[TCP_CHECKSUM_L_P + 3 + pos], value, len);
//  return pos + len;
//}
//
///**
// * Add a char * to buf buffer.
// * @return new position in buf
// */
//uint16_t addToBuffer(uint8_t *buf, unsigned long pos, char *value) {
//  size_t size = strlen(value);
//  memcpy(&buf[TCP_CHECKSUM_L_P + 3 + pos], value, size);
//  return pos + size;
//}
//
///**
// * Add a long char* representation to buf buffer.
// * @return new position in buf
// */
//uint16_t addToBuffer(uint8_t *buf, unsigned long pos, uint16_t value) {
//   static char value_to_add[16] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//   int j = 0;
//
//   sprintf(value_to_add, "%d", value);
//   while (value_to_add[j]) {
//     buf[TCP_CHECKSUM_L_P + 3 + pos] = value_to_add[j++];
//     pos++;
//   }
//   return pos;
//}
//
///**
// * Add a long char* representation to buf buffer.
// * @return new position in buf
// */
//uint16_t addToBufferHex(uint8_t *buf, unsigned long pos, uint16_t value) {
//   static char value_to_add[3] = {0,0,0};
//   int j = 0;
//
//   sprintf(value_to_add, "%02X", value);
//   while (value_to_add[j]) {
//     buf[TCP_CHECKSUM_L_P + 3 + pos] = value_to_add[j++];
//     pos++;
//   }
//   return pos;
//}
//


//          if (globalErrorBuffer[0]) {
//            DEBUG_PRINTLN(F("Fatal error found"));
//            plen = es.ES_fill_tcp_data_p(buf, 0, F("HTTP/1.0 500 OK\r\nContent-Type: application/json\r\n\r\n"));
////            plen = addToBuffer(buf, plen, "{\"message\":\"");
////            plen = addToBuffer(buf, plen, globalErrorBuffer);
////            plen = addToBuffer(buf, plen, "\"}");
//          } else {
//              plen = es.ES_fill_tcp_data_p(buf, 0, PSTR("HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\r\nOK"));
//              plen = addToBuffer(buf, plen, "OK");
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
//          }


//
//t_resource  p_resource[] = {
//  {1, GET, "/ ", rootGet},
//  {2, GET, "/pin/", pinGet},
////  {PUT, "/ ",  },
//
//  {0, 0, 0, 0}
//};
//
//
//uint16_t pinGet(uint8_t *buf, uint16_t dat_p, uint16_t plen) {
//  int pinId;
//  char request[20] = {0};
//
//  //sscanf will always return 0 or 2 because it read HTTP/1.1 when there is nothing after the pinId
//  int found = sscanf((char *) &buf[dat_p + 5], "%d%s", &pinId, request);
//  if (found != 0) {
//    if (pinId < 0 || pinId > p_pinSize - 1) {
//      plen = es.ES_fill_tcp_data_p(buf, 0, PSTR("HTTP/1.0 400 OK\r\nContent-Type: application/json\r\n\r\n"));
//      plen = addToBuffer(buf, plen, "{\"message\":\"PinId overflow\"}");
//      return plen;
//    }
//    if ((request[0] == '/' && !request[1]) || strncmp("HTTP/", request, 5) == 0) {
//      plen = es.ES_fill_tcp_data_p(buf, 0, PSTR("HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\r\n"));
//      plen = addToBuffer(buf, plen, "{root}");
//      return plen;
//    } else if (strncmp("/value", request, 5) == 0) {
//      plen = es.ES_fill_tcp_data_p(buf, 0, PSTR("HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\r\n"));
//      plen = addToBuffer(buf, plen, getValue(pinId));
//      return plen;
//    } else if (strncmp("/info", request, 4) == 0) {
//      plen = es.ES_fill_tcp_data_p(buf, 0, PSTR("HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\r\n"));
//      plen = addToBuffer(buf, plen, "{info}");
//      return plen;
//    } else {
//      plen = es.ES_fill_tcp_data_p(buf, 0, PSTR("HTTP/1.0 404 OK\r\nContent-Type: application/json\r\n\r\n"));
//      plen = addToBuffer(buf, plen, "{\"message\":\"404 No resource on pin for this method & url\"}");
//      return plen;
//    }
//  } else {
//    plen = es.ES_fill_tcp_data_p(buf, 0, PSTR("HTTP/1.0 400 OK\r\nContent-Type: application/json\r\n\r\n"));
//    plen = addToBuffer(buf, plen, "{\"message\":\"Cannot read pin number in the request\"}");
//    return plen;
//  }
//  return plen;
//}
//
//
//uint16_t pinGetDescription(uint8_t *buf, uint16_t pinId) {
//  t_pin current = p_pin[pinId];
//  uint16_t plen = es.ES_fill_tcp_data_p(buf, 0, PSTR("HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\r\n"));
//
////  plen = addToBuffer(buf, plen, "{\"technicalDescription\":\"");
////  plen = addToBuffer(buf, plen, current.technicalDescription);
////
////  plen = addToBuffer(buf, plen, "\",\"direction\":\"");
////  plen = addToBuffer(buf, plen, current.direction);
////
////  plen = addToBuffer(buf, plen, "\",\"type\":\"");
////  plen = addToBuffer(buf, plen, current.type);
////
////  plen = addToBuffer(buf, plen, "\",\"pullUp\":\"");
////  plen = addToBuffer(buf, plen, current.pullUp);
////
////  plen = addToBuffer(buf, plen, "\",\"valueMin\":\"");
////  plen = addToBuffer(buf, plen, current.valueMin);
////
////  plen = addToBuffer(buf, plen, "\",\"valueMax\":\"");
////  plen = addToBuffer(buf, plen, current.valueMax);
////
////  plen = addToBuffer(buf, plen, "\",\"valueStep\":\"");
////  plen = addToBuffer(buf, plen, current.valueStep);
//
//  plen = addToBuffer(buf, plen, "\"}");
//  return plen;
//}
//
//
///**
// * Fill buffer with default response
// */
//uint16_t fillDefaultResponseBuffer(uint8_t *buf) {
//  uint16_t plen = 0;
//
//  plen = es.ES_fill_tcp_data_p(buf, plen, PSTR("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\r\n"));
//  plen = es.ES_fill_tcp_data_p(buf, plen, PSTR("{"));
//  int flag = 0;
//  for (int i = 0; i < p_pinSize; i++) {
//    if (!(p_pin[i].mode == INPUT || p_pin[i].mode == ANALOG || p_pin[i].mode == DIGITAL)) {
//      continue;
//    }
//    if (flag) {
//      plen = es.ES_fill_tcp_data_p(buf, plen, PSTR(","));
//    }
//
//    plen = addToBuffer(buf, plen, i);
//    plen = es.ES_fill_tcp_data_p(buf, plen, PSTR(":"));
//    int sensorValue = getValue(i);
//
//    plen = addToBuffer(buf, plen, sensorValue);
//    flag++;
//  }
//  plen = es.ES_fill_tcp_data_p(buf, plen, PSTR("}"));
//  return(plen);
//}
//
//
//
//uint16_t rootGet(uint8_t *buf, uint16_t dat_p, uint16_t plen) {
//  plen = es.ES_fill_tcp_data_p(buf, 0, PSTR("HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\r\n"));
//  plen = addToBuffer(buf, plen, "{\"software\":\"HouseCream Client\", \"version\":\"");
//  plen = addToBuffer(buf, plen, HCC_VERSION);
//
//  plen = addToBuffer(buf, plen, "\",\"hardware\":\"");
//  plen = addToBuffer(buf, plen, HARDWARE);
//
//  plen = addToBuffer(buf, plen, "\",\"name\":\"");
//  plen = addToBuffer(buf, plen, name);
//
//  plen = addToBuffer(buf, plen, "\",\"description\":\"");
//  plen = addToBuffer(buf, plen, description);
//
//  plen = addToBuffer(buf, plen, "\",\"notifyUrl\":\"");
//  plen = addToBuffer(buf, plen, notifyUrl);
//
//  plen = addToBuffer(buf, plen, "\",\"technicalDescription\":\"");
//  plen = addToBuffer(buf, plen, technicalDescription);
//
//  plen = addToBuffer(buf, plen, "\",\"ip\":\"");
//  plen = addToBuffer(buf, plen, ip[0]);
//  plen = addToBuffer(buf, plen, ".");
//  plen = addToBuffer(buf, plen, ip[1]);
//  plen = addToBuffer(buf, plen, ".");
//  plen = addToBuffer(buf, plen, ip[2]);
//  plen = addToBuffer(buf, plen, ".");
//  plen = addToBuffer(buf, plen, ip[3]);
//
//  plen = addToBuffer(buf, plen, "\",\"port\":\"");
//  plen = addToBuffer(buf, plen, port);
//
//  plen = addToBuffer(buf, plen, "\",\"numberOfPin\":\"");
//  plen = addToBuffer(buf, plen, p_pinSize);
//
////  plen = addToBuffer(buf, plen, "\",\"mac\":\"");
////  plen = addToBufferHex(buf, plen, mac[0]);
////  plen = addToBufferHex(buf, plen, mac[1]);
////  plen = addToBufferHex(buf, plen, mac[2]);
////  plen = addToBufferHex(buf, plen, mac[3]);
////  plen = addToBufferHex(buf, plen, mac[4]);
////  plen = addToBufferHex(buf, plen, mac[5]);
//
//  plen = addToBuffer(buf, plen, "\"}");
////    private String mac;
//
//  return plen;
//}
