#include "server-pin.h"

uint16_t pinPut(char *buf, uint16_t dat_p, uint16_t plen) {
    return 0;
}

uint16_t pinGet(char *buf, uint16_t dat_p, uint16_t plen) {
    uint8_t pinId;
    char request[15] = { 0 };

    //sscanf will always return 0 or 2 because it read HTTP/1.1 when there is nothing after the pinId
    int found = sscanf_P((char *) &buf[dat_p + 5], PSTR("%d%s"), &pinId, request);
    if (found != 0) {
        if (pinId < 0 || pinId > NUMBER_OF_PINS - 1) {
            plen = addToBufferTCP_p(buf, 0, HEADER_400);
            plen = addToBufferTCP_p(buf, plen, PSTR("{\"message\":\"PinId overflow\"}"));
            return plen;
        }
        if (strncmp_P(request, PSTR("/value"), 5) == 0) {
            plen = addToBufferTCP_p(buf, 0, HEADER_200);
//            plen = addToBufferTCP(buf, plen, getPinValue(pinId));
            return plen;
        } else if ((request[0] == '/' && !request[1])
                || strncmp_P(request, PSTR("HTTP/"), 5) == 0) {
            plen = pinGetDescription(buf, pinId);
            return plen;
        } else if (strncmp_P(request, PSTR("/info"), 5) == 0) {
            plen = addToBufferTCP_p(buf, 0, HEADER_200);
            plen = addToBufferTCP_p(buf, plen, PSTR("{info}"));
            return plen;
        } else {
            plen = addToBufferTCP_p(buf, 0, HEADER_404);
            plen = addToBufferTCP_p(buf, plen, BAD_PIN_REQUEST);
            return plen;
        }
    } else {
        plen = addToBufferTCP_p(buf, 0, HEADER_400);
        plen = addToBufferTCP_p(buf, plen, CANNOT_READ_PINID);
        return plen;
    }
    return plen;
}

uint16_t pinGetDescription(char *buf, uint8_t pinId) {
    uint16_t plen;

    plen = addToBufferTCP_p(buf, 0, HEADER_200);
    plen = addToBufferTCP_p(buf, plen, PSTR("{\"description\":\""));
    plen = addToBufferTCP_p(buf, plen, (const prog_char *)&pinDescriptions[pinId].description);

    uint8_t direction = pgm_read_byte(&pinDescriptions[pinId].direction);
    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"direction\":\""));
    plen = addToBufferTCP_p(buf, plen, pinDirection[direction]);

    uint8_t type = pgm_read_byte(&pinDescriptions[pinId].type);
    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"type\":\""));
    plen = addToBufferTCP_p(buf, plen, pinType[type]);

//    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"valueMin\":\""));
//    plen = addToBufferTCP_p(buf, plen, type ? PSTR("0") : PSTR("0"));
//
//    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"valueMax\":\""));
//    plen = addToBufferTCP_p(buf, plen, type ? PSTR("1") : PSTR("1"));

//
//  plen = addToBuffer(buf, plen, "\",\"valueStep\":\"");
//  plen = addToBuffer(buf, plen, current.valueStep);

    plen = addToBufferTCP_p(buf, plen, PSTR("\"}"));
    return plen;
}
