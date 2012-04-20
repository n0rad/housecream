#ifndef EEPROM_CONFIG_H
#define EEPROM_CONFIG_H

#include <stdint.h>
#include <avr/pgmspace.h>
#include <avr/eeprom.h>

#include "../pin/pin-manager.h"
#include "settings-config.h"


/** description stay in program memory and cannot change */
/** infos are stored in eeprom and can change */
/** data are stored in eeprom and keep in ram for fast access */


void settingsLoad(void);
void settingsReload(void);

void getConfigIP(uint8_t ip[4]);
void getConfigMac(uint8_t ip[6]);
uint16_t getConfigPort();
uint8_t *getConfigBoardName_e();
char* getConfigNotifyUrl(void);



extern const char *pinDirection[];
extern const char *pinType[];

#define CONFIG_EEPROM_START 0

#endif
