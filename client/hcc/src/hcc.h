#ifndef HCC_H
#define HCC_H

#include "../config.h"
#include "../lib/arduino/Arduino.h" // arduino specific
#include "debug.h"

#include "driver/network.h"
#include "driver/board.h"
#include "driver/pin.h"

#include "config-manager.h"
#include "pin/pin-manager.h"

int my_strpos(const char *s, int ch);



void DEBUG_p(const prog_char *progmem_s);

//extern s_boardData boardData;
extern s_pinData pinData;
extern char *criticalProblem_p;
extern char *definitionError;



int availableMemory(void);


void hccInit(void);
void hccSetup(void);
void hccLoop(void);

#endif
