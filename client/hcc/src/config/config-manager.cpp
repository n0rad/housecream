#include <avr/pgmspace.h>

#include "../driver/board.h"

char *buildGlobalError_P(const prog_char *progmem_s, int pin) {
    char *ptr = (char *) malloc(100 * sizeof(char));
    memset(ptr, 0, 100 * sizeof(char));
    sprintf_P(ptr, progmem_s, pin);
    return ptr;
}

const int8_t configGetInputPinIdx(uint8_t pinIdToFind) {
    int8_t pinId;
    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinInputDescription[i].pinId)); i++) {
        if (pinId == pinIdToFind) {
            return i;
        }
    }
    return -1;
}

const int8_t configGetOutputPinIdx(uint8_t pinIdToFind) {
    int8_t pinId;
    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinOutputDescription[i].pinId)); i++) {
        if (pinId == pinIdToFind) {
            return i;
        }
    }
    return -1;
}

char *configCheck() {
//    UNCESSERY AS WE CANNOT SEND THIS ERROR
//    uint8_t ip[4];
//    ip[0] = pgm_read_byte(&boardDescription.ip);
//    ip[1] = pgm_read_byte(&boardDescription.ip[1]);
//    ip[2] = pgm_read_byte(&boardDescription.ip[2]);
//    ip[3] = pgm_read_byte(&boardDescription.ip[3]);
//    if (ip[0] == 255 || ip[1] == 255 || ip[2] == 255 || ip[3] == 255 // cannot be 255
//            || ip[0] == 0 || ip[3] == 0) { // cannot start or finish with 0
//        return buildGlobalError_P(PSTR("invalid ip"), 0);
//    }

    int8_t pinId;
    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinInputDescription[i].pinId)); i++) {
        const int8_t inpos = configGetInputPinIdx(pinId);
        const int8_t outpos = configGetOutputPinIdx(pinId);
        if (outpos != -1 || inpos != i) {
            return buildGlobalError_P(PIN_DEFINE_TWICE, pinId);
        }
        uint8_t type = pgm_read_byte(&pinInputDescription[i].type);
        if (!(type == DIGITAL || type == ANALOG)) {
            return buildGlobalError_P(PIN_TYPE_INVALID, pinId);
        }
        for (uint8_t j = 0; j < 4; j++) {
            uint8_t cond = pgm_read_byte(&pinInputDescription[i].notifies[j].condition);
            if (cond) {
                uint32_t tmp = pgm_read_dword(&(pinInputDescription[i].notifies[j].value));
                float value = *((float*)&tmp);
                if (!(cond == OVER_EQ || cond == UNDER_EQ)) {
                    return buildGlobalError_P(PSTR("invalid notify on pin%d"), pinId);
                }
                PinInputConversion conversionFunc = (PinInputConversion) pgm_read_word(&(pinInputDescription[i].convertValue));
                if (type == DIGITAL) {
                    if (value > conversionFunc(1) || value < conversionFunc(0)) {
                        return buildGlobalError_P(NOTIFY_VAL_OVERFLOW, pinId);
                    }
                } else if (value > conversionFunc(1023) || value < conversionFunc(0)) {
                    return buildGlobalError_P(NOTIFY_VAL_OVERFLOW, pinId);
                }
            }
        }
    }

    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinOutputDescription[i].pinId)); i++) {
        const int8_t inpos = configGetInputPinIdx(pinId);
        const int8_t outpos = configGetOutputPinIdx(pinId);
        if (inpos != -1 || outpos != i) {
            return buildGlobalError_P(PIN_DEFINE_TWICE, pinId);
        }
        uint8_t type = pgm_read_byte(&pinOutputDescription[i].type);
        if (!(type == DIGITAL || type == ANALOG)) {
            return buildGlobalError_P(PIN_TYPE_INVALID, pinId);
        }

        uint32_t start = pgm_read_dword(&(pinOutputDescription[i].startValue));
        uint32_t min = pgm_read_dword(&(pinOutputDescription[i].valueMin));
        uint32_t max = pgm_read_dword(&(pinOutputDescription[i].valueMax));
        PinOutputConversion conversionFunc = (PinOutputConversion) pgm_read_word(&(pinOutputDescription[i].convertValue));
        if (type == DIGITAL) {
            if (!(conversionFunc(*((float*)&start)) == 0 || conversionFunc(*((float*)&start)) == 1)) {
                DEBUG_PRINTLN((conversionFunc(*((float*)&start))));
                return buildGlobalError_P(PIN_START_INVALID, pinId);
            }
            if (conversionFunc(*((float*)&min)) != 0) {
                return buildGlobalError_P(PIN_MIN_INVALID, pinId);
            }
            if (conversionFunc(*((float*)&max)) != 1) {
                return buildGlobalError_P(PIN_MAX_INVALID, pinId);
            }
        } else {
            if (conversionFunc(*((float*)&start)) > 255 || conversionFunc(*((float*)&start)) < 0) {
                return buildGlobalError_P(PIN_START_INVALID, pinId);
            }
            if (conversionFunc(*((float*)&min)) < 0) {
                return buildGlobalError_P(PIN_MIN_INVALID, pinId);
            }
            if (conversionFunc(*((float*)&max)) > 255) {
                return buildGlobalError_P(PIN_MAX_INVALID, pinId);
            }
        }

    }

    char *error = boardCheckConfig();
    if (error) {
        return error;
    }

    return 0;
}
