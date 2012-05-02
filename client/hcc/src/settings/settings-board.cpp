#include "settings-board.h"



void configBoardGetMac(uint8_t mac[6]) {
    memcpy_P(mac, boardDescription.mac, 6);
}
void settingsBoardGetIP(uint8_t ip[4]) {
    eeprom_read_block((void*)ip, (uint8_t *)offsetof(t_boardSettings, ip), CONFIG_BOARD_IP_SIZE);
}
uint16_t settingsBoardGetPort() {
   return eeprom_read_word((uint16_t*) offsetof(t_boardSettings, port));
}
uint8_t *settingsBoardGetName_E() {
    return (uint8_t *) offsetof(t_boardSettings, name);
}
uint8_t *settingsBoardGetNotifyUrl_E() {
    return (uint8_t *) offsetof(t_boardSettings, notifyUrl);
}

//


const prog_char *configBoardSetNotifyUrl(char *buf, uint16_t len, uint8_t index) {
    if (len >  CONFIG_BOARD_NOTIFY_SIZE - 1) {
        return PSTR("notifyUrl is too long");
    }
    if (strncmp_P(buf, PSTR("http://"), 7)) {
        return PSTR("http only for notifyUrl");
    }

    uint8_t newIp[4];
    if (readIP(&buf[7], strspn_P(&buf[7], IP_CHARACTERS), newIp)) {
        return PSTR("invalid Ip in notifyUrl");
    }
    eeprom_write_block(buf, (uint8_t *) offsetof(t_boardSettings, notifyUrl), len);
    eeprom_write_byte((uint8_t *) (offsetof(t_boardSettings, notifyUrl) + len), 0);
    settingsReload();
    return 0;
}
const prog_char *configBoardSetName(char *buf, uint16_t len, uint8_t index) {
    if (len >  CONFIG_BOARD_NAME_SIZE - 1) {
        return NAME_TOO_LONG;
    }
    eeprom_write_block(buf, (uint8_t *) offsetof(t_boardSettings, name), len);
    eeprom_write_byte((uint8_t *) (offsetof(t_boardSettings, name) + len), 0);
    return 0;
}

const prog_char *configBoardSetIP(char *buf, uint16_t len, uint8_t index) {
    uint8_t newIp[4];
    if (readIP(buf, len, newIp)) {
        return NOT_VALID_IP;
    }

    uint8_t currentIp[4];
    settingsBoardGetIP(currentIp);
    if (currentIp[0] != newIp[0] || currentIp[1] != newIp[1] || currentIp[2] != newIp[2] || currentIp[3] != newIp[3]) {
        eeprom_write_block(newIp, (uint8_t *) offsetof(t_boardSettings, ip), 4);
        needReboot = true;
    }
    return 0;
}

const prog_char *configBoardSetPort(char *buf, uint16_t len, uint8_t index) {
    for (uint8_t i = 0; i < len; i++) {
        if (buf[i] < '0' || buf[i] > '9') {
            return NOT_VALID_PORT;
        }
    }
    uint16_t port = atoi(buf);
    if (port != settingsBoardGetPort()) {
        eeprom_write_word((uint16_t*)offsetof(t_boardSettings, port), port);
        needReboot = true;
    }
    return 0;
}

const prog_char *configBoardSetMac(char *buf, uint16_t len, uint8_t index) {
    if (len != 17) {
        return CANNOT_SET_MAC;
    }
    char strMac[18];
    uint8_t byteMac[6];
    configBoardGetMac(byteMac);
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

const prog_char *configBoardSetDescription(char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, boardDescription.description, len)) {
        return 0;
    }
    return DESCRIPTION_CANNOT_BE_SET;
}
const prog_char *configBoardSetSoftware(char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, PSTR("HouseCream Client"), len)) {
        return 0;
    }
    return PSTR("cannot set software");
}
const prog_char *configBoardSetHardware(char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, PSTR(HARDWARE), len)) {
        return 0;
    }
    return PSTR("cannot set hardware");
}
const prog_char *configBoardSetVersion(char *buf, uint16_t len, uint8_t index) {
    if (!strncmp_P(buf, hcc_version, len)) {
        return 0;
    }
    return PSTR("cannot set version");
}
const prog_char *configBoardSetPinIds(char *buf, uint16_t len, uint8_t index) {
    uint8_t bufPinId = atoi(buf);
    uint8_t currentPinId;
    if (index < pinInputSize) {
        currentPinId = pgm_read_byte(&pinInputDescription[index].pinId);
    } else {
        currentPinId = pgm_read_byte(&pinOutputDescription[index - pinInputSize].pinId);
    }
    if (currentPinId != bufPinId) {
        return PSTR("cannot set pinId");
    }
    return 0;
}

const prog_char *configBoardHandlePinIdsArray(uint8_t index) {
    return 0;
}
