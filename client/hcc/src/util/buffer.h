#ifndef BUFFER_H
#define BUFFER_H

#include <avr/pgmspace.h>
#include <avr/eeprom.h>
#include <stdio.h>

#include "../hcc.h"

#define TCP_CHECKSUM_L_P    0x33 // TODO move to driver

const prog_char sprintfpHEX[] PROGMEM = "%02X";
const prog_char sprintfpDEC[] PROGMEM = "%d";

uint16_t addToBufferTCP(char *buf, uint16_t pos, float val);
uint16_t addToBufferTCPHex(char *buf, uint16_t pos, uint16_t val);
uint16_t addToBufferTCP(char *buf, uint16_t pos, uint16_t val);
uint16_t addToBufferTCP(char *buf, uint16_t pos, char val);
uint16_t addToBufferTCP(char *buf, uint16_t pos, char *mem_s);
uint16_t addToBufferTCP_P(char *buf, uint16_t pos, const prog_char *progmem_s);
uint16_t addToBufferTCP_E(char *buf, uint16_t pos, uint8_t *eeprom_s);

#endif
