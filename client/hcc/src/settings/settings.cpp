#include "settings.h"

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


static void settingsSave() {
    for (int i = 0; i < sizeof(t_config); i++) {
        uint8_t b = pgm_read_byte((uint8_t*)&defaultConfig + i);
        eeprom_write_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + i, b);
    }
}

void settingsReload() {
    eeprom_read_block((void*)&boardData, (char*) CONFIG_EEPROM_START + sizeof(t_boardInfo), sizeof(boardData));
    eeprom_read_block((void*)&pinData, (char*) CONFIG_EEPROM_START + sizeof(t_boardInfo) + sizeof(t_boardData) + sizeof(t_pinInfo), sizeof(pinData));
}

void settingsLoad() {
    if (//eeprom_read_byte(CONFIG_EEPROM_START + sizeof(config) - 1) == config.version[4] && // this is '\0'
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(defaultConfig) - 2) == pgm_read_byte((char*)&defaultConfig + sizeof(defaultConfig) - 2) &&
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(defaultConfig) - 3) == pgm_read_byte((char*)&defaultConfig + sizeof(defaultConfig) - 3) &&
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(defaultConfig) - 4) == pgm_read_byte((char*)&defaultConfig + sizeof(defaultConfig) - 4) &&
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(defaultConfig) - 5) == pgm_read_byte((char*)&defaultConfig + sizeof(defaultConfig) - 5))
    {
        settingsReload();
    } else {
        settingsSave();
    }
}

void getConfigMac(uint8_t mac[6]) {
    memcpy_P(mac, boardDescription.mac, 6);
}
void getConfigIP(uint8_t ip[4]) {
    eeprom_read_block((void*)ip, (void*) CONFIG_EEPROM_START, CONFIG_BOARD_IP_SIZE);
}
uint16_t getConfigPort() {
   return eeprom_read_word((uint16_t*)(CONFIG_EEPROM_START + CONFIG_BOARD_IP_SIZE));
}
uint8_t *getConfigBoardName_e() {
    return (uint8_t *) CONFIG_EEPROM_START + CONFIG_BOARD_IP_SIZE + CONFIG_BOARD_PORT_SIZE;
}
uint16_t getConfigPinValue(uint8_t pinId) {
    return pinData.lastValue;
}
char* getConfigNotifyUrl(void) {
    return boardData.notifyurl;
}
