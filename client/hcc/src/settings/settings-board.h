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


prog_char *setConfigBoardName(char* PGMkey, char *buf, uint16_t len, uint8_t index);
prog_char *setConfigBoardNotifyUrl(char* PGMkey, char *buf, uint16_t len, uint8_t index);
prog_char *setConfigBoardIP(char* PGMkey, char *buf, uint16_t len, uint8_t index);
prog_char *setConfigBoardPort(char* PGMkey, char *buf, uint16_t len, uint8_t index);

prog_char *setConfigBoardNumberOfPin(char* PGMkey, char *buf, uint16_t len, uint8_t index);
prog_char *setConfigBoardMac(char* PGMkey, char *buf, uint16_t len, uint8_t index);
prog_char *setConfigBoardDescription(char* PGMkey, char *buf, uint16_t len, uint8_t index);
prog_char *setConfigBoardVersion(char* PGMkey, char *buf, uint16_t len, uint8_t index);
prog_char *setConfigBoardHardware(char* PGMkey, char *buf, uint16_t len, uint8_t index);
prog_char *setConfigBoardSoftware(char* PGMkey, char *buf, uint16_t len, uint8_t index);


#endif
