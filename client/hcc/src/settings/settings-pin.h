#ifndef SETTINGS_PIN_H
#define SETTINGS_PIN_H

#include <stdint.h>

#include "../config.h"
#include "settings.h"

uint16_t getConfigPinValue(uint8_t pinId);
void getConfigPinNotify(uint8_t pinIdx, uint8_t notifyId, t_notify *notify);
uint8_t *getConfigPinName_E(uint8_t pinIdx, uint8_t pinDirection);

const prog_char *setConfigPinId(char *buf, uint16_t len, uint8_t index);
const prog_char *setConfigPinName(char *buf, uint16_t len, uint8_t index);
const prog_char *setConfigPinDescription(char *buf, uint16_t len, uint8_t index);
const prog_char *setConfigPinDirection(char *buf, uint16_t len, uint8_t index);
const prog_char *setConfigPinType(char *buf, uint16_t len, uint8_t index);
const prog_char *setConfigPinValueMin(char *buf, uint16_t len, uint8_t index);
const prog_char *setConfigPinValueMax(char *buf, uint16_t len, uint8_t index);
const prog_char *setConfigPinNotifies(char *buf, uint16_t len, uint8_t index);
const prog_char *setConfigPinNotifyCond(char *buf, uint16_t len, uint8_t index);
const prog_char *setConfigPinNotifyValue(char *buf, uint16_t len, uint8_t index);
const prog_char *setConfigPinValue(char *buf, uint16_t len, uint8_t index);
const prog_char *setConfigPinStartValue(char *buf, uint16_t len, uint8_t index);

#endif
