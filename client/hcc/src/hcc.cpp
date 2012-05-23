#include "hcc.h"
#include "../lib/arduino/Arduino.h" // for serial
#include "driver/network.h"
#include "settings/settings.h"
#include "client/client.h"
#include "config/config-manager.h"


char *definitionError;
char *criticalProblem_p;
uint8_t needReboot = false;
t_notification *notification = 0;

void DEBUG_P(const prog_char *progmem) {
    char c;
    while ((c = pgm_read_byte(progmem++))) {
        DEBUG_PRINT(c);
    }
    DEBUG_PRINTLN();
}

void DEBUG_E(const char *eeprom) {
    char c;
    while ((c = eeprom_read_byte((uint8_t *)eeprom++))) {
        DEBUG_PRINT(c);
    }
    DEBUG_PRINTLN();
}

int main(void) {
    init(); // load init of arduino

#ifdef DEBUG
    delay(3000);
    Serial.begin(9600);
#endif

    definitionError = configCheck();
    settingsLoad();
    networkSetup();
    if (!definitionError) {
        pinInit();
    }

    clientBoardNotify(BOARD_NOTIFY_BOOT);

    while (1) {
        networkManage();
        if (!definitionError) {
            pinCheckChange();
        }
    }

    return 0;
}
