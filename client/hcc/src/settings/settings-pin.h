#ifndef SETTINGS_PIN_H
#define SETTINGS_PIN_H

#include <stdint.h>

#include "settings-config.h"
#include "settings.h"

uint16_t getConfigPinValue(uint8_t pinId);
void getConfigPinNotify(uint8_t pinId, uint8_t notifyId, t_notify *notify);
uint8_t *getConfigPinName_E(uint8_t pinId);

#endif
