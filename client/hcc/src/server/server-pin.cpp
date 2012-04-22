#include "server-pin.h"



static uint16_t pinGetDescription(char *buf, uint8_t pinId) {
    uint16_t plen;

    plen = startResponseHeader(buf, HEADER_200);
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
            plen = startResponseHeader(buf, HEADER_400);
            plen = appendErrorMsg_P(buf, plen, PSTR("PinId overflow"));
            return plen;
        }
        if (strncmp_P(request, PSTR("/value"), 5) == 0) {
            plen = startResponseHeader(buf, HEADER_200);
            plen = addToBufferTCP(buf, plen, getPinValue(pinId));
            return plen;
        } else if ((request[0] == '/' && !request[1])
                || strncmp_P(request, PSTR("HTTP/"), 5) == 0) {
            plen = pinGetDescription(buf, pinId);
            return plen;
        } else {
            plen = startResponseHeader(buf, HEADER_404);
            plen = appendErrorMsg_P(buf, plen, PSTR("No resource on pin for this method & url"));
            return plen;
        }
    } else {
        plen = startResponseHeader(buf, HEADER_400);
        plen = appendErrorMsg_P(buf, plen, PSTR("Cannot read pin number in the request"));
        return plen;
    }
    return plen;
}


