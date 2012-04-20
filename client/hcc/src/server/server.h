#ifndef SERVER_H_
#define SERVER_H_

#include <avr/eeprom.h>
#include <string.h>

#include "../util/buffer.h"
#include "../hcc.h"
#include "../util/json.h"

const char DOUBLE_ENDL[] PROGMEM = "\r\n\r\n";
const prog_char RESOURCE_PIN[] PROGMEM = "/pin/";
const prog_char RESOURCE_BOARD[] PROGMEM = "/ ";
const prog_char GET[] PROGMEM = "GET ";
const prog_char PUT[] PROGMEM = "PUT ";

static prog_char HEADER_200[] PROGMEM = "HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\r\n";
static prog_char HEADER_500[] PROGMEM = "HTTP/1.0 500 Internal Server Error\r\nContent-Type: application/json\r\n\r\n";
static prog_char HEADER_404[] PROGMEM = "HTTP/1.0 404 Not Found\r\nContent-Type: application/json\r\n\r\n";
static prog_char HEADER_400[] PROGMEM = "HTTP/1.0 400 Bad Request\r\nContent-Type: application/json\r\n\r\n";
static prog_char HEADER_413[] PROGMEM = "HTTP/1.0 413 Request Entity Too Large\r\nContent-Type: application/json\r\n\r\n";
static prog_char BAD_PIN_REQUEST[] PROGMEM = "{\"message\":\"404 No resource on pin for this method & url\"}";
static prog_char CANNOT_READ_PINID[] PROGMEM = "{\"message\":\"Cannot read pin number in the request\"}";

uint16_t pinGet(char *buf, uint16_t dat_p, uint16_t plen);
uint16_t pinPut(char *buf, uint16_t dat_p, uint16_t plen);
uint16_t boardGet(char *buf, uint16_t dat_p, uint16_t plen);
uint16_t boardPut(char *buf, uint16_t dat_p, uint16_t plen);
uint16_t pinGetDescription(char *buf, uint8_t pinId);


uint16_t addToBufferTCPHex(char *buf, uint16_t pos, uint16_t val);
uint16_t addToBufferTCP(char *buf, uint16_t pos, uint16_t val);
uint16_t addToBufferTCP(char *buf, uint16_t pos, char val);
uint16_t addToBufferTCP(char *buf, uint16_t pos, char *mem_s);
uint16_t addToBufferTCP_P(char *buf, uint16_t pos, const prog_char *progmem_s);
uint16_t addToBufferTCP_e(char *buf, uint16_t pos, uint8_t *eeprom_s);

uint16_t handleWebRequest(char *buf, uint16_t dataPointer, uint16_t dataLen);

struct s_resource {
  const prog_char *method;
  const prog_char *query;
  uint16_t (*func)(char *buf, uint16_t dat_p, uint16_t plen);
} const resources[] PROGMEM = {
  {GET, RESOURCE_PIN, pinGet},
  {PUT, RESOURCE_PIN,  pinPut},
  {GET, RESOURCE_BOARD, boardGet},
  {PUT, RESOURCE_BOARD,  boardPut},
  {0, 0, 0}
};

#endif
