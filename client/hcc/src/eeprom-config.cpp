#include <avr/eeprom.h>
#include "eeprom-config.h"
#include "hcc.h"
#include <avr/pgmspace.h>

t_config config = {
    {
        {192, 168, 42, 245},                        // ip
        80,                                         // port
        "window1 controller",                       // name
        "192.168.42.86:8080/hcs/ws/event",          // notify url (without http://)
    },
    {
        0, "Windowtemp1", {PIN_NOTIFY_OVER_EQ, 1},
        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
//        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
    },
    CONFIG_VERSION
};


void configSave(void) {
    DEBUG_PRINT_FULL("Saving full config to eeprom");
    eeprom_write_block((const void*)&config, (void*)0, sizeof(config));
}

void configLoad(void) {
    DEBUG_START();
    DEBUG_PRINT("Configuration size is ");
    DEBUG_PRINTLN(sizeof(config));

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