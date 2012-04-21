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
        if (direction == PIN_NOTUSED || direction == PIN_RESERVED) {
            continue;
        }
        if (direction != PIN_INPUT && direction != PIN_OUTPUT) {
            return buildGlobalError_P(PSTR("Pin direction in not set for %d"), i);
        }

        int type = pgm_read_byte(&pinDescriptions[i].type);

        float min;
        float max;
        memcpy_P(&min, &pinDescriptions[i].valueMin, sizeof(float));
        memcpy_P(&max, &pinDescriptions[i].valueMax, sizeof(float));


        // check converter
        PinValueConversion conversionFunc = (PinValueConversion) pgm_read_word(&(pinDescriptions[i].convertValue));
        if (conversionFunc == 0) {
            return buildGlobalError_P(PSTR("Conversion func not found on pin%d. Forgot to set NUMBER_OF_PIN ?"), i);
        }

        if (direction == PIN_OUTPUT) {
            float maxRes = conversionFunc(max);
            float minRes = conversionFunc(min);
            if (type == PIN_DIGITAL) {
                if (maxRes != 1) {
                    return buildGlobalError_P(PSTR("Conversion func on pin%d with max value is not 1 (max of digital pin)"), i);
                }
                if (minRes != 0) {
                    return buildGlobalError_P(PSTR("Conversion func on pin%d with min value is not 0 (min of digital pin)"), i);
                }
            } else if (type == PIN_ANALOG) {
                if (maxRes > 255) {
                    return buildGlobalError_P(PSTR("Conversion func on pin%d with max value is over 255 (max of PWM pin)"), i);
                }
                if (minRes < 0) {
                    return buildGlobalError_P(PSTR("Conversion func on pin%d with min value is under 0 (min of PWM pin)"), i);
                }
            }
        } else if (direction == PIN_INPUT) {

        }
    }

    char *error = boardCheckConfig();
    if (error) {
        return error;
    }

    return 0;
}
