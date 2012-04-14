#include "hcc.h"

char *criticalProblem_p;

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
    networkManage();
}
