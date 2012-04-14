#include "hcc.h"

__FlashStringHelper *criticalProblem;

//#define BUFFER_SIZE 1000
//uint8_t buf[BUFFER_SIZE + 1];


void hccInit(void) {
    init(); // load init of arduino

#ifdef DEBUG
    delay(3000);
    Serial.begin(9600);
#endif
}

void hccSetup(void) {
    if (checkBoardRequirement()) {
        return;
    }
    configLoad();
    networkSetup();
}

void hccLoop(void) {
    if (criticalProblem) {
        //TODO read default conf to start a HTTP server and send an error on all calls
        DEBUG_PRINT_FULL(criticalProblem);
        delay(1000);
        return;
    }
//    DEBUG_PRINT(F("Available memory : "));
//    DEBUG_PRINTLN(availableMemory());

    networkManage();
}
