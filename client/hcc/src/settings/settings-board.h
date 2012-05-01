#ifndef SETTINGS_BOARD_H
#define SETTINGS_BOARD_H

#include <avr/pgmspace.h>
#include <avr/eeprom.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "../util/buffer.h"
#include "settings.h"
#include "../util/mylibc.h"

const char hcc_version[] PROGMEM = "0.1";

const char NOT_VALID_IP[] PROGMEM = "not valid ip";
const char NOT_VALID_PORT[] PROGMEM = "not valid port";
const char CANNOT_SET_MAC[] PROGMEM = "mac cannot be set";


void configBoardGetMac(uint8_t ip[6]);


void settingsBoardGetIP(uint8_t ip[4]);
uint16_t settingsBoardGetPort();
uint8_t *settingsBoardGetName_E();
uint8_t *settingsBoardGetNotifyUrl_E();

const prog_char *configBoardHandlePinIdsArray(uint8_t index);
const prog_char *configBoardSetName(char *buf, uint16_t len, uint8_t index);
const prog_char *configBoardSetNotifyUrl(char *buf, uint16_t len, uint8_t index);
const prog_char *configBoardSetIP(char *buf, uint16_t len, uint8_t index);
const prog_char *configBoardSetPort(char *buf, uint16_t len, uint8_t index);

const prog_char *configBoardSetPinIds(char *buf, uint16_t len, uint8_t index);
const prog_char *configBoardSetMac(char *buf, uint16_t len, uint8_t index);
const prog_char *configBoardSetDescription(char *buf, uint16_t len, uint8_t index);
const prog_char *configBoardSetVersion(char *buf, uint16_t len, uint8_t index);
const prog_char *configBoardSetHardware(char *buf, uint16_t len, uint8_t index);
const prog_char *configBoardSetSoftware(char *buf, uint16_t len, uint8_t index);

#endif
