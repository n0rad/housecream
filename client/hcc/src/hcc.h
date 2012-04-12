
#ifndef HCC_H
#define HCC_H

#include "../config.h"
#include "WProgram.h" // arduino specific
//#include "../lib/arduino/WString.h"
#include "debug.h"
#include "eeprom-config.h"
#include "driver/network.h"


void hccInit(void);
void hccSetup(void);
void hccLoop(void);

#endif
