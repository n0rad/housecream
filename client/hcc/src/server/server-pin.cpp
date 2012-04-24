#include "server-pin.h"



uint16_t pinPutValue(char *buf, uint16_t dat_p, uint16_t plen, uint8_t pinId) {
    plen = startResponseHeader(&buf, HEADER_200);
    plen = addToBufferTCP_P(buf, plen, PSTR("pinPutValue"));
    return plen;
}

uint16_t pinGetValue(char *buf, uint16_t dat_p, uint16_t plen, uint8_t pinId) {
    plen = startResponseHeader(&buf, HEADER_200);
    plen = addToBufferTCP_P(buf, plen, PSTR("pinGetValue"));
    return plen;
}

uint16_t pinPut(char *buf, uint16_t dat_p, uint16_t plen, uint8_t pinId) {
    plen = startResponseHeader(&buf, HEADER_200);
    plen = addToBufferTCP_P(buf, plen, PSTR("pinPut"));
    return plen;
}

uint16_t pinGet(char *buf, uint16_t dat_p, uint16_t plen, uint8_t pinId) {
    plen = startResponseHeader(&buf, HEADER_200);
    plen = addToBufferTCP_P(buf, plen, PSTR("{\"id\":"));
    plen = addToBufferTCP(buf, plen, (uint16_t) pinId);


    plen = addToBufferTCP_P(buf, plen, PSTR(",\"name\":\""));
    plen = addToBufferTCP_E(buf, plen, getConfigPinName_E(pinId));

    plen = addToBufferTCP_P(buf, plen, PSTR("\",\"description\":\""));
    plen = addToBufferTCP_P(buf, plen, (const prog_char *)&pinDescriptions[pinId].description);

    uint8_t direction = pgm_read_byte(&pinDescriptions[pinId].direction);
    plen = addToBufferTCP_P(buf, plen, PSTR("\",\"direction\":\""));
    plen = addToBufferTCP_P(buf, plen, (const prog_char *) pgm_read_byte(&pinDirection[direction - 1]));
    plen = addToBufferTCP_P(buf, plen, PSTR("\""));

    if (direction != PIN_RESERVED && direction != PIN_NOTUSED) {
        uint8_t type = pgm_read_byte(&pinDescriptions[pinId].type);
        plen = addToBufferTCP_P(buf, plen, PSTR(",\"type\":\""));
        plen = addToBufferTCP_P(buf, plen, (const prog_char *) pgm_read_byte(&pinType[type - 1]));

        PinValueConversion conversionFunc = (PinValueConversion) pgm_read_word(&(pinDescriptions[pinId].convertValue));

        plen = addToBufferTCP_P(buf, plen, PSTR("\",\"valueMin\":"));
        float minValue;
        memcpy_P(&minValue, &pinDescriptions[pinId].valueMin, sizeof(float));
        plen = addToBufferTCP(buf, plen, conversionFunc(minValue));

        plen = addToBufferTCP_P(buf, plen, PSTR(",\"valueMax\":"));
        float maxValue;
        memcpy_P(&maxValue, &pinDescriptions[pinId].valueMax, sizeof(float));
        plen = addToBufferTCP(buf, plen, conversionFunc(maxValue));

        if (direction == PIN_OUTPUT) {
            plen = addToBufferTCP_P(buf, plen, PSTR(",\"startValue\":"));
            plen = addToBufferTCP(buf, plen, getConfigPinValue(pinId));
        }

        if (direction == PIN_INPUT) {
            plen = addToBufferTCP_P(buf, plen, PSTR(",\"notifies\":["));
            for (uint8_t i = 0; i < PIN_NUMBER_OF_NOTIFY; i++) {
                t_notify notify;
                getConfigPinNotify(pinId, i, &notify);
                if (notify.condition > PIN_NOTIFY_NOT_SET) {
                    if (i) {
                        plen = addToBufferTCP_P(buf, plen, PSTR(","));
                    }
                    plen = addToBufferTCP_P(buf, plen, PSTR("{\"notifyCondition\":\""));
                    plen = addToBufferTCP_P(buf, plen, (const prog_char *) pgm_read_byte(&pinNotification[notify.condition - 1]));

                    plen = addToBufferTCP_P(buf, plen, PSTR("\",\"notifyValue\":"));
                    plen = addToBufferTCP(buf, plen, notify.value);
                    plen = addToBufferTCP_P(buf, plen, PSTR("}"));
                }
            }
            plen = addToBufferTCP_P(buf, plen, PSTR("]"));
        }

        plen = addToBufferTCP_P(buf, plen, PSTR(",\"value\":"));
        plen = addToBufferTCP(buf, plen, getPinValue(pinId));
    }
    plen = addToBufferTCP_P(buf, plen, PSTR("}"));
    return plen;
}


