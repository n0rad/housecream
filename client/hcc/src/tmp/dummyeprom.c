#include <avr/eeprom.h>

struct settings_t
{
  long alarm;
  int mode;
} settings;

void setup()
{
  eeprom_read_block((void*)&settings, (void*)0, sizeof(settings));
    // ...
}
void loop()
{
    // let the user adjust their alarm settings
    // let the user adjust their mode settings
    // ...

    // if they push the "Save" button, save their configuration
    if (digitalRead(13) == HIGH)
      eeprom_write_block((const void*)&settings, (void*)0, sizeof(settings));
}
