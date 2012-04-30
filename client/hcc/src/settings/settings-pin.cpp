#include "settings-pin.h"

uint8_t currentSetPinIdx;

uint16_t getConfigPinValue(uint8_t pinIdx) {
    uint16_t eepromPos = sizeof(t_boardSettings) + (sizeof(t_pinInputSettings) * pinInputSize) + (sizeof(t_pinOutputSettings) * pinIdx);
    eepromPos += offsetof(t_pinOutputSettings, lastValue);
    return eeprom_read_word((const uint16_t *)eepromPos);
}

void getConfigPinNotify(uint8_t pinIdx, uint8_t notifyId, t_notify *notify) {
    uint16_t eepromPos = sizeof(t_boardSettings) + (sizeof(t_pinInputSettings) * pinIdx);
    eepromPos += offsetof(t_pinInputSettings, notifies) + (sizeof(t_notify) * notifyId);
    eeprom_read_block((void *)notify, (char *)eepromPos, sizeof(t_notify));
}

uint8_t *getConfigPinName_E(uint8_t pinIdx) {
    uint16_t eepromPinIdx = sizeof(t_boardSettings);
    if (pinIdx < pinInputSize) {
        eepromPinIdx += (sizeof(t_pinInputSettings) * pinIdx);
    } else {
        eepromPinIdx += (sizeof(t_pinInputSettings) * pinInputSize) + (sizeof(t_pinOutputSettings) * (pinIdx - pinInputSize));
    }
    return (uint8_t *)eepromPinIdx /* + offsetof(t_pin*Settings, name); not needed as name is first elem in both */;
}

/////////////////

const prog_char *setConfigPinId(char *buf, uint16_t len, uint8_t index) {
    return setConfigBoardPinId(buf, len, currentSetPinIdx);
}
const prog_char *setConfigPinName(char *buf, uint16_t len, uint8_t index) {
    if (len >  CONFIG_PIN_NAME_SIZE - 1) {
        return NAME_TOO_LONG;
    }
    uint16_t pinPos;
    if (currentSetPinIdx < pinInputSize) {
        pinPos = sizeof(t_boardSettings) + sizeof(t_pinInputSettings) * currentSetPinIdx;
    } else {
        pinPos = sizeof(t_boardSettings) + (sizeof(t_pinInputSettings) * pinInputSize) + sizeof(t_pinOutputSettings) * (currentSetPinIdx - pinInputSize);
    }
    eeprom_write_block(buf, (uint8_t *)(pinPos + offsetof(t_pinInputSettings, name)), len);
    eeprom_write_byte((uint8_t *) (pinPos + offsetof(t_pinInputSettings, name) + len), 0);
    return 0;
}
const prog_char *setConfigPinDescription(char *buf, uint16_t len, uint8_t index) {
    const char *desc = currentSetPinIdx < pinInputSize ? pinInputDescription[currentSetPinIdx].description :
            pinOutputDescription[currentSetPinIdx - pinInputSize].description;
    if (!strncmp_P(buf, desc, len)) {
        return 0;
    }
    return DESCRIPTION_CANNOT_BE_SET;
}
const prog_char *setConfigPinDirection(char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, currentSetPinIdx < pinInputSize ? STR_INPUT : STR_OUTPUT, len)) {
        return 0;
    }
    return PSTR("direction cannot be set");
}
const prog_char *setConfigPinType(char *buf, uint16_t len, uint8_t index) {
    uint8_t type = pgm_read_byte(currentSetPinIdx < pinInputSize ? &pinInputDescription[currentSetPinIdx].type :
            &pinOutputDescription[currentSetPinIdx - pinInputSize].type);
    if (!strncmp_P(buf, (const prog_char *)pgm_read_byte(&pinType[type - 1]), len)) {
        return 0;
    }
    return PSTR("type cannot be set");
}


const prog_char *setConfigPinValueMin(char *buf, uint16_t len, uint8_t index) {
    float value = atof(buf);
    if (currentSetPinIdx < pinInputSize) {
        PinInputConversion conversion = (PinInputConversion) pgm_read_word(&(pinInputDescription[currentSetPinIdx].convertValue));
        if (RelDif(value, conversion(0)) >= 0.001) {
            return CANNOT_SET_MIN_VAL;
        }
    } else {
        float minValue;
        memcpy_P(&minValue, &pinOutputDescription[currentSetPinIdx - pinInputSize].valueMin, sizeof(float));
        if (RelDif(value, minValue) >= 0.001) {
            return CANNOT_SET_MIN_VAL;
        }
    }
    return 0;
}
const prog_char *setConfigPinValueMax(char *buf, uint16_t len, uint8_t index) {
    float value = atof(buf);
    if (currentSetPinIdx < pinInputSize) {
        PinInputConversion conversion = (PinInputConversion) pgm_read_word(&(pinInputDescription[currentSetPinIdx].convertValue));
        if (RelDif(value, conversion(1023)) >= 0.001) {
            return CANNOT_SET_MAX_VAL;
        }
    } else {
        float minValue;
        memcpy_P(&minValue, &pinOutputDescription[currentSetPinIdx - pinInputSize].valueMax, sizeof(float));
        if (RelDif(value, minValue) >= 0.001) {
            return CANNOT_SET_MAX_VAL;
        }
    }
    return 0;
}

const prog_char *setConfigPinNotifyCond(char *buf, uint16_t len, uint8_t index) {
    return PSTR("notifyCondition");
}
const prog_char *setConfigPinNotifyValue(char *buf, uint16_t len, uint8_t index) {
    return PSTR("notifyValue");
}
const prog_char *setConfigPinValue(char *buf, uint16_t len, uint8_t index) {
    return PSTR("value");
}
