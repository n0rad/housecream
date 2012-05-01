#include "settings.h"

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
//    eeprom_read_block((void*)&boardData, (char*) CONFIG_EEPROM_START + sizeof(t_boardInfo), sizeof(boardData));
//    eeprom_read_block((void*)&pinData, (char*) CONFIG_EEPROM_START + sizeof(t_boardInfo) + sizeof(t_boardData) + sizeof(t_pinInfo), sizeof(pinData));
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

        DEBUG_PRINTLN("found");
        settingsReload();
    } else {
        DEBUG_PRINTLN("NOTfound");
        settingsSave();
    }
}
