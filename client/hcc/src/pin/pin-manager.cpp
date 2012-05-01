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
//        DEBUG_PRINT("reading analog ");
//        DEBUG_PRINT(pinId);
//        DEBUG_PRINT(" ");
        uint16_t val = analogRead(pinId);
//        DEBUG_PRINTLN(val);
        return val;
    } else {
//        DEBUG_PRINT("reading digital ");
//        DEBUG_PRINT(pinId);
//        DEBUG_PRINT(" ");
        uint16_t val = digitalRead(pinId);
//        DEBUG_PRINTLN(val);
        return val;
    }
}
void defaultPinWrite(uint8_t pinId, uint8_t type, uint16_t value) {
    if (type == ANALOG) {
//        DEBUG_PRINT("writing analog ");
//        DEBUG_PRINT(pinId);
//        DEBUG_PRINT(" ");
//        DEBUG_PRINTLN(value);
        analogWrite(pinId, value);
    } else {
//        DEBUG_PRINT("writing digital ");
//        DEBUG_PRINT(pinId);
//        DEBUG_PRINT(" ");
//        DEBUG_PRINTLN(value);
        digitalWrite(pinId, value);
    }
}

static uint16_t *previousInputValues;

void pinInit() {
    int8_t pinId;
    previousInputValues = (uint16_t *) malloc(pinInputSize * sizeof(uint16_t));
    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinInputDescription[i].pinId)); i++) {
        pinMode(pinId, INPUT);

        if (pgm_read_byte(&pinInputDescription[i].pullup)) {
            digitalWrite(pinId, HIGH);
        }
        delay(10);
        PinRead readfunc = (PinRead) pgm_read_word(&pinInputDescription[i].read);
        uint8_t type = pgm_read_byte(&pinInputDescription[i].type);
        previousInputValues[i] = readfunc(pinId, type);
    }
    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinOutputDescription[i].pinId)); i++) {
        pinMode(pinId, OUTPUT);
        float val = settingsPinOutputGetValue(i);
        setPinValue(i, settingsPinOutputGetValue(i));
    }
}

void pinCheckChange() {
    if (notification) {
        return; // TODO do not skip but instead keep nexts to send
    }
    int8_t pinId;
    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinInputDescription[i].pinId)); i++) {
        PinRead readfunc = (PinRead) pgm_read_word(&pinInputDescription[i].read);
        uint8_t type = pgm_read_byte(&pinInputDescription[i].type);

        uint16_t oldValue = previousInputValues[i];
        uint16_t value = readfunc(pinId, type);

        for (uint8_t j = 0; j < PIN_NUMBER_OF_NOTIFY; j++) {
            t_notify notify;
            settingsPinGetNotify(i, j, &notify);
            if (notify.condition != 0) {
                if ((notify.condition == UNDER_EQ && oldValue > notify.value && value <= notify.value)
                        || (notify.condition == OVER_EQ && oldValue < notify.value && value >= notify.value)) {
                    clientNotify(pinId, oldValue, value, notify);
                }
            }
        }
        previousInputValues[i] = value;
    }
}
