#ifndef SERVER_BOARD_H
#define SERVER_BOARD_H

#include <string.h>

#include "../hcc.h"
#include "../settings/settings-board.h"
#include "../util/buffer.h"
#include "../client/client.h"

uint16_t boardGet(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource);
uint16_t boardPut(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource);
uint16_t boardReset(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource);
uint16_t boardReInit(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource);
uint16_t boardNotify(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource);

#include "server.h"

const prog_char BOARD_PARAM_NOTIFYURL[] PROGMEM = "notifyUrl";
const prog_char BOARD_PARAM_IP[] PROGMEM = "ip";
const prog_char BOARD_PARAM_PORT[] PROGMEM = "port";
const prog_char BOARD_PARAM_PINIDS[] PROGMEM = "pinIds";
const prog_char BOARD_PARAM_HARDWARE[] PROGMEM = "hardware";
const prog_char BOARD_PARAM_VERSION[] PROGMEM = "version";
const prog_char BOARD_PARAM_SOFTWARE[] PROGMEM = "software";
const prog_char BOARD_PARAM_MAC[] PROGMEM = "mac";

const t_json boardPutElements[] PROGMEM = {
        {BOARD_PARAM_PINIDS, configBoardSetPinIds, 0, configBoardHandlePinIdsArray},
        {BOARD_PARAM_HARDWARE, configBoardSetHardware, 0, 0},
        {BOARD_PARAM_VERSION, configBoardSetVersion, 0, 0},
        {BOARD_PARAM_SOFTWARE, configBoardSetSoftware, 0, 0},
        {BOARD_PARAM_MAC, configBoardSetMac, 0, 0},
        {PARAM_NAME, configBoardSetName, 0, 0},
        {BOARD_PARAM_NOTIFYURL, configBoardSetNotifyUrl, 0, 0},
        {PARAM_DESCRIPTION, configBoardSetDescription, 0, 0},
        {BOARD_PARAM_IP, configBoardSetIP, 0, 0},
        {BOARD_PARAM_PORT, configBoardSetPort, 0, 0},
        {0, 0, 0, 0}
};

const t_json boardPutObj PROGMEM = {0, 0, (t_json *)boardPutElements, 0};

#endif
