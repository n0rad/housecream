#include "hcc.h"

void hccInit(void) {
    init(); // load init of arduino

    DEBUG_PRINT_FULL("Starting init");
    configLoad();
}

void hccSetup(void) {
    DEBUG_PRINT_FULL("Starting setup");
}

void hccLoop(void) {

}
