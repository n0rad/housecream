#include "client.h"

#define TCP_DATA_P          0x36

static uint16_t addToBufferTCP_P2(char *buf, uint16_t pos, const prog_char *progmem) {
    unsigned char c;
    while ((c = pgm_read_byte(progmem++))) {
            buf[TCP_DATA_P + pos] = c;
            pos++;
    }
    return pos;
}

void clientNotify(int pinId, float oldValue, float value, t_notify notify) {
    notification = (t_notification *) malloc(sizeof(t_notification));
    notification->pinId = pinId;
    notification->value = value;
    notification->oldValue = oldValue;
    notification->notify.condition = notify.condition;
    notification->notify.value = notify.value;
}

uint16_t clientBuildNextQuery(char *buf) {
    uint16_t plen;
    plen = addToBufferTCP_P2(buf, 0, PSTR("PUT /ethershield_log/save.php?pwd=secret&client="));
    plen = addToBufferTCP_P2(buf, plen, PSTR(" HTTP/1.0\r\n"));
//    plen = addToBufferTCP_P2(buf, plen, PSTR("Host: 192.168.1.4\r\n"));
//    plen = addToBufferTCP_P2(buf, plen, PSTR("User-Agent: HouseCream Client\r\n"));
    plen = addToBufferTCP_P2(buf, plen, PSTR("Accept: application/json\r\n"));
    plen = addToBufferTCP_P2(buf, plen, PSTR("Keep-Alive: 300\r\n"));
    plen = addToBufferTCP_P2(buf, plen, PSTR("Connection: keep-alive\r\n\r\n"));
    // {id: 42, oldValue: 42, value: 42, notify : {notifyCondition: "SUP_OR_EQUAL", notifyValue: 42}}
    return plen;
}
