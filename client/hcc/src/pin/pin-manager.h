#ifndef PIN_MANAGER_H_
#define PIN_MANAGER_H_

#include <stdint.h>

/////////////////////////
// conversion
/////////////////////////
typedef float (*PinInputConversion)(uint16_t pinValue);
typedef int16_t (*PinOutputConversion)(float pinValue);

float noInputConversion(uint16_t pinValue);
int16_t noOutputConversion(float pinValue);

/////////////////////////
// read / write
/////////////////////////
typedef uint16_t (*PinRead)(uint8_t pinId, uint8_t type);
typedef void (*PinWrite)(uint8_t pinId, uint8_t type, uint16_t value);

uint16_t defaultPinRead(uint8_t pinId, uint8_t type);
void defaultPinWrite(uint8_t pinId, uint8_t type, uint16_t value);




#include "../config/config.h"

const prog_char *setPinValue(uint8_t pinOutputIdx, float value);
float getPinValue(uint8_t pinId);

void pinInit();
void pinCheckInit();
void pinCheckChange();

#endif
