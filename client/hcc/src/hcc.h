#ifndef HCC_H
#define HCC_H

#if 1
#define PROGMEM2 __attribute__(( section(".progmem.data") ))
#define PSTR2(s) (__extension__({static prog_char __c[] PROGMEM2 = (s); &__c[0];}))
#endif

//#define DEBUG

#include "debug.h"

extern char *criticalProblem_p;
extern char *definitionError;

int getFreeMemory();

#endif
