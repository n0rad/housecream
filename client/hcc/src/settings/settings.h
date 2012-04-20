#ifndef EEPROM_CONFIG_H
#define EEPROM_CONFIG_H

#include <stdint.h>
#include <avr/pgmspace.h>
#include <avr/eeprom.h>

#include "../hcc.h"
#include "../pin/pin-manager.h"
#include "settings-config.h"


const prog_char PIN_STRING_INPUT[] PROGMEM = "INPUT";
const prog_char PIN_STRING_OUTPUT[] PROGMEM = "OUTPUT";
const prog_char PIN_STRING_NOTUSED[] PROGMEM = "NOTUSED";
const prog_char PIN_STRING_RESERVED[] PROGMEM = "RESERVED";

const prog_char PIN_TYPE_ANALOG[] PROGMEM = "ANALOG";
const prog_char PIN_TYPE_DIGITAL[] PROGMEM = "DIGITAL";

/** description stay in program memory and cannot change */
/** infos are stored in eeprom and can change */
/** data are stored in eeprom and keep in ram for fast access */


void settingsLoad(void);
void settingsReload(void);

void getConfigIP(uint8_t ip[4]);
void getConfigMac(uint8_t ip[6]);
uint16_t getConfigPort();
uint8_t *getConfigBoardName_E();
char* getConfigNotifyUrl(void);



extern const char *pinDirection[];
extern const char *pinType[];

#define CONFIG_EEPROM_START 0

#endif
