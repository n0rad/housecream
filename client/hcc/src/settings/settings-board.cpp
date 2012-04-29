#include "settings-board.h"

static uint8_t readIP(char *buf, uint16_t len, uint8_t newIp[4]) {
    char num = 0;
    char point = 0;
    for (uint8_t i = 0; i < len; i++) {
        if ((buf[i] < '0' || buf[i] > '9') && buf[i] != '.') {
            return 1;
        }
        num++;
        if (buf[i] == '.') {
            num = 0;
            point++;
        }
        if (num > 3) {
            return 1;
        }
    }
    if (point != 3) {
        return 1;
    }
    newIp[0] = atoi(buf);
    newIp[1] = atoi(buf = &buf[my_strpos(buf, '.') + 1]);
    newIp[2] = atoi(buf = &buf[my_strpos(buf, '.') + 1]);
    newIp[3] = atoi(buf = &buf[my_strpos(buf, '.') + 1]);
    if (newIp[0] == 255 || newIp[1] == 255 || newIp[2] == 255 || newIp[3] == 255 // cannot be 255
            || newIp[0] == 0 || newIp[3] == 0) { // cannot start or finish with 0
        return 1;
    }

    return 0;
}

const prog_char *setConfigBoardNotifyUrl(char *buf, uint16_t len, uint8_t index) {
    if (len >  CONFIG_BOARD_NOTIFY_SIZE - 1) {
        return PSTR("notifyUrl is too long");
    }
    if (strncmp_P(buf, PSTR("http://"), 7)) {
        return PSTR("http only for notifyUrl");
    }

    uint8_t newIp[4];
    if (readIP(&buf[7], strspn(&buf[7], "0123456789."), newIp)) {
        return PSTR("Not valid Ip in notifyUrl");
    }
    eeprom_write_block(buf, (uint8_t *) offsetof(t_boardSettings, notifyUrl), len);
    eeprom_write_byte((uint8_t *) (offsetof(t_boardSettings, notifyUrl) + len), 0);
    settingsReload();
    return 0;
}
const prog_char *setConfigBoardName(char *buf, uint16_t len, uint8_t index) {
    if (len >  CONFIG_BOARD_NAME_SIZE - 1) {
        return PSTR("name is too long");
    }
    eeprom_write_block(buf, (uint8_t *) offsetof(t_boardSettings, name), len);
    eeprom_write_byte((uint8_t *) (offsetof(t_boardSettings, name) + len), 0);
    return 0;
}

const prog_char *setConfigBoardIP(char *buf, uint16_t len, uint8_t index) {
    uint8_t newIp[4];
    if (readIP(buf, len, newIp)) {
        return NOT_VALID_IP;
    }

    uint8_t currentIp[4];
    getConfigBoardIP(currentIp);
    if (currentIp[0] != newIp[0] || currentIp[1] != newIp[1] || currentIp[2] != newIp[2] || currentIp[3] != newIp[3]) {
        eeprom_write_block(newIp, (uint8_t *) offsetof(t_boardSettings, ip), 4);
        needReboot = true;
    }
    return 0;
}

const prog_char *setConfigBoardPort(char *buf, uint16_t len, uint8_t index) {
    for (uint8_t i = 0; i < len; i++) {
        if (buf[i] < '0' || buf[i] > '9') {
            return NOT_VALID_PORT;
        }
    }
    uint16_t port = atoi(buf);
    if (port != getConfigBoardPort()) {
        eeprom_write_word((uint16_t*)offsetof(t_boardSettings, port), port);
        needReboot = true;
    }
    return 0;
}


const prog_char *setConfigBoardMac(char *buf, uint16_t len, uint8_t index) {
    if (len != 17) {
        return CANNOT_SET_MAC;
    }
    char strMac[17];
    uint8_t byteMac[6];
    getConfigBoardMac(byteMac);
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
    return CANNOT_SET_MAC;
}

const prog_char *setConfigBoardDescription(char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, boardDescription.description, len)) {
        return 0;
    }
    return PSTR("description cannot be set");
}
const prog_char *setConfigBoardSoftware(char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, PSTR("HouseCream Client"), len)) {
        return 0;
    }
    return PSTR("software cannot be set");
}
const prog_char *setConfigBoardHardware(char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, PSTR(HARDWARE), len)) {
        return 0;
    }
    return PSTR("hardware cannot be set");
}
const prog_char *setConfigBoardVersion(char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, hcc_version, len)) {
        return 0;
    }
    return PSTR("version cannot be set");
}
const prog_char *setConfigBoardPinId(char *buf, uint16_t len, uint8_t index) {
    DEBUG_PRINT(">>index ");
    DEBUG_PRINTLN(index);

    //    char pins[3] = {0, 0, 0};
//    itoa(NUMBER_OF_PINS, pins, 10);
//    if (!strncmp(pins, buf, len)) {
        return 0; // same number
//    }
//    return PSTR("numberOfPin cannot be set");
}
