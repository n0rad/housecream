#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <ctype.h>
#include <util/delay.h> 
#include <avr/io.h>
#include <avr/pgmspace.h>
#include "io-commands.h"
#include "driver/serialconsole.h"
#include "hw-layout.h"


#if ADC_CHANNELS > 0
    #include "driver/ADC.h"
#endif

char buffer[100];

// Help
void help() {
    puts_P(PSTR("Butterfly USB I/O Firmware v1.1.3\n"));
    
    puts_P(PSTR("Commands that affect all port pins:"));
    puts_P(PSTR("d,DDCCBBAA  Set direction of all port pins, high=output, low=input"));
    puts_P(PSTR("p,DDCCBBAA  Configure pull-up resistors, high=on, low=off"));
    puts_P(PSTR("o,DDCCBBAA  Output data on all port pins"));
    puts_P(PSTR("i           Read from all port pins\n"));

    puts_P(PSTR("Commands that affect one port:"));
    puts_P(PSTR("dPB,xx      Set direction of port B"));
    puts_P(PSTR("pPB,xx      Configure pull-up resistors of port B"));
    puts_P(PSTR("oPB,xx      Output data on port B"));
    puts_P(PSTR("iPB         Read from port B\n"));

    puts_P(PSTR("Commands that affect a single port pin:"));
    puts_P(PSTR("dPB0,i      Set port B pin 0 to output direction (0 or i=input, 1 or o=output)"));
    puts_P(PSTR("pPB0,e      Enable pull-up's of port B pin 0 (0 or d=disabled, 1 or e=enabled)"));
    puts_P(PSTR("oPB0,h      Output high on port B pin 0 (0 or l=low, 1 or h=high)")); 
    puts_P(PSTR("iPB0        Read from port B pin 0\n"));
    
    puts_P(PSTR("Analog inputs:")); 
    puts_P(PSTR("rVCC        Use VCC as reference (5V)"));
    puts_P(PSTR("r1.1V       Use internal 1,1V reference"));
    puts_P(PSTR("r2.56V      Use internal 2,56V reference"));
    puts_P(PSTR("rEXT        Use external reference"));
    puts_P(PSTR("a3          Read from analog pin 3\n"));

    puts_P(PSTR("xx and DDCCBBAA are hexadecimal numbers."));
#ifdef PORTE
    puts_P(PSTR("Instead of DDCCBBAA, you can also use HHGGFFEEDDCCBBAA"));
#endif
    puts_P(PSTR("Binary and hexadecimal digits can be given in upper and lower case."));
    puts_P(PSTR("Instead of \",\" you can also enter \"=\"."));
}



int main(void) {

    // Flash the status LED two times to indicate that the AVR controller is living
    STATUS_LED_on();
    _delay_ms(20);
    STATUS_LED_off();
    _delay_ms(100);
    STATUS_LED_on();
    _delay_ms(20);
    STATUS_LED_off();

    // Initialize direction and level of I/O pins
    init_io_pins(); 
    
    #if ADC_CHANNELS > 0
        // initialize the ADC
        initADC();
    #endif

    // initialize the serial port
    initserial();
    
    
    // endless main loop
    while (1) {

        // read command from serial port
        fgets(buffer,sizeof(buffer),stdin);

        // Search for backspace characters and
        // delete each as well as the previous character.
        // Also terminate the string at the position of the line-feed character.
        int i=0;
        while (i<sizeof(buffer)) {
            char c=buffer[i];
            if (c=='\b' && i>0) {
                strcpy(buffer+i-1,buffer+i+1);
                i--;
            }
            else if (c=='\n' || c=='\r' || c==0) {
                buffer[i]=0;
                break;
            }
            i++;
        }
        
        // check for h or help command
        if (strcmp_P(buffer,PSTR("h"))==0 || strcmp_P(buffer,PSTR("help"))==0) {
            help();
        }
        else {
            // We use the buffer for input and output.
            io_command(buffer,buffer);
            // remove the trailing line-feed from the buffer to prevent double line-break
            int len=strlen(buffer);
            if (len>0 && buffer[len-1]=='\n')
                buffer[len-1]=0;
            // Output the result of the command
            puts(buffer);
        }
    }
}
