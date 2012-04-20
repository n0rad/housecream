#include "settings-board.h"

static char NOT_VALID_IP[] PROGMEM = "not valid ip";
static char NOT_VALID_PORT[] PROGMEM = "not valid port";


char *setConfigBoardNotifyUrl(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (len >  CONFIG_BOARD_NOTIFY_SIZE) {
        return PSTR("notifyUrl is too long");
    }
    eeprom_write_block(buf, (uint8_t *) CONFIG_EEPROM_START +  sizeof(t_boardInfo), len);
    eeprom_write_byte((uint8_t *) CONFIG_EEPROM_START +  sizeof(t_boardInfo) + len, 0);
    settingsReload();
    return 0;
}
char *setConfigBoardName(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (len >  CONFIG_BOARD_NAME_SIZE) {
        return PSTR("name is too long");
    }
    eeprom_write_block(buf, (uint8_t *) CONFIG_EEPROM_START + CONFIG_BOARD_IP_SIZE + CONFIG_BOARD_PORT_SIZE, len);
    eeprom_write_byte((uint8_t *) CONFIG_EEPROM_START + CONFIG_BOARD_IP_SIZE + CONFIG_BOARD_PORT_SIZE + len, 0);
    return 0;
}

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
    extern uint8_t needReboot;
    needReboot = true;
    return 0;
}

char *setConfigBoardPort(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    for (uint8_t i = 0; i < len; i++) {
        if (buf[i] < '0' || buf[i] > '9') {
            return NOT_VALID_PORT;
        }
    }
    uint16_t port = atoi(buf);
    eeprom_write_word((uint16_t*)(CONFIG_EEPROM_START + CONFIG_BOARD_IP_SIZE), port);
    extern uint8_t needReboot;
    needReboot = true;
    return 0;
}


//////////////


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
    if (!strncmp_P(buf, boardDescription.description, len)) {
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
