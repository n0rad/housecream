#ifndef CLIENT_H
#define CLIENT_H

#include "../hcc.h"
#include "../config/config.h"
#include "../util/buffer.h"
#include "../settings/settings.h"

const prog_char PUT2[] PROGMEM = "PUT ";

void clientNotify(int pinId, float oldValue, float value, t_notify notify);
uint16_t clientBuildNextQuery(char *buf);

typedef struct s_notification {
    uint8_t pinId;
    float oldValue;
    float value;
    t_notify notify;
    struct s_notification *next;
} t_notification;

extern t_notification *notification;

#endif
