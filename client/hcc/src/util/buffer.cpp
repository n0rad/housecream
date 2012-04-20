#include "buffer.h"

uint16_t addToBufferTCPHex(char *buf, uint16_t pos, uint16_t val) {
    static char value_to_add[4] = {0,0,0,0};
    int j = 0;

    sprintf_P(value_to_add, sprintfpHEX, val);
    while (value_to_add[j]) {
     buf[TCP_CHECKSUM_L_P + 3 + pos] = value_to_add[j++];
     pos++;
    }
    return pos;
}

uint16_t addToBufferTCP(char *buf, uint16_t pos, char val) {
    buf[TCP_CHECKSUM_L_P + 3 + pos++] = buf[0];
    return pos;
}

uint16_t addToBufferTCP(char *buf, uint16_t pos, uint16_t val) {
    int j = 0;
    char value_to_add[16] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    sprintf_P(value_to_add, sprintfpDEC, val);
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

uint16_t addToBufferTCP_P(char *buf, uint16_t pos, const prog_char *progmem) {
    unsigned char c;
    while ((c = pgm_read_byte(progmem++))) {
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
