#include <avr/eeprom.h>
#include "eeprom-config.h"
#include "hcc.h"


void configSave(void) {
    DEBUG_PRINT_FULL("Saving full config to eeprom")
    eeprom_write_block((const void*)&config, (void*)0, sizeof(config));
}

void configLoad(void) {
    if (//eeprom_read_byte(CONFIG_EEPROM_START + sizeof(config) - 1) == config.version[4] && // this is '\0'
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(config) - 2) == config.version[3] &&
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(config) - 3) == config.version[2] &&
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(config) - 4) == config.version[1] &&
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(config) - 5) == config.version[0])
    {
        DEBUG_PRINT_FULL("Loading config from eeprom");
        eeprom_read_block((void*)&config, (void*)0, sizeof(config));
    } else {
        DEBUG_PRINT_FULL("Version not found in eeprom, will load default config");
        configSave();
    }
}
