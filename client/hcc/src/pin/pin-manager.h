#ifndef PIN_MANAGER_H_
#define PIN_MANAGER_H_

float noConversion(float pinValue);
uint16_t defaultPinRead(uint8_t pinId);
void defaultPinWrite(uint8_t pinId, uint16_t value);

void pinCheckChange();

#endif
