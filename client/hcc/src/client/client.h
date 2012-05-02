#ifndef CLIENT_H
#define CLIENT_H

#include "../hcc.h"
#include "../config/config.h"
#include "../util/buffer.h"
#include "../settings/settings.h"


#define BOARD_NOTIFY_BOOT 0
#define BOARD_NOTIFY_NOTIF 1


void clientBoardNotify(uint8_t notifType);
void clientPinNotify(int pinId, float oldValue, float value, t_notify *notify);
uint16_t clientBuildNextQuery(char *buf);

typedef struct s_notification {
    uint8_t isBoardNotif;
    uint8_t boardNotifType;
    uint8_t pinId;
    float oldValue;
    float value;
    t_notify notify;
    struct s_notification *next;
} t_notification;

extern t_notification *notification;

#endif
