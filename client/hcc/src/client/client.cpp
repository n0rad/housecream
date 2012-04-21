#include "client.h"

void clientNotify(int pinId, float oldValue, float value, t_notify notify) {
    DEBUG_PRINT("notify for pin: ");
    DEBUG_PRINT(pinId);
    DEBUG_PRINT(" old: ");
    DEBUG_PRINT(oldValue);
    DEBUG_PRINT(" new: ");
    DEBUG_PRINT(value);
    DEBUG_PRINT(" with cond:  ");
    DEBUG_PRINT(notify.condition);
    DEBUG_PRINT(" ");
    DEBUG_PRINTLN(notify.value);
}
