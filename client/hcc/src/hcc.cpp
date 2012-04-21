#include "hcc.h"
#include "../lib/arduino/Arduino.h" // for serial
#include "network.h"
#include "settings/settings.h"

char *definitionError;
char *criticalProblem_p;
uint8_t needReboot = false;
uint8_t clientDataReady = false;

void DEBUG_p(const prog_char *progmem_s) {
    char c;
    while ((c = pgm_read_byte(progmem_s++))) {
        DEBUG_PRINT(c);
    }
    DEBUG_PRINTLN(" ");
}

void hccInit(void) {
    init(); // load init of arduino

#ifdef DEBUG
    delay(3000);
    Serial.begin(9600);
#endif
}

void hccSetup(void) {
    definitionError = checkConfig();
    settingsLoad();
    networkSetup();
    pinInit();
    pinCheckInit();
}

void hccLoop(void) {
    networkManage();
    pinCheckChange();
    networkManage2();
}
