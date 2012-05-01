#include "../board.h"

const prog_char ERROR_PIN_RESERVED[] PROGMEM = "pin%d is reserved";

char *boardCheckConfig() {

    int8_t pinId;
    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinInputDescription[i].pinId)); i++) {
        int type = pgm_read_byte(&pinInputDescription[i].type);
        if (pinId >= 10 && pinId <= 13) {
            return buildGlobalError_P(ERROR_PIN_RESERVED, pinId);
        }
        if (type == ANALOG && pinId <= 13) {
            return buildGlobalError_P(PSTR("pin%d cannot be INPUT ANALOG"), pinId);
        }
    }

    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinOutputDescription[i].pinId)); i++) {
        int type = pgm_read_byte(&pinOutputDescription[i].type);
        if (pinId < 0 || pinId > 13) {
            return buildGlobalError_P(PSTR("pin%d cannot be OUTPUT"), pinId);
        }
        if (pinId >= 10 && pinId <= 13) {
            return buildGlobalError_P(ERROR_PIN_RESERVED, pinId);
        }
        if (type == ANALOG && !(pinId == 3 || pinId == 5 || pinId == 6 || pinId == 9 || pinId == 10 || pinId == 11 || pinId >= 14)) {
            return buildGlobalError_P(PSTR("pin%d cannot be OUTPUT ANALOG"), pinId);
        }
    }
    return 0;
}
