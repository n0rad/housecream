#ifndef BUFFER_H
#define BUFFER_H

#include <avr/pgmspace.h>
#include <avr/eeprom.h>
#include <stdio.h>

#define TCP_CHECKSUM_L_P    0x33 // TODO move to driver

const prog_char sprintfpHEX[] PROGMEM = "%02X";
const prog_char sprintfpDEC[] PROGMEM = "%d";

#endif
