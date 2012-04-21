#ifndef SERVER_H_
#define SERVER_H_

#include <avr/eeprom.h>
#include <string.h>

#include "../settings/settings.h"
#include "../util/buffer.h"
#include "../hcc.h"
#include "../util/json.h"


const char DOUBLE_ENDL[] PROGMEM = "\r\n\r\n";
const prog_char RESOURCE_PIN[] PROGMEM = "/pin/";
const prog_char RESOURCE_BOARD[] PROGMEM = "/ ";
const prog_char RESOURCE_RESET[] PROGMEM = "/reset";
const prog_char RESOURCE_INIT[] PROGMEM = "/init";
const prog_char GET[] PROGMEM = "GET ";
const prog_char PUT[] PROGMEM = "PUT ";

static prog_char HEADER_200[] PROGMEM = "HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\r\n";
static prog_char HEADER_500[] PROGMEM = "HTTP/1.0 500 Internal Server Error\r\nContent-Type: application/json\r\n\r\n";
static prog_char HEADER_404[] PROGMEM = "HTTP/1.0 404 Not Found\r\nContent-Type: application/json\r\n\r\n";
static prog_char HEADER_400[] PROGMEM = "HTTP/1.0 400 Bad Request\r\nContent-Type: application/json\r\n\r\n";
static prog_char HEADER_413[] PROGMEM = "HTTP/1.0 413 Request Entity Too Large\r\nContent-Type: application/json\r\n\r\n";
static prog_char BAD_PIN_REQUEST[] PROGMEM = "{\"message\":\"404 No resource on pin for this method & url\"}";
static prog_char CANNOT_READ_PINID[] PROGMEM = "{\"message\":\"Cannot read pin number in the request\"}";



#include "server-board.h"
#include "server-pin.h"

//uint16_t pinGetDescription(char *buf, uint8_t pinId);

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
