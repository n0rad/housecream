#ifndef HCC_H
#define HCC_H

#define HARDWARE "Arduino Duemilanove / Nuelectronics enc28j60 Ethernet Shield V1.1"


#define DEBUG

#include "debug.h"


const prog_char PUT[] PROGMEM = "PUT ";
const prog_char JSON_STR_END[] PROGMEM = "\"}";

typedef struct s_webRequest {
    const struct s_resource *resource;
    uint8_t pinIdx;
} t_webRequest;


extern char *criticalProblem_p;
extern char *definitionError;
extern uint8_t needReboot;

int getFreeMemory();

#endif
