#include "hcc.h"
#include "../lib/arduino/wiring.h"
#include "eeprom-config.h"

void hccInit(void) {
    init(); // load init of arduino

    DEBUG_PRINT_FULL("Starting init");
    configLoad();

//    net
}

void hccSetup(void) {
    DEBUG_PRINT_FULL("Starting setup");
}

void hccLoop(void) {

}
