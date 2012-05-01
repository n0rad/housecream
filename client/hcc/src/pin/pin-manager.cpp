#include "pin-manager.h"
#include "../client/client.h"
#include "../hcc.h"
#include "../settings/settings-pin.h"
#include "../driver/pin.h"

float noInputConversion(uint16_t pinValue) {
    return pinValue;
}
int16_t noOutputConversion(float pinValue) {
    return pinValue;
}

////////////////////////////////////////////////////////
// High level pin access (read pin or settings)
////////////////////////////////////////////////////////
const prog_char *setPinValue(uint8_t pinOutputIdx, float value) {
    uint8_t type = pgm_read_byte(&pinOutputDescription[pinOutputIdx].type);
    uint8_t pinId = pgm_read_byte(&pinOutputDescription[pinOutputIdx].pinId);
    PinWrite writefunc = (PinWrite) pgm_read_word(&pinOutputDescription[pinOutputIdx].write);
    PinOutputConversion converter = (PinOutputConversion) pgm_read_word(&pinOutputDescription[pinOutputIdx].convertValue);
    uint16_t lowLvlVal = converter(value);
    if (lowLvlVal > converter(type == ANALOG ? 255 : 1) || value < converter(0)) {
        return PSTR("value overflow");
    }
    settingsPinOutputSetValue(pinOutputIdx, value);
    writefunc(pinId, type, lowLvlVal);
    return 0;
}
float getPinValue(uint8_t pinIdx) {
    if (pinIdx < pinInputSize) {
        uint8_t type = pgm_read_byte(&pinInputDescription[pinIdx].type);
        uint8_t pinId = pgm_read_byte(&pinInputDescription[pinIdx].pinId);
        PinRead readfunc = (PinRead) pgm_read_word(&pinInputDescription[pinIdx].read);
        PinInputConversion converter = (PinInputConversion) pgm_read_word(&pinInputDescription[pinIdx].convertValue);
        return converter(readfunc(pinId, type));
    } else {
        return settingsPinOutputGetValue(pinIdx - pinInputSize);
    }
}

////////////////////////////////////////////////////////
// Low level pin access
////////////////////////////////////////////////////////
uint16_t defaultPinRead(uint8_t pinId, uint8_t type) {
    if (type == ANALOG) {
        DEBUG_PRINT("reading analog ");
        DEBUG_PRINT(pinId);
        DEBUG_PRINT(" ");
        uint16_t val = analogRead(pinId);
        DEBUG_PRINTLN(val);
        return val;
    } else {
        DEBUG_PRINT("reading digital ");
        DEBUG_PRINT(pinId);
        DEBUG_PRINT(" ");
        uint16_t val = digitalRead(pinId);
        DEBUG_PRINTLN(val);
        return val;
    }
}
void defaultPinWrite(uint8_t pinId, uint8_t type, uint16_t value) {
    if (type == ANALOG) {
        DEBUG_PRINT("writing analog ");
        DEBUG_PRINT(pinId);
        DEBUG_PRINT(" ");
        DEBUG_PRINTLN(value);
        analogWrite(pinId, value);
    } else {
        DEBUG_PRINT("writing digital ");
        DEBUG_PRINT(pinId);
        DEBUG_PRINT(" ");
        DEBUG_PRINTLN(value);
        digitalWrite(pinId, value);
    }
}



void pinInit() {
    int8_t pinId;
    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinInputDescription[i].pinId)); i++) {
        pinMode(pinId, INPUT);
        if (pgm_read_byte(&pinInputDescription[i].pullup)) {
            DEBUG_PRINTLN(pinId);
            digitalWrite(pinId, HIGH);
        }
    }
    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinOutputDescription[i].pinId)); i++) {
        pinMode(pinId, OUTPUT);
        float val = settingsPinOutputGetValue(i);
        setPinValue(i, settingsPinOutputGetValue(i));
    }
}




void pinCheckInit() {
//    for (uint8_t i = 0; i < NUMBER_OF_PINS; i++) {
//        int direction = pgm_read_byte(&pinDescriptions[i].direction);
//        if (direction == PIN_INPUT) {
//            PinRead read = (PinRead) pgm_read_word(&pinDescriptions[i].read);
//            previousValue[i] = read(i);
//        }
//    }
}

void pinCheckChange() {
//    if (notification) {
//        return; // TODO do not skip but instead keep nexts to send
//    }
//    for (uint8_t i = 0; i < NUMBER_OF_PINS; i++) {
//        int direction = pgm_read_byte(&pinDescriptions[i].direction);
//        if (direction == PIN_INPUT) {
//            uint16_t oldValue = previousValue[i];
//            PinRead read = (PinRead) pgm_read_word(&pinDescriptions[i].read);
//            uint16_t value = read(i);
//            for (uint8_t j = 0; j < PIN_NUMBER_OF_NOTIFY; j++) {
//                t_notify notify;
//                getConfigPinNotify(i, j, &notify);
//                if (notify.condition != PIN_NOTIFY_NOT_SET) {
//                    if ((notify.condition == PIN_NOTIFY_UNDER_EQ && oldValue > notify.value && value <= notify.value)
//                            || (notify.condition == PIN_NOTIFY_OVER_EQ && oldValue < notify.value && value >= notify.value)) {
//                        clientNotify(i, oldValue, value, notify);
//                    }
//                }
//            }
//            previousValue[i] = value;
//        }
//    }
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
