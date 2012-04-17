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

char hcc_version[] PROGMEM = "0.1";


static void configSave() {
    for (int i = 0; i < sizeof(t_config); i++) {
        byte b = pgm_read_byte((byte*)&defaultConfig + i);
        eeprom_write_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + i, b);
    }
}

static void loadEeprom() {
    eeprom_read_block((void*)&boardData, (char*) CONFIG_EEPROM_START + sizeof(t_boardInfo), sizeof(boardData));
    eeprom_read_block((void*)&pinData, (char*) CONFIG_EEPROM_START + sizeof(t_boardInfo) + sizeof(t_boardData) + sizeof(t_pinInfo), sizeof(pinData));
}

void configLoad() {
    if (//eeprom_read_byte(CONFIG_EEPROM_START + sizeof(config) - 1) == config.version[4] && // this is '\0'
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(defaultConfig) - 2) == pgm_read_byte((char*)&defaultConfig + sizeof(defaultConfig) - 2) &&
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(defaultConfig) - 3) == pgm_read_byte((char*)&defaultConfig + sizeof(defaultConfig) - 3) &&
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(defaultConfig) - 4) == pgm_read_byte((char*)&defaultConfig + sizeof(defaultConfig) - 4) &&
        eeprom_read_byte((uint8_t *)(uint16_t)CONFIG_EEPROM_START + sizeof(defaultConfig) - 5) == pgm_read_byte((char*)&defaultConfig + sizeof(defaultConfig) - 5))
    {
        loadEeprom();
    } else {
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

char *handleUnsetableMac(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (len != 17) {
        return PSTR("mac cannot be set");
    }
    char strMac[17];
    uint8_t byteMac[6];
    getConfigMac(byteMac);
    sprintf(strMac, "%02X", byteMac[0]);
    sprintf(&strMac[3], "%02X", byteMac[1]);
    sprintf(&strMac[6], "%02X", byteMac[2]);
    sprintf(&strMac[9], "%02X", byteMac[3]);
    sprintf(&strMac[12], "%02X", byteMac[4]);
    sprintf(&strMac[15], "%02X", byteMac[5]);
    strMac[2] = ':';
    strMac[5] = ':';
    strMac[8] = ':';
    strMac[11] = ':';
    strMac[14] = ':';
    if (!strncmp(strMac, buf, len)) {
        return 0; // same mac
    }
    return PSTR("mac cannot be set");
}

char *handleUnsetableDescription(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, boardDescription.technicalDescription, len)) {
        return 0;
    }
    return PSTR("description cannot be set");
}
char *handleUnsetableSoftware(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (!strncmp(buf, "HouseCream Client", len)) {
        return 0;
    }
    return PSTR("software cannot be set");
}
char *handleUnsetableHardware(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, PSTR(HARDWARE), len)) {
        return 0;
    }
    return PSTR("hardware cannot be set");
}
char *handleUnsetableVersion(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, hcc_version, len)) {
        return 0;
    }
    return PSTR("version cannot be set");
}
char *handleUnsetableNumberOfPin(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    char pins[3] = {0, 0, 0};
    itoa(NUMBER_OF_PINS, pins, 10);
    if (!strncmp(pins, buf, len)) {
        return 0; // same number
    }
    return PSTR("numberOfPin cannot be set");
}

char *setConfigBoardNotifyUrl(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (len >  CONFIG_BOARD_NOTIFY_SIZE) {
        return PSTR("notifyUrl is too long");
    }
    eeprom_write_block(buf, (uint8_t *) CONFIG_EEPROM_START +  sizeof(t_boardInfo), len);
    eeprom_write_byte((uint8_t *) CONFIG_EEPROM_START +  sizeof(t_boardInfo) + len, 0);
    loadEeprom();
    return 0;
}
char *setConfigBoardName(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (len >  CONFIG_BOARD_NAME_SIZE) {
        return PSTR("name is too long");
    }
    eeprom_write_block(buf, (uint8_t *) CONFIG_EEPROM_START + CONF_BOARD_IP_SIZE + CONF_BOARD_PORT_SIZE, len);
    eeprom_write_byte((uint8_t *) CONFIG_EEPROM_START + CONF_BOARD_IP_SIZE + CONF_BOARD_PORT_SIZE + len, 0);
    return 0;
}

static char NOT_VALID_IP[] PROGMEM = "not valid ip";

char *setConfigBoardIP(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    char num = 0;
    char point = 0;
    for (uint8_t i = 0; i < len; i++) {
        if ((buf[i] < '0' || buf[i] > '9') && buf[i] != '.') {
            return NOT_VALID_IP;
        }
        num++;
        if (buf[i] == '.') {
            num = 0;
            point++;
        }
        if (num > 3) {
            return NOT_VALID_IP;
        }
    }
    if (point != 3) {
        return NOT_VALID_IP;
    }
    uint8_t newIp[4];
    newIp[0] = atoi(buf);
    newIp[1] = atoi(buf = &buf[my_strpos(buf, '.') + 1]);
    newIp[2] = atoi(buf = &buf[my_strpos(buf, '.') + 1]);
    newIp[3] = atoi(buf = &buf[my_strpos(buf, '.') + 1]);
    if (newIp[0] == 255 || newIp[1] == 255 || newIp[2] == 255 || newIp[3] == 255 // cannot be 255
            || newIp[0] == 0 || newIp[3] == 0) { // cannot start or finish with 0
        return NOT_VALID_IP;
    }
    eeprom_write_block(newIp, (uint8_t *) CONFIG_EEPROM_START, 4);
    extern boolean needReboot;
    needReboot = true;
    return 0;
}

static char NOT_VALID_PORT[] PROGMEM = "not valid port";

char *setConfigBoardPort(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    for (uint8_t i = 0; i < len; i++) {
        if (buf[i] < '0' || buf[i] > '9') {
            return NOT_VALID_PORT;
        }
    }
    uint16_t port = atoi(buf);
    eeprom_write_word((uint16_t*)(CONFIG_EEPROM_START + CONF_BOARD_IP_SIZE), port);
    extern boolean needReboot;
    needReboot = true;
    return 0;
}

