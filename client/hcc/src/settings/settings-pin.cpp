#include "settings-pin.h"

uint16_t getConfigPinValue(uint8_t pinId) {
    uint16_t pinData = CONFIG_EEPROM_START + sizeof(t_boardInfo) + sizeof(t_boardData) + (sizeof(t_pinInfo) * NUMBER_OF_PINS);
    return eeprom_read_word((const uint16_t *)(pinData + (sizeof(t_pinData) * pinId)));
}

void getConfigPinNotify(uint8_t pinId, uint8_t notifyId, t_notify *notify) {
    uint16_t pinData = CONFIG_EEPROM_START + sizeof(t_boardInfo) + sizeof(t_boardData) + (sizeof(t_pinInfo) * NUMBER_OF_PINS);
    uint16_t sizePinId = (sizeof(t_pinData) * pinId);
    uint16_t sizeLastvalue = sizeof(uint16_t);
    eeprom_read_block((void *)notify, (char *)pinData + sizePinId + sizeLastvalue + (sizeof(t_notify) * notifyId), sizeof(t_notify));
}

uint8_t *getConfigPinName_E(uint8_t pinId) {
    return (uint8_t *)CONFIG_EEPROM_START + sizeof(t_boardInfo) + sizeof(t_boardData) + (sizeof(t_pinInfo) * pinId);
}


/////////////////

prog_char *setConfigPinId(char *buf, uint16_t len, uint8_t index) {
    return PSTR("id cannot be set");
}
prog_char *setConfigPinName(char *buf, uint16_t len, uint8_t index) {
    return PSTR("name cannot be set");
}
prog_char *setConfigPinDescription(char *buf, uint16_t len, uint8_t index) {
    return PSTR("description cannot be set");
}
prog_char *setConfigPinDirection(char *buf, uint16_t len, uint8_t index) {
    return PSTR("direction cannot be set");
}
prog_char *setConfigPinType(char *buf, uint16_t len, uint8_t index) {
    return PSTR("type cannot be set");
}
prog_char *setConfigPinValueMin(char *buf, uint16_t len, uint8_t index) {
    return PSTR("valueMin cannot be set");
}
prog_char *setConfigPinValueMax(char *buf, uint16_t len, uint8_t index) {
    return PSTR("valueMax cannot be set");
}
prog_char *setConfigPinNotifies(char *buf, uint16_t len, uint8_t index) {
    return PSTR("notifies cannot be set");
}
prog_char *setConfigPinNotifyCond(char *buf, uint16_t len, uint8_t index) {
    return PSTR("notifyCondition cannot be set");
}
prog_char *setConfigPinNotifyValue(char *buf, uint16_t len, uint8_t index) {
    return PSTR("notifyValue cannot be set");
}
prog_char *setConfigPinValue(char *buf, uint16_t len, uint8_t index) {
    return PSTR("value cannot be set");
}
prog_char *setConfigPinStartValue(char *buf, uint16_t len, uint8_t index) {
    return PSTR("startValue cannot be set");
}
