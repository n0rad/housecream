#ifndef SERVER_BOARD_H
#define SERVER_BOARD_H

#include <string.h>

#include "../hcc.h"
#include "../settings/settings-board.h"
#include "../util/buffer.h"

uint16_t boardGet(char *buf, uint16_t dat_p, uint16_t plen, uint8_t pinId);
uint16_t boardPut(char *buf, uint16_t dat_p, uint16_t plen, uint8_t pinId);
uint16_t boardReset(char *buf, uint16_t dat_p, uint16_t plen, uint8_t pinId);
uint16_t boardReInit(char *buf, uint16_t dat_p, uint16_t plen, uint8_t pinId);

#include "server.h"

const prog_char BOARD_PARAM_NOTIFYURL[] PROGMEM = "notifyUrl";
const prog_char BOARD_PARAM_IP[] PROGMEM = "ip";
const prog_char BOARD_PARAM_PORT[] PROGMEM = "port";
const prog_char BOARD_PARAM_NUMBEROFPIN[] PROGMEM = "numberOfPin";
const prog_char BOARD_PARAM_HARDWARE[] PROGMEM = "hardware";
const prog_char BOARD_PARAM_VERSION[] PROGMEM = "version";
const prog_char BOARD_PARAM_SOFTWARE[] PROGMEM = "software";
const prog_char BOARD_PARAM_MAC[] PROGMEM = "mac";

const t_json boardPutElements[] PROGMEM = {
        {BOARD_PARAM_NUMBEROFPIN, setConfigBoardNumberOfPin},
        {BOARD_PARAM_HARDWARE, setConfigBoardHardware},
        {BOARD_PARAM_VERSION, setConfigBoardVersion},
        {BOARD_PARAM_SOFTWARE, setConfigBoardSoftware},
        {BOARD_PARAM_MAC, setConfigBoardMac},
        {PARAM_NAME, setConfigBoardName},
        {BOARD_PARAM_NOTIFYURL, setConfigBoardNotifyUrl},
        {PARAM_DESCRIPTION, setConfigBoardDescription},
        {BOARD_PARAM_IP, setConfigBoardIP},
        {BOARD_PARAM_PORT, setConfigBoardPort},
        {0, 0}
};

#endif
