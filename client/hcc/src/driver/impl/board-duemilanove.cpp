#include "../../hcc.h"


static char *buildGlobalError_P(const prog_char *progmem_s, int pin) {
    va_list argp;
    char *ptr = (char *) malloc(100 * sizeof(char));
    memset(ptr, 0, 100 * sizeof(char));
    sprintf_P(ptr, progmem_s, pin);
    return ptr;
}

char *checPinDescriptions() {

    for (uint8_t i = 0; i < NUMBER_OF_PINS; i++) {
        int direction = pgm_read_byte(&pinDescriptions[i].direction);
        int type = pgm_read_byte(&pinDescriptions[i].type);
        uint16_t min = pgm_read_word(&pinDescriptions[i].valueMin);
        uint16_t max = pgm_read_word(&pinDescriptions[i].valueMax);
        if (direction == PIN_NOTUSED || direction == PIN_RESERVED) {
            continue;
        }

        // check that for duemilanov the pin 10 to 13 are not used (used by ethernet shield)
        if (i >= 10 && i <= 13) {
            return buildGlobalError_P(PSTR("FATAL ERROR : Pin (%d) is used by ethernet shield you cannot use it"), i);
        }

        if (type == PIN_ANALOG && !(i == 3 || i == 5 || i == 6 || i == 9 || i == 10 || i == 11 || i >= 14)) {
            return buildGlobalError_P(PSTR("FATAL ERROR : pin cannot be used as analogic %d"), i);
        }

        // check that analog pin is in input mode
        if (i > 13 && direction == PIN_OUTPUT) {
            return buildGlobalError_P(PSTR("FATAL ERROR : Analog pin (%d) can only be in input mode"), i);
        }

        // check converter
        conversion conversionFunc = (conversion) pgm_read_word(&(pinDescriptions[i].convertValue));
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

//        // check set default value for digital pins
//        // skip digital INPUT pin check to allow 20k pullup start value (but no modification after that)
//        if (i < 10 && (p_pin[i].mode == OUTPUT || p_pin[i].mode == PWM)) {
//          // check that default value is applicable
//          if (checkSetValue(i, p_pin[i].startValue)) {
//            return true;
//          }
//        }
    }
    return 0;
}


///**
// * Check that pin is readable
// */
//int checkGetValue(int pin) {
//  if(p_pin[pin].mode == PWM) {
//    sprintf(globalErrorBuffer, "You can not read this pin (%d) as its in PWM mode", pin);
//    return 1;
//  }
//  return 0;
//}


///**
// * Check pin definition in p_pin for this board version
// * @TODO duamilanov specific function
// */
//int checkDef() {
//  for (int i = 0; i < p_pinSize; i++) {
//    // skip not used pin
//    if (p_pin[i].mode == NOTUSED) {
//      continue;
//    }
//
//    // check that for duemilanov the pin 10 to 13 are not used (used by ethernet shield)
//    if (i >= 10 && i <= 13) {
//      sprintf(globalErrorBuffer, "FATAL ERROR : Pin (%d) is used by ethernet shield you can not use it", i);
//      return 1;
//    }
//
//    // check pwm pin
//    if (p_pin[i].mode == PWM && !(i == 3 || i == 5 || i == 6 || i == 9 || i == 10 || i == 11)) {
//      sprintf(globalErrorBuffer, "FATAL ERROR : Mode PWM can not be used for pin %d", i);
//      return 1;
//    }
//
//    // check that analog pin is in ANALOG or DIGITAL
//    if (i > 13 && !(p_pin[i].mode == ANALOG || p_pin[i].mode == DIGITAL)) {
//      sprintf(globalErrorBuffer, "FATAL ERROR : Analog pin (%d) can only be in ANALOG or DIGITAL mode", i);
//      return 1;
//    }
//
//    // check set default value for digital pins
//    // skip digital INPUT pin check to allow 20k pullup start value (but no modification after that)
//    if (i < 10 && (p_pin[i].mode == OUTPUT || p_pin[i].mode == PWM)) {
//      // check that default value is applicable
//      if (checkSetValue(i, p_pin[i].startValue)) {
//        return 1;
//      }
//    }
//  }
//  return 0;
//}
