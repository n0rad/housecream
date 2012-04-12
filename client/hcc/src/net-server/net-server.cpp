#include "net-server.h"
#include "../hcc.h"
#include <avr/pgmspace.h>

void netSetup(void) {
    uint16_t c = config.boardInfo.port;
    DEBUG_START();
    DEBUG_PRINT("Starting net server with on port : ");
    DEBUG_PRINTDEC(c);
    DEBUG_PRINTLN("");
}
