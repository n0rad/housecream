#ifndef SETTINGS_BOARD_H
#define SETTINGS_BOARD_H

#include <avr/pgmspace.h>
#include <avr/eeprom.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "settings.h"
#include "../util/mylibc.h"

const char hcc_version[] PROGMEM = "0.1";


char *setConfigBoardName(char* PGMkey, char *buf, uint16_t len, uint8_t index);
char *setConfigBoardNotifyUrl(char* PGMkey, char *buf, uint16_t len, uint8_t index);
char *setConfigBoardIP(char* PGMkey, char *buf, uint16_t len, uint8_t index);
char *setConfigBoardPort(char* PGMkey, char *buf, uint16_t len, uint8_t index);

char *handleUnsetableNumberOfPin(char* PGMkey, char *buf, uint16_t len, uint8_t index);
char *handleUnsetableMac(char* PGMkey, char *buf, uint16_t len, uint8_t index);
char *handleUnsetableDescription(char* PGMkey, char *buf, uint16_t len, uint8_t index);
char *handleUnsetableVersion(char* PGMkey, char *buf, uint16_t len, uint8_t index);
char *handleUnsetableHardware(char* PGMkey, char *buf, uint16_t len, uint8_t index);
char *handleUnsetableSoftware(char* PGMkey, char *buf, uint16_t len, uint8_t index);


#endif
