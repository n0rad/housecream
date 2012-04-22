#ifndef SERVER_H_
#define SERVER_H_

#include <avr/eeprom.h>
#include <string.h>

#include "../settings/settings.h"
#include "../util/buffer.h"
#include "../hcc.h"
#include "../util/json.h"


const prog_char DOUBLE_ENDL[] PROGMEM = "\r\n\r\n";
const prog_char RESOURCE_PIN[] PROGMEM = "/pin/";
const prog_char RESOURCE_BOARD[] PROGMEM = "/ ";
const prog_char RESOURCE_RESET[] PROGMEM = "/reset";
const prog_char RESOURCE_INIT[] PROGMEM = "/init";
const prog_char GET[] PROGMEM = "GET ";
const prog_char PUT[] PROGMEM = "PUT ";

const prog_char HEADER_HTTP[] PROGMEM = "HTTP/1.0 ";
const prog_char HEADER_CONTENT[] PROGMEM = "Content-Type: application/json";
const prog_char HEADER_200[] PROGMEM = "200 OK";
const prog_char HEADER_500[] PROGMEM = "500 Internal Server Error";
const prog_char HEADER_404[] PROGMEM = "404 Not Found";
const prog_char HEADER_400[] PROGMEM = "400 Bad Request";
const prog_char HEADER_413[] PROGMEM = "413 Request Entity Too Large";

const prog_char JSON_STR_END[] PROGMEM = "\"}";
const prog_char ERROR_MSG_START[] PROGMEM = "{\"message\":\"";

const prog_char PARAM_NAME[] PROGMEM = "name";
const prog_char PARAM_DESCRIPTION[] PROGMEM = "description";

uint16_t startResponseHeader(char **buf, const prog_char *codeMsg);
uint16_t appendErrorMsg_P(char *buf, uint16_t plen, const prog_char *msg);
uint16_t appendErrorMsg(char *buf, uint16_t plen, char *msg);
uint16_t appendJsonKey(char *buf, uint16_t plen, const prog_char *key);


#include "server-board.h"
#include "server-pin.h"


uint16_t handleWebRequest(char *buf, uint16_t dataPointer, uint16_t dataLen);

typedef uint16_t (*ResourceFunc)(char *buf, uint16_t dat_p, uint16_t plen);

struct s_resource {
  const prog_char *method;
  const prog_char *query;
  ResourceFunc resourceFunc;
} const resources[] PROGMEM = {
  {GET, RESOURCE_PIN, pinGet},
  {PUT, RESOURCE_PIN,  pinPut},
  {GET, RESOURCE_BOARD, boardGet},
  {PUT, RESOURCE_BOARD,  boardPut},
  {PUT, RESOURCE_RESET,  boardReset},
  {PUT, RESOURCE_INIT,  boardReInit},
  {0, 0, 0}
};

#endif
