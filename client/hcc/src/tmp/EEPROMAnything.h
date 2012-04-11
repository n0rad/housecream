#include <eeprom.h>
#include <Arduino.h>  // for type definitions

template <class T> int EEPROM_writeAnything(int ee, const T& value)
{
    const byte* p = (const byte*)(const void*)&value;
    unsigned int i;
    for (i = 0; i < sizeof(value); i++)
          EEPROM.write(ee++, *p++);
    return i;
}

template <class T> int EEPROM_readAnything(int ee, T& value)
{
    byte* p = (byte*)(void*)&value;
    unsigned int i;
    for (i = 0; i < sizeof(value); i++)
          *p++ = EEPROM.read(ee++);
    return i;
}

#include <EEPROM.h>
#include "EEPROMAnything.h"

struct config_t
{
    long alarm;
    int mode;
} configuration;

void setup()
{
    EEPROM_readAnything(0, configuration);
    // ...
}
void loop()
{
    // let the user adjust their alarm settings
    // let the user adjust their mode settings
    // ...

    // if they push the "Save" button, save their configuration
    if (digitalRead(13) == HIGH)
        EEPROM_writeAnything(0, configuration);
}
