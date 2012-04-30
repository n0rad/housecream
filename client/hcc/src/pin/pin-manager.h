#ifndef PIN_MANAGER_H_
#define PIN_MANAGER_H_

#include <stdint.h>

uint16_t defaultPinRead(uint8_t pinId);
void defaultPinWrite(uint8_t pinId, uint16_t value);

typedef float (*PinInputConversion)(uint16_t pinValue);
typedef int16_t (*PinOutputConversion)(float pinValue);

typedef uint16_t (*PinRead)(uint8_t pinId);
typedef void (*PinWrite)(uint8_t pinId, uint16_t value);

float noInputConversion(uint16_t pinValue);
int16_t noOutputConversion(float pinValue);


#include "../config/config.h"

float getPinValue(uint8_t pinId);
void pinInit();
void pinCheckInit();
void pinCheckChange();

#endif
