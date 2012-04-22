#ifndef HCC_H
#define HCC_H

#define DEBUG

#include "debug.h"

extern char *criticalProblem_p;
extern char *definitionError;

int getFreeMemory();

void hccInit(void);
void hccSetup(void);
void hccLoop(void);

#endif
