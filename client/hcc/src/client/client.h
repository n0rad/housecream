#ifndef CLIENT_H
#define CLIENT_H

#include "../hcc.h"
#include "../settings/settings-config.h"

void clientNotify(int pinId, float oldValue, float value, t_notify notify);
uint16_t generateRequest(char *buf);

#endif
