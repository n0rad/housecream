#ifndef SERVER_BOARD_H
#define SERVER_BOARD_H

#include <string.h>

#include "../hcc.h"
#include "../settings/settings-board.h"
#include "../util/buffer.h"

uint16_t boardGet(char *buf, uint16_t dat_p, uint16_t plen);
uint16_t boardPut(char *buf, uint16_t dat_p, uint16_t plen);
uint16_t boardReset(char *buf, uint16_t dat_p, uint16_t plen);
uint16_t boardReInit(char *buf, uint16_t dat_p, uint16_t plen);

#include "server.h"

const prog_char BOARDPUT_PARAM_NAME[] PROGMEM = "name";
const prog_char BOARDPUT_PARAM_NOTIFYURL[] PROGMEM = "notifyUrl";
const prog_char BOARDPUT_PARAM_DESCRIPTION[] PROGMEM = "description";
const prog_char BOARDPUT_PARAM_IP[] PROGMEM = "ip";
const prog_char BOARDPUT_PARAM_PORT[] PROGMEM = "port";
const prog_char BOARDPUT_PARAM_NUMBEROFPIN[] PROGMEM = "numberOfPin";
const prog_char BOARDPUT_PARAM_HARDWARE[] PROGMEM = "hardware";
const prog_char BOARDPUT_PARAM_VERSION[] PROGMEM = "version";
const prog_char BOARDPUT_PARAM_SOFTWARE[] PROGMEM = "software";
const prog_char BOARDPUT_PARAM_MAC[] PROGMEM = "mac";

const t_json boardPutElements[] PROGMEM = {
        {BOARDPUT_PARAM_NUMBEROFPIN, setConfigBoardNumberOfPin},
        {BOARDPUT_PARAM_HARDWARE, setConfigBoardHardware},
        {BOARDPUT_PARAM_VERSION, setConfigBoardVersion},
        {BOARDPUT_PARAM_SOFTWARE, setConfigBoardSoftware},
        {BOARDPUT_PARAM_MAC, setConfigBoardMac},
        {BOARDPUT_PARAM_NAME, setConfigBoardName},
        {BOARDPUT_PARAM_NOTIFYURL, setConfigBoardNotifyUrl},
        {BOARDPUT_PARAM_DESCRIPTION, setConfigBoardDescription},
        {BOARDPUT_PARAM_IP, setConfigBoardIP},
        {BOARDPUT_PARAM_PORT, setConfigBoardPort},
        {0, 0}
};

#endif
