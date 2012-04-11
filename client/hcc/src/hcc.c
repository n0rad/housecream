#include "hcc.h"
#include "eeprom-config.h"

void init(void) {
    DEBUG_PRINT_FULL("Starting init");
    configLoad();
}

void setup(void) {
    DEBUG_PRINT_FULL("Starting setup");
}

void loop(void) {

}
