#include "hcc.h"
#include "../lib/arduino/wiring.h"
#include "eeprom-config.h"
#include "config-def.h"

extern "C" void __cxa_pure_virtual()
{
#ifdef __AVR__
    asm("cli");
#endif
    while (1)
    ;
}

void hccInit(void) {
    init(); // load init of arduino

//#ifdef DEBUG
    Serial.begin(9600);
//#endif

    DEBUG_PRINT_FULL("Starting init");
 //   configLoad();

//    net
}

void hccSetup(void) {
    DEBUG_PRINT_FULL("Starting setup");
}

int count = 0;

void hccLoop(void) {
    Serial.print("temp: ");
    Serial.println(count);
    count++;
    delay (1000);
}
