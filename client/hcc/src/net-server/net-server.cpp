#include "net-server.h"
#include "../hcc.h"
#include <avr/pgmspace.h>
#include <avr/eeprom.h>

t_resource  p_resource[] = {
  {GET, "/pin/", pinGet},
  {PUT, "/pin ",  pinPut},
  {GET, "/ ", boardGet},
  {PUT, "/ ",  boardPut},
  {0, 0, 0}
};

uint16_t addToBufferTCPHex(char *buf, uint16_t pos, uint16_t val) {
    static char value_to_add[4] = {0,0,0,0};
    int j = 0;

    sprintf(value_to_add, "%02X", val);
    while (value_to_add[j]) {
     buf[TCP_CHECKSUM_L_P + 3 + pos] = value_to_add[j++];
     pos++;
    }
    return pos;
}

uint16_t addToBufferTCP(char *buf, uint16_t pos, uint16_t val) {
    int j = 0;
    char value_to_add[16] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    sprintf(value_to_add, "%d", val);
    while (value_to_add[j]) {
      buf[TCP_CHECKSUM_L_P + 3 + pos] = value_to_add[j++];
      pos++;
    }
    return pos;
}

uint16_t addToBufferTCP(char *buf, uint16_t pos, char *mem_s) {
    char c;
    while ((c = *(mem_s++))) {
            buf[TCP_CHECKSUM_L_P + 3 + pos] = c;
            pos++;
    }
    return pos;
}

uint16_t addToBufferTCP_p(char *buf, uint16_t pos, const prog_char *progmem_s) {
    char c;
    while ((c = pgm_read_byte(progmem_s++))) {
            buf[TCP_CHECKSUM_L_P + 3 + pos] = c;
            pos++;
    }
    return pos;
}

uint16_t addToBufferTCP_e(char *buf, uint16_t pos, uint8_t *eeprom_s) {
    char c;
    while ((c = eeprom_read_byte(eeprom_s++))) {
            buf[TCP_CHECKSUM_L_P + 3 + pos] = c;
            pos++;
    }
    return pos;
}


uint16_t handleWebRequest(char *buf, uint16_t dataPointer, uint16_t dataLen) {
    DEBUG_PRINT(F("Available memory : "));
    DEBUG_PRINTLN(availableMemory());
    DEBUG_PRINT(F("DATA LEN : "));
    DEBUG_PRINTLN(dataLen);
    DEBUG_PRINTLN(&buf[dataPointer]);

    uint16_t plen;
    if (dataLen >= 999) {
        plen = addToBufferTCP_p(buf, 0, HEADER_413);
        plen = addToBufferTCP_p(buf, plen, PSTR("{\"message\":\"413: Request is too big, please be gentle\"}"));
        return plen;
    }

    if (criticalProblem_p) {
        plen = addToBufferTCP_p(buf, 0, HEADER_500);
        plen = addToBufferTCP_p(buf, plen, PSTR("{\"message\":\""));
        plen = addToBufferTCP_p(buf, plen, criticalProblem_p);
        plen = addToBufferTCP_p(buf, plen, PSTR("\"}"));
        return plen;
    }
    if (definitionError) {
        DEBUG_PRINTLN(definitionError);
        plen = addToBufferTCP_p(buf, 0, HEADER_500);
        plen = addToBufferTCP_p(buf, plen, PSTR("{\"message\":\""));
        plen = addToBufferTCP(buf, plen, definitionError);
        plen = addToBufferTCP_p(buf, plen, PSTR("\"}"));
        return plen;
    }

    boolean managed = false;
    for (int i = 0; p_resource[i].method; i++) {
        int reslen = strlen(p_resource[i].method);
        if (strncmp(p_resource[i].method, (char *) & (buf[dataPointer]), reslen) == 0
                && strncmp(p_resource[i].query, (char *) & (buf[dataPointer + reslen]), strlen(p_resource[i].query)) == 0) {
          plen = p_resource[i].func((char*)buf, dataPointer + reslen, dataLen);
          managed = true;
          break;
        }
    }
    if (!managed) {
        plen = addToBufferTCP_p(buf, 0, HEADER_404);
        plen = addToBufferTCP_p(buf, plen, PSTR("{\"message\":\"404: No resource for this method & url\"}"));
    }
    return plen;
}
