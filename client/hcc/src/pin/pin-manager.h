#ifndef PIN_MANAGER_H_
#define PIN_MANAGER_H_

#include <stdint.h>


typedef float (*PinValueConversion)(float pinValue);
typedef uint16_t (*PinRead)(uint8_t pinId);
typedef void (*PinWrite)(uint8_t pinId, uint16_t value);


#include "../hcc.h"
#include "../settings/settings-pin.h"
#include "../pin.h"
#include "../client/client.h"

float noConversion(float pinValue);
uint16_t defaultPinRead(uint8_t pinId);
void defaultPinWrite(uint8_t pinId, uint16_t value);
float getPinValue(uint8_t pinId);
void pinInit();
void pinCheckInit();
void pinCheckChange();

#endif
