#include "settings-pin.h"

uint16_t getConfigPinValue(uint8_t pinId) {
    uint16_t pre = CONFIG_EEPROM_START + sizeof(t_boardInfo) + sizeof(t_boardData) + (sizeof(t_pinInfo) * NUMBER_OF_PINS);
    DEBUG_PRINTLN(pre);
    DEBUG_PRINTLN((sizeof(t_pinData) * pinId));
    return eeprom_read_word((const uint16_t *) pre + (sizeof(t_pinData) * pinId));
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
