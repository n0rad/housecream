#include "pin-manager.h"

static uint16_t previousValue[NUMBER_OF_PINS];

float noConversion(float pinValue) {
    return pinValue;
}

uint16_t defaultPinRead(uint8_t pinId) {
    uint8_t direction = pgm_read_byte(&pinDescriptions[pinId].direction);
    uint8_t type = pgm_read_byte(&pinDescriptions[pinId].type);
    if (direction == PIN_INPUT) {
        return pinReadValue(pinId, type);
    } else if (direction == PIN_OUTPUT) {
        return getConfigPinValue(pinId);
    }
    return 0;
}

void defaultPinWrite(uint8_t pinId, uint16_t value) {
    pinWriteValue(pinId, value);
}


float getPinValue(uint8_t pinId) {
    PinRead read = (PinRead) pgm_read_word(&pinDescriptions[pinId].read);
    PinValueConversion convert = (PinValueConversion) pgm_read_word(&pinDescriptions[pinId].convertValue);
    return convert(read(pinId));
}

void pinCheckInit() {
    for (uint8_t i = 0; i < NUMBER_OF_PINS; i++) {
        int direction = pgm_read_byte(&pinDescriptions[i].direction);
        if (direction == PIN_INPUT) {
            PinRead read = (PinRead) pgm_read_word(&pinDescriptions[i].read);
            previousValue[i] = read(i);
        }
    }
}

void pinCheckChange() {
    if (notification) {
        return; // TODO do not skip but instead keep nexts to send
    }
    for (uint8_t i = 0; i < NUMBER_OF_PINS; i++) {
        int direction = pgm_read_byte(&pinDescriptions[i].direction);
        if (direction == PIN_INPUT) {
            uint16_t oldValue = previousValue[i];
            PinRead read = (PinRead) pgm_read_word(&pinDescriptions[i].read);
            uint16_t value = read(i);
            for (uint8_t j = 0; j < PIN_NUMBER_OF_NOTIFY; j++) {
                t_notify notify;
                getConfigPinNotify(i, j, &notify);
                if (notify.condition != PIN_NOTIFY_NOT_SET) {
                    if ((notify.condition == PIN_NOTIFY_UNDER_EQ && oldValue > notify.value && value <= notify.value)
                            || (notify.condition == PIN_NOTIFY_OVER_EQ && oldValue < notify.value && value >= notify.value)) {
                        clientNotify(i, oldValue, value, notify);
                    }
                }
            }
            previousValue[i] = value;
        }
    }
}

void pinInit() {
    pinMode(0, INPUT);
    pinMode(2, INPUT);
    pinMode(3, OUTPUT);
    pinMode(4, INPUT);
    pinMode(5, OUTPUT);
}




//
//
//int getValue(int pin) {
//  if (checkGetValue(pin)) {
//    return 0;
//  }
//  if (p_pin[pin].mode == ANALOG) {
//    return analogRead(pin);
//  } else {
//    return digitalRead(pin);
//  }
//}
//
///**
// * check that a value is setable to a pin
// * @return 1 if error
// * @TODO duamilanov specific function
// */
//int checkSetValue(int pin, int value) {
//  if (pin >= 10) {
//    sprintf(globalErrorBuffer, "You can not set this pin (%d)", pin);
//    return 1;
//  }
//
//  if (p_pin[pin].mode == PWM) {
//    if (value < 0 || value > 255) {
//      sprintf(globalErrorBuffer, "This value (%d) can not be set to PWM pin (%d)", value, pin);
//      return 1;
//    }
//  } else if (p_pin[pin].mode == OUTPUT) {
//    if (value < 0 || value > 1) {
//      sprintf(globalErrorBuffer, "This value (%d) can not be set to OUTPUT pin (%d)", value, pin);
//      return 1;
//    }
//  } else if (p_pin[pin].mode == INPUT) {
//    if (value < 0 || value > 1) {
//      sprintf(globalErrorBuffer, "This value (%d) can not be set to INPUT pin (%d)", value, pin);
//      return 1;
//    }
//    // disable 20k pullup usage at run
//    sprintf(globalErrorBuffer, "This pin (%d) is set as an input and can not be set", pin);
//    return 1;
//  } else {
//    sprintf(globalErrorBuffer, "This pin (%d) is set as not used and can not be set", pin);
//    return 1;
//  }
//  return 0;
//}
//
//
///**
// * check if value is setable and set it
// */
//int setValue(int pin, int value) {
//  if (checkSetValue(pin, value)) {
//    return 1;
//  }
//
//  DEBUG_PRINT("set pin (");
//  //DEBUG_PRINTDEC(pin);
//  DEBUG_PRINT(" ");
//  DEBUG_PRINT(p_pin[pin].mode == OUTPUT ? "OUTPUT" : "PWM");
//  DEBUG_PRINT(") to value : ");
//  DEBUG_PRINTDEC(value);
//  DEBUG_PRINTLN("");
//
//  if (p_pin[pin].mode == PWM) {
//    analogWrite(pin, value);
//  } else {
//    digitalWrite(pin, value);
//  }
//  return 0;
//}
//
//
///**
// * init pins as input or output and set starting value
// */
//void initPins() {
//  for (int i = 0; i < p_pinSize; i++) {
//    // skip notused and analog
//    if (i >= 10 || p_pin[i].mode == NOTUSED) {
//      continue;
//    }
//
//    // init input and output (PWM does not need init)
//    if (p_pin[i].mode != PWM) {
//      DEBUG_PRINT("set pin (");
//      DEBUG_PRINTDEC(i);
//      DEBUG_PRINT(") to mode ");
//
//      DEBUG_PRINT((p_pin[i].mode == INPUT ? "INPUT" : (p_pin[i].mode == OUTPUT ? "OUTPUT" : "UNKNOWN")));
//      DEBUG_PRINT(" (");
//      DEBUG_PRINT(p_pin[i].description);
//      DEBUG_PRINTLN(")");
//      pinMode(i, p_pin[i].mode);
//    }
//
//    // set default value
//    if (p_pin[i].mode == OUTPUT || p_pin[i].mode == PWM) {
//      if (setValue(i, p_pin[i].startValue)) {
//        // error when set
//        DEBUG_PRINTLN(globalErrorBuffer);
//        return;
//      }
//    } else if (p_pin[i].mode == INPUT && p_pin[i].startValue == HIGH) {
//      DEBUG_PRINT("Enable 20k pullup resistor on pin ");
//      DEBUG_PRINTDEC(i);
//      DEBUG_PRINTLN("");
//      digitalWrite(i, HIGH);
//    }
//  }
//}
//
