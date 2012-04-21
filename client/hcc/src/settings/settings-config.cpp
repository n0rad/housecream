#include <avr/pgmspace.h>

#include "settings-config.h"
#include "../board.h"

char *buildGlobalError_P(const prog_char *progmem_s, int pin) {
    char *ptr = (char *) malloc(100 * sizeof(char));
    memset(ptr, 0, 100 * sizeof(char));
    sprintf_P(ptr, progmem_s, pin);
    return ptr;
}

char *checkConfig() {
    for (uint8_t i = 0; i < NUMBER_OF_PINS; i++) {
        int direction = pgm_read_byte(&pinDescriptions[i].direction);
        int type = pgm_read_byte(&pinDescriptions[i].type);
        uint16_t min = pgm_read_word(&pinDescriptions[i].valueMin);
        uint16_t max = pgm_read_word(&pinDescriptions[i].valueMax);
        if (direction == PIN_NOTUSED || direction == PIN_RESERVED) {
            continue;
        }

        // check converter
        PinValueConversion conversionFunc = (PinValueConversion) pgm_read_word(&(pinDescriptions[i].convertValue));
        if (conversionFunc == 0) {
            return buildGlobalError_P(PSTR("FATAL ERROR : conversion func not found on pin%d. Forgot to set NUMBER_OF_PIN ?"), i);
        }

        if (direction == PIN_OUTPUT) {
            float res = conversionFunc(max);
            if (type == PIN_DIGITAL) {
                if (res > 1) {
                    return buildGlobalError_P(PSTR("FATAL ERROR : conversion func on pin%d with max value is over 1 (max of digital pin)"), i);
                }
            } else if (type == PIN_ANALOG) {
                if (res > 255) {
                    return buildGlobalError_P(PSTR("FATAL ERROR : conversion func on pin%d with max value is over 255 (max of PWM pin)"), i);
                }

            }
        } else {

        }
    }

    char *error = boardCheckConfig();
    if (error) {
        return error;
    }

    return 0;
}
