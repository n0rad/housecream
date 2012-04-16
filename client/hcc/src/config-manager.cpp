#include <avr/eeprom.h>
#include "config-manager.h"
#include "hcc.h"

t_pinData pinData;
t_boardData boardData;

const char PIN_STRING_INPUT[] PROGMEM = "INPUT";
const char PIN_STRING_OUTPUT[] PROGMEM = "OUTPUT";
const char PIN_STRING_NOTUSED[] PROGMEM = "NOTUSED";
const char PIN_STRING_RESERVED[] PROGMEM = "RESERVED";

const char *pinDirection[] = { PIN_STRING_INPUT, PIN_STRING_OUTPUT, PIN_STRING_NOTUSED, PIN_STRING_RESERVED};

const char PIN_TYPE_ANALOG[] PROGMEM = "ANALOG";
const char PIN_TYPE_DIGITAL[] PROGMEM = "DIGITAL";
const char *pinType[] = { PIN_TYPE_ANALOG, PIN_TYPE_DIGITAL};


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
        DEBUG_PRINT_FULL(sizeof(t_config));
        configSave();
    }
}


#define CONF_BOARD_MAC_SIZE sizeof(uint8_t) * 6
#define CONF_BOARD_IP_SIZE sizeof(uint8_t) * 4
#define CONF_BOARD_PORT_SIZE sizeof(uint16_t)
#define CONF_BOARD_NAME_SIZE sizeof(char) * 21


void getConfigMac(uint8_t mac[6]) {
    memcpy_P(mac, boardDescription.mac, 6);
}
void getConfigIP(uint8_t ip[4]) {
    eeprom_read_block((void*)ip, (void*) CONFIG_EEPROM_START, CONF_BOARD_IP_SIZE);
}
uint16_t getConfigPort() {
   return eeprom_read_word((uint16_t*)(CONFIG_EEPROM_START + CONF_BOARD_IP_SIZE));
}
uint8_t *getConfigBoardName_e() {
    return (uint8_t *) CONFIG_EEPROM_START + CONF_BOARD_IP_SIZE + CONF_BOARD_PORT_SIZE;
}
uint16_t getConfigPinValue(uint8_t pinId) {
    return pinData.lastValue;
}
char* getConfigNotifyUrl(void) {
    return boardData.notifyurl;
}

//////////////////

char *setConfigBoardName(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    DEBUG_PRINTLN("writing board name");
    if (len >  CONFIG_BOARD_NAME_SIZE) {
        return PSTR("name is too long");
    }
    eeprom_write_block(buf, (uint8_t *) CONFIG_EEPROM_START + CONF_BOARD_IP_SIZE + CONF_BOARD_PORT_SIZE, len);
    eeprom_write_byte((uint8_t *) CONFIG_EEPROM_START + CONF_BOARD_IP_SIZE + CONF_BOARD_PORT_SIZE + len, 0);
    return 0;
}



