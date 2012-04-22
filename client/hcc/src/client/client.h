#ifndef CLIENT_H
#define CLIENT_H

#include "../hcc.h"
#include "../settings/settings-config.h"
#include "../util/buffer.h"

const prog_char PUT2[] PROGMEM = "PUT ";

void clientNotify(int pinId, float oldValue, float value, t_notify notify);
uint16_t clientBuildNextQuery(char *buf);

typedef struct s_notification {
    uint8_t pinId;
    float oldValue;
    float value;
    t_notify notify;
} t_notification;

extern t_notification *notification;

//#include "../server/server.h"

#endif
