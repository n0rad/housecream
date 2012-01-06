#include "ADC.h"
#include <avr/io.h> 
#include <string.h>
#include <stdint.h>
#include "../hw-layout.h"


// Functions to access the internal ADC

// internal ADC reference configuration
static uint8_t adcReference=(1<<REFS0); // = VCC 5V

// get a sample from ADC chip.
// chanell must be in range 0-7.
uint16_t getADC(int channel) {
    ADMUX = adcReference + channel;
    // start one sample
    ADCSRA |= (1<<ADSC);
    // wait for the result
    loop_until_bit_is_clear(ADCSRA, ADSC);
    return ADC;
}

// initialize the ADC 
void initADC() {
    ADCSRA |= (1<<ADEN) | (1<<ADPS2) | (1<<ADPS1) | (1<<ADPS0);
}

// select reference
// value can be "VCC", "1.1V or "2.56V"
// returns 1 on success
uint8_t setAREF(char* name) {
    if (strcmp(name,"VCC")==0)
        adcReference=(1<<REFS0);     
    else if (strcmp(name,"1.1V")==0)
        adcReference=(1<<REFS1);
    else if (strcmp(name,"2.56V")==0)
        adcReference=((1<<REFS1) | (1<<REFS0));
    else if (strcmp(name,"EXT")==0)     
        adcReference=0;                        
    else 
        return 0;
    // Perform one sample to initialize the ADC on the new reference
    ADMUX = adcReference;
    ADCSRA |= (1<<ADSC);
    loop_until_bit_is_clear(ADCSRA, ADSC);
    (ADC);
    return 1;
}

#ifdef EXT_AREF
// get current current voltage
// This assumes that VCC is 5.0 Volt.
float getAREF(void) {
    uint8_t aref=ADMUX & ((1<<REFS1) | (1<<REFS0));
    if (aref == 0)
        return EXT_AREF/1024;        
    else if (aref == (1<<REFS0))
        return 5.00/1024;        
    else if (aref == (1<<REFS1))
        return 1.10/1024;
    else
        return 2.56/1024;
}
#endif
