#ifndef CONFIG_MANAGER_H
#define CONFIG_MANAGER_H

#include <avr/pgmspace.h>

const int8_t configGetInputPinIdx(uint8_t pinIdToFind);
const int8_t configGetOutputPinIdx(uint8_t pinIdToFind);

char *buildGlobalError_P(const prog_char *progmem_s, int pin);

char *configCheck();



#endif
