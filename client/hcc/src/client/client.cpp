#include "client.h"

#define TCP_DATA_P          0x36

uint16_t addToBufferTCP_P2(char *buf, uint16_t pos, const prog_char *progmem) {
    unsigned char c;
    while ((c = pgm_read_byte(progmem++))) {
            buf[TCP_DATA_P + pos] = c;
            pos++;
    }
    return pos;
}

void clientNotify(int pinId, float oldValue, float value, t_notify notify) {
//    DEBUG_PRINT("notify for pin: ");
//    DEBUG_PRINT(pinId);
//    DEBUG_PRINT(" old: ");
//    DEBUG_PRINT(oldValue);
//    DEBUG_PRINT(" new: ");
//    DEBUG_PRINT(value);
//    DEBUG_PRINT(" with cond:  ");
//    DEBUG_PRINT(notify.condition);
//    DEBUG_PRINT(" ");
//    DEBUG_PRINTLN(notify.value);
    extern uint8_t clientDataReady;
    clientDataReady = true;
}

uint16_t generateRequest(char *buf) {
    uint16_t plen;
    plen = addToBufferTCP_P2(buf, 0, PSTR("PUT /ethershield_log/save.php?pwd=secret&client="));
    plen = addToBufferTCP_P2(buf, plen, PSTR(" HTTP/1.0\r\n"));
    plen = addToBufferTCP_P2(buf, plen, PSTR("Host: 192.168.1.4\r\n"));
    plen = addToBufferTCP_P2(buf, plen, PSTR("User-Agent: HouseCream Client\r\n"));
    plen = addToBufferTCP_P2(buf, plen, PSTR("Accept: text/html\r\n"));
    plen = addToBufferTCP_P2(buf, plen, PSTR("Keep-Alive: 300\r\n"));
    plen = addToBufferTCP_P2(buf, plen, PSTR("Connection: keep-alive\r\n\r\n"));
    return plen;
}
