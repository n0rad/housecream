#ifndef EEPROM_CONFIG_H
#define EEPROM_CONFIG_H

#include <stdint.h>
#include <avr/pgmspace.h>
#include <avr/eeprom.h>

#include "../hcc.h"
#include "../pin/pin-manager.h"
#include "settings-config.h"

/** description stay in program memory and cannot change */
/** infos are stored in eeprom and can change */
/** data are stored in eeprom and keep in ram for fast access */


void settingsLoad();
void settingsReload();
void settingsSave();

void getConfigIP(uint8_t ip[4]);
void getConfigMac(uint8_t ip[6]);
uint16_t getConfigPort();
uint8_t *getConfigBoardName_E();
char* getConfigNotifyUrl();


const prog_char PIN_STRING_INPUT[] PROGMEM = "INPUT";
const prog_char PIN_STRING_OUTPUT[] PROGMEM = "OUTPUT";
const prog_char PIN_STRING_NOTUSED[] PROGMEM = "NOTUSED";
const prog_char PIN_STRING_RESERVED[] PROGMEM = "RESERVED";

const prog_char PIN_TYPE_ANALOG[] PROGMEM = "ANALOG";
const prog_char PIN_TYPE_DIGITAL[] PROGMEM = "DIGITAL";

const prog_char PIN_NOTIFICATION_SUP[] PROGMEM = "SUP_OR_EQUAL";
const prog_char PIN_NOTIFICATION_INF[] PROGMEM = "INF_OR_EQUAL";


extern const char *pinDirection[];
extern const char *pinType[];
extern const char *pinNotification[];

#define CONFIG_EEPROM_START 0

#endif
