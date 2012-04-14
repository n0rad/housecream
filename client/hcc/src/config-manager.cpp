#include <avr/eeprom.h>
#include "config-manager.h"
#include "hcc.h"
#include <avr/pgmspace.h>

static s_boardData boardData;
static s_pinData pinData;

void configSave(void) {
    for (int i = 0; i < sizeof(t_config); i++) {
        byte b = pgm_read_byte((byte*)&defaultConfig + i);
        eeprom_write_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + i, b);
    }
}

void configLoad(void) {
    if (//eeprom_read_byte(CONFIG_EEPROM_START + sizeof(config) - 1) == config.version[4] && // this is '\0'
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(defaultConfig) - 2) == pgm_read_byte((char*)&defaultConfig + sizeof(defaultConfig) - 2) &&
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(defaultConfig) - 3) == pgm_read_byte((char*)&defaultConfig + sizeof(defaultConfig) - 3) &&
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(defaultConfig) - 4) == pgm_read_byte((char*)&defaultConfig + sizeof(defaultConfig) - 4) &&
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(defaultConfig) - 5) == pgm_read_byte((char*)&defaultConfig + sizeof(defaultConfig) - 5))
    {
        DEBUG_PRINT_FULL(F("Loading config from eeprom"));
        eeprom_read_block((void*)&boardData, (char*)sizeof(t_boardInfo), sizeof(boardData));
        eeprom_read_block((void*)&pinData, (char*)sizeof(t_boardInfo) + sizeof(t_boardData) + sizeof(t_pinInfo), sizeof(pinData));
    } else {
        DEBUG_PRINT_FULL(F("Version not found in eeprom, will load default config"));
        configSave();
    }
}


char* getNotifyUrl(void) {
    return boardData.notifyurl;
}

#define CONF_BOARD_MAC_SIZE sizeof(uint8_t) * 6
#define CONF_BOARD_IP_SIZE sizeof(uint8_t) * 4
#define CONF_BOARD_PORT_SIZE sizeof(uint16_t)
#define CONF_BOARD_NAME_SIZE sizeof(char) * 21

void getConfigIP(uint8_t ip[4]) {
    eeprom_read_block((void*)ip, (void*) CONFIG_EEPROM_START, CONF_BOARD_IP_SIZE);
}

void getConfigMac(uint8_t mac[6]) {
    for (int i = 0; i < sizeof(CONF_BOARD_MAC_SIZE); i++) {
        mac[i] = pgm_read_byte((byte*)&boardDescription + i);
    }
}

uint16_t getConfigPort() {
   return eeprom_read_word((uint16_t*)(CONFIG_EEPROM_START + CONF_BOARD_IP_SIZE));
}

void getBoardName(char name[21]) {
    eeprom_read_block((void*)name, (void*) (CONFIG_EEPROM_START + CONF_BOARD_IP_SIZE + CONF_BOARD_PORT_SIZE), CONF_BOARD_NAME_SIZE);
}

