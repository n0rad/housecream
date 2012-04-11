#include "hcc.h"
#include "eeprom-config.h"

void hccInit(void) {
    init(); // load init of arduino

#ifdef DEBUG
    delay(3000);
    Serial.begin(9600);
#endif

    DEBUG_PRINT_FULL("Starting init");
    configLoad();

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
    delay(1000);
}
