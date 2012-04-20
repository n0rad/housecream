#ifndef HCC_H
#define HCC_H

#include "debug.h"

#define DEBUG

extern char *criticalProblem_p;
extern char *definitionError;

int availableMemory(void);

void hccInit(void);
void hccSetup(void);
void hccLoop(void);

#endif
