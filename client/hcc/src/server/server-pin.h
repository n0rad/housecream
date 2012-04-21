#ifndef SERVER_PIN_H
#define SERVER_PIN_H

#include <avr/pgmspace.h>
#include <stdio.h>

#include "../hcc.h"
#include "../settings/settings.h"

uint16_t pinGet(char *buf, uint16_t dat_p, uint16_t plen);
uint16_t pinPut(char *buf, uint16_t dat_p, uint16_t plen);

#include "server.h"

#endif
