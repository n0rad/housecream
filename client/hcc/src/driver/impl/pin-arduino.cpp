#include "../../hcc.h"

uint16_t pinReadValue(uint8_t pin, uint8_t type) {
    if (type == PIN_ANALOG) {
        return analogRead(pin);
    } else {
        return digitalRead(pin);
    }
}
