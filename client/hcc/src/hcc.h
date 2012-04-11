
#ifndef HCC_H
#define HCC_H

#include "../config.h"
#include "WProgram.h" // arduino specific
#include "debug.h"
#include "eeprom-config.h"
#include "net-server/net-server.h"

void hccInit(void);
void hccSetup(void);
void hccLoop(void);

#endif
