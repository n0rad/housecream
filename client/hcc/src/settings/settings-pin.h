#ifndef SETTINGS_PIN_H
#define SETTINGS_PIN_H

#include <stdint.h>

#include "settings-config.h"

uint16_t getConfigPinValue(uint8_t pinId);
t_notify *getConfigPinNotify(uint8_t pinId);

#endif
