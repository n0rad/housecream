#ifndef _ADC_H__
#define _ADC_H__
#include <stdint.h>

// Functions to access the ADC

// We have two implementations:
// MCP3204.c and internal_ADC.c


// get a sample from ADC chip.
uint16_t getADC(int channel);

// initialize the ADC chip
void initADC(void);

// select reference
// returns 1 on success
uint8_t setAREF(char* name);

#ifdef EXT_AREF
    // get current reference voltage
    float getAREF(void);
#endif


#endif // _ADC_H__
