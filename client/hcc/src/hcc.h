
#ifndef HCC_H
#define HCC_H

#include "../config.h"
#include "../lib/arduino/Arduino.h" // arduino specific
//#include "../lib/arduino/WString.h"
#include "debug.h"
#include "eeprom-config.h"
#include "driver/network.h"

#define F(string_literal) (reinterpret_cast<__FlashStringHelper *>(PSTR(string_literal)))

void hccInit(void);
void hccSetup(void);
void hccLoop(void);

#endif
