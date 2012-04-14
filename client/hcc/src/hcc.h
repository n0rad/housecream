
#ifndef HCC_H
#define HCC_H

#include "../config.h"
#include "../lib/arduino/Arduino.h" // arduino specific
#include "debug.h"
#include "config-manager.h"
#include "driver/network.h"
#include "driver/board.h"


int availableMemory(void);


void hccInit(void);
void hccSetup(void);
void hccLoop(void);

#endif
