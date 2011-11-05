// Functions to access the ADC chip MCP3204 or MCP3208

#include "ADC.h"
#include <avr/io.h>
#include <string.h>
#include <stdint.h>
#include "../hw-layout.h"

#if F_CPU > 40000000
    #error The ADC driver has been designed for max 20 Mhz. You will need to insert more wait states.
#elif F_CPU > 20000000
    #define wait250ns() { (PINA); (PINA); (PINA); (PINA); (PINA); (PINA); (PINA); (PINA); (PINA); (PINA);}  
#else
    #define wait250ns() { (PINA); (PINA); (PINA); (PINA); (PINA);}
#endif

// Get a sample from ADC chip.
// Channel must be in range 0-3
uint16_t getADC(int channel) {
  
    // comment this out if you have an MCP3208 chip
    if (channel>=ADC_CHANNELS)
        return -1;    
    
    int result=0;
    // save the orignal pin status
    uint8_t saved_ddr=ADC_DDR;
    uint8_t saved_port=ADC_PORT;
    // configure pin i/o direction
    init_adc_pins();    
    // the chip requires that nCS is high and CLK is low before a conversation starts
    nCSADC(1);
    ADC_CLK(0);
    // select ADC chip
    nCSADC(0);                            
    // start bit (=high)  
    ADC_write(1);
    wait250ns();
    ADC_CLK(1); 
    wait250ns();
    ADC_CLK(0); 
    // single input mode (=high)
    ADC_write(1);
    wait250ns();
    ADC_CLK(1); 
    wait250ns();
    ADC_CLK(0); 
    // channel bit 2
    ADC_write(channel & 4);
    wait250ns();
    ADC_CLK(1);
    wait250ns();
    ADC_CLK(0);
    // channel bit 1
    ADC_write(channel & 2);
    wait250ns();
    ADC_CLK(1);
    wait250ns();
    ADC_CLK(0);
    // channel bit 0
    ADC_write(channel & 1);
    wait250ns();
    ADC_CLK(1);
    wait250ns();
    ADC_CLK(0);
    // start sampling (2 clock cycles without data)
    wait250ns();
    ADC_CLK(1);
    wait250ns();
    ADC_CLK(0);
    wait250ns();
    ADC_CLK(1);
    wait250ns();
    ADC_CLK(0);
    // Read the result (12 bits)
    for (int i=0; i<12; i++) {
        wait250ns();
        ADC_CLK(1);     
	wait250ns();
        result=(result<<1) | ADC_read();
        ADC_CLK(0);
    }
    // finish ADC communication (1 clock cycle withou data)
    wait250ns();
    ADC_CLK(1);
    wait250ns();
    ADC_CLK(0);
    // unselect ADC chip
    nCSADC(1);  
    // restore state of portb
    ADC_PORT = saved_port;
    ADC_DDR = saved_ddr;
    return result;
}

// initialize the ADC chip
void initADC(void) {
    // dummy, the chip initializes itself
}

// Hence this chip has no selectable reference source, this is a dummy function.
uint8_t setAREF(char* name) {
    if (strcmp(name,"VCC")==0)
        return 1;
    else if (strcmp(name,"EXT")==0)
        return 1;
    else
        return 0;
}

// get current reference voltage
// This is an estimation only
float getAREF(void) {
  return EXT_AREF/4096;
}

