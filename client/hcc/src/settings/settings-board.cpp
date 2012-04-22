#include "settings-board.h"

static char NOT_VALID_IP[] PROGMEM = "not valid ip";
static char NOT_VALID_PORT[] PROGMEM = "not valid port";

prog_char *setConfigBoardNotifyUrl(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (len >  CONFIG_BOARD_NOTIFY_SIZE) {
        return PSTR("notifyUrl is too long");
    }
    eeprom_write_block(buf, (uint8_t *) CONFIG_EEPROM_START +  sizeof(t_boardInfo), len);
    eeprom_write_byte((uint8_t *) CONFIG_EEPROM_START +  sizeof(t_boardInfo) + len, 0);
    settingsReload();
    return 0;
}
prog_char *setConfigBoardName(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (len >  CONFIG_BOARD_NAME_SIZE) {
        return PSTR("name is too long");
    }
    eeprom_write_block(buf, (uint8_t *) CONFIG_EEPROM_START + CONFIG_BOARD_IP_SIZE + CONFIG_BOARD_PORT_SIZE, len);
    eeprom_write_byte((uint8_t *) CONFIG_EEPROM_START + CONFIG_BOARD_IP_SIZE + CONFIG_BOARD_PORT_SIZE + len, 0);
    return 0;
}

prog_char *setConfigBoardIP(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
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

prog_char *setConfigBoardPort(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
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


prog_char *setConfigBoardMac(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (len != 17) {
        return PSTR("mac cannot be set");
    }
    char strMac[17];
    uint8_t byteMac[6];
    getConfigMac(byteMac);
    sprintf_P(strMac, sprintfpHEX, byteMac[0]);
    sprintf_P(&strMac[3], sprintfpHEX, byteMac[1]);
    sprintf_P(&strMac[6], sprintfpHEX, byteMac[2]);
    sprintf_P(&strMac[9], sprintfpHEX, byteMac[3]);
    sprintf_P(&strMac[12], sprintfpHEX, byteMac[4]);
    sprintf_P(&strMac[15], sprintfpHEX, byteMac[5]);
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

prog_char *setConfigBoardDescription(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, boardDescription.description, len)) {
        return 0;
    }
    return PSTR("description cannot be set");
}
prog_char *setConfigBoardSoftware(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, PSTR("HouseCream Client"), len)) {
        return 0;
    }
    return PSTR("software cannot be set");
}
prog_char *setConfigBoardHardware(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, PSTR(HARDWARE), len)) {
        return 0;
    }
    return PSTR("hardware cannot be set");
}
prog_char *setConfigBoardVersion(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, hcc_version, len)) {
        return 0;
    }
    return PSTR("version cannot be set");
}
prog_char *setConfigBoardNumberOfPin(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    char pins[3] = {0, 0, 0};
    itoa(NUMBER_OF_PINS, pins, 10);
    if (!strncmp(pins, buf, len)) {
        return 0; // same number
    }
    return PSTR("numberOfPin cannot be set");
}
