#include "settings.h"

uint8_t NotifyDstIp[4];
uint16_t notifyDstPort;
char notifyUrlPrefix[36];

t_notify **pinNotifies = 0;


uint8_t pinInputSize = 0;
uint8_t pinOutputSize = 0;

const prog_char *pinType[] PROGMEM = { PIN_TYPE_ANALOG, PIN_TYPE_DIGITAL};
const prog_char *pinNotification[] PROGMEM = { PIN_NOTIFICATION_SUP, PIN_NOTIFICATION_INF};

static void eeprom_write_block_P(uint8_t *eepromPos, prog_char *progPos, uint16_t len) {
    for (uint16_t i = 0; i < len; i++) {
        uint8_t data = pgm_read_byte(&progPos[i]);
        eeprom_write_byte(&eepromPos[i], data);
    }
}

void settingsSave() {
    eeprom_write_block_P((uint8_t *)offsetof(t_boardSettings, ip), (prog_char *)&boardDescription.ip, CONFIG_BOARD_IP_SIZE);
    eeprom_write_word((uint16_t *)offsetof(t_boardSettings, port), pgm_read_word(&boardDescription.port));
    eeprom_write_block_P((uint8_t *)offsetof(t_boardSettings, name), (prog_char *)&boardDescription.name, CONFIG_BOARD_NAME_SIZE);
    eeprom_write_block_P((uint8_t *)offsetof(t_boardSettings, notifyUrl), (prog_char *)&boardDescription.notifyurl, CONFIG_BOARD_NOTIFY_SIZE);

    int8_t pinId;
    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinInputDescription[i].pinId)); i++) {
        uint16_t pinPos = sizeof(t_boardSettings) + sizeof(t_pinInputSettings) * i;
        eeprom_write_block_P((uint8_t *)(pinPos + offsetof(t_pinInputSettings, name)), (prog_char *) &pinInputDescription[i].name, CONFIG_PIN_NAME_SIZE);
        eeprom_write_block_P((uint8_t *)(pinPos + offsetof(t_pinInputSettings, notifies)), (prog_char *)&pinInputDescription[i].notifies, sizeof(t_notify) * PIN_NUMBER_OF_NOTIFY);
    }

    for (uint8_t i = 0; -1 != (pinId = (int8_t) pgm_read_byte(&pinOutputDescription[i].pinId)); i++) {
        uint16_t pinPos = sizeof(t_boardSettings) + (sizeof(t_pinInputSettings) * pinInputSize) + (sizeof(t_pinOutputSettings) * i);
        eeprom_write_block_P((uint8_t *)(pinPos + offsetof(t_pinOutputSettings, name)), (prog_char *) &pinOutputDescription[i].name, CONFIG_PIN_NAME_SIZE);
        eeprom_write_dword((uint32_t *)(pinPos + offsetof(t_pinOutputSettings, lastValue)), pgm_read_dword(&(pinOutputDescription[i].startValue)));
    }

    eeprom_write_block_P((uint8_t *)(sizeof(t_boardSettings) + (sizeof(t_pinInputSettings) * pinInputSize) + (sizeof(t_pinOutputSettings) * pinOutputSize)),
            (prog_char *) CONFIG_VERSION, 4);

    //TODO write 0 till the end of eeprom because if you reduce the number of pin and then put it back to same value it will find the version
    //TODO and will not rewrite the conf and the eeprom conf will be corrupted
}

void settingsReload() {
    char tmpNotifyUrl[CONFIG_BOARD_NOTIFY_SIZE];
    eeprom_read_block((void*)&tmpNotifyUrl, (char*) offsetof(t_boardSettings, notifyUrl), CONFIG_BOARD_NOTIFY_SIZE);

    // notify ip
    uint16_t endIP = strspn_P(&tmpNotifyUrl[7], IP_CHARACTERS);
    readIP(&tmpNotifyUrl[7], endIP, NotifyDstIp);

    // notify port
    uint16_t startUrl = 7 + endIP;
    if (tmpNotifyUrl[7 + endIP] == ':') {
        notifyDstPort = atoi(&tmpNotifyUrl[startUrl + 1]);
        startUrl += strspn_P(&tmpNotifyUrl[startUrl + 1], PSTR("0123456789")) + 1;
    } else {
        notifyDstPort = 80;
    }

    // notify url
    uint16_t len = strlen(&tmpNotifyUrl[startUrl]);
    memcpy(notifyUrlPrefix, &tmpNotifyUrl[startUrl], len + 1);

    // notifies
    for (uint8_t i = 0; i < pinInputSize; i++) {
        for (uint8_t j = 0; j < 4; j++) {
            uint16_t eepromPos = sizeof(t_boardSettings) + (sizeof(t_pinInputSettings) * i);
            eepromPos += offsetof(t_pinInputSettings, notifies) + (sizeof(t_notify) * j);
            eeprom_read_block((void *)&pinNotifies[i][j], (char *)eepromPos, sizeof(t_notify));
        }
    }

}

void settingsLoad() {
    // find size of pin list
    for (; -1 != (int8_t) pgm_read_byte(&pinInputDescription[pinInputSize].pinId); pinInputSize++);
    for (; -1 != (int8_t) pgm_read_byte(&pinOutputDescription[pinOutputSize].pinId); pinOutputSize++);

    uint16_t confVersionPos = sizeof(t_boardSettings) + (sizeof(t_pinInputSettings) * pinInputSize) + (sizeof(t_pinOutputSettings) * pinOutputSize);
    if (eeprom_read_byte((uint8_t *) confVersionPos) == pgm_read_byte(CONFIG_VERSION)               // h
        && eeprom_read_byte((uint8_t *) confVersionPos + 1) == pgm_read_byte(CONFIG_VERSION + 1)    // c
        && eeprom_read_byte((uint8_t *) confVersionPos + 2) == pgm_read_byte(CONFIG_VERSION + 2)    // c
        && eeprom_read_byte((uint8_t *) confVersionPos + 3) == pgm_read_byte(CONFIG_VERSION + 3)) { //\0

    } else {
        settingsSave();
    }

    // allocate notifies in ram
    pinNotifies = (t_notify **) malloc(pinInputSize * sizeof(t_notify *));
    int8_t pinId;
    for (uint8_t i = 0; i < pinInputSize; i++) {
        pinNotifies[i] = (t_notify *) malloc(4 * sizeof(t_notify));
    }

    settingsReload();
}
