#include "../board.h"

const prog_char ERROR_PIN_RESERVED[] PROGMEM = "pin%d is reserved";
const prog_char ERROR_PIN_ANALOG[] PROGMEM = "pin%d cannot be ANALOG";

char *boardCheckConfig() {

    int8_t pinId;
    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinInputDescription[i].pinId)); i++) {
        int type = pgm_read_byte(&pinInputDescription[i].type);
        if (pinId >= 10 && pinId <= 13) {
            return buildGlobalError_P(ERROR_PIN_RESERVED, pinId);
        }
        if (type == ANALOG && !(pinId == 3 || pinId == 5 || pinId == 6 || pinId == 9 || pinId == 10 || pinId == 11 || pinId >= 14)) {
            return buildGlobalError_P(ERROR_PIN_ANALOG, pinId);
        }
    }

    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinOutputDescription[i].pinId)); i++) {
        int type = pgm_read_byte(&pinInputDescription[i].type);
        if (pinId < 0 || pinId > 13) {
            return buildGlobalError_P(PSTR("pin%d cannot be OUTPUT"), pinId);
        }
        if (pinId >= 10 && pinId <= 13) {
            return buildGlobalError_P(ERROR_PIN_RESERVED, pinId);
        }
        if (type == ANALOG && !(pinId == 3 || pinId == 5 || pinId == 6 || pinId == 9 || pinId == 10 || pinId == 11 || pinId >= 14)) {
            return buildGlobalError_P(ERROR_PIN_ANALOG, pinId);
        }
    }

////        // check set default value for digital pins
////        // skip digital INPUT pin check to allow 20k pullup start value (but no modification after that)
////        if (i < 10 && (p_pin[i].mode == OUTPUT || p_pin[i].mode == PWM)) {
////          // check that default value is applicable
////          if (checkSetValue(i, p_pin[i].startValue)) {
////            return true;
////          }
////        }
    return 0;
}
