#include "hcc.h"
#include "../lib/arduino/Arduino.h" // for serial
#include "network.h"
#include "settings/settings.h"
#include "client/client.h"

char *definitionError;
char *criticalProblem_p;
uint8_t needReboot = false;
t_notification *notification = 0;

void DEBUG_p(const prog_char *progmem_s) {
    char c;
    while ((c = pgm_read_byte(progmem_s++))) {
        DEBUG_PRINT(c);
    }
    DEBUG_PRINTLN(" ");
}


int main(void) {
    init(); // load init of arduino

#ifdef DEBUG
    delay(3000);
    Serial.begin(9600);
#endif

    definitionError = checkConfig();
    settingsLoad();
    networkSetup();
    pinInit();
    pinCheckInit();

    for (;;) {
        networkManageServer();
        pinCheckChange();
        networkManageClient();
    }

    return 0;
}
