#include "hcc.h"

char *definitionError;
char *criticalProblem_p;
boolean needReboot = false;



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
    definitionError = checPinDescriptions();
    configLoad();
    networkSetup();
}

void hccLoop(void) {
    networkManage();
    pinCheckChange();
}
