#ifndef PIN_MANAGER_H_
#define PIN_MANAGER_H_

#include <stdint.h>

typedef float (*PinValueConversion)(float pinValue);
typedef uint16_t (*PinRead)(uint8_t pinId);
typedef void (*PinWrite)(uint8_t pinId, uint16_t value);


#include "../settings/settings.h"

float noConversion(float pinValue);
uint16_t defaultPinRead(uint8_t pinId);
void defaultPinWrite(uint8_t pinId, uint16_t value);




void pinCheckChange();

#endif