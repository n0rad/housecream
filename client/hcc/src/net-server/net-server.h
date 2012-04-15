#ifndef NET_SERVER_H_
#define NET_SERVER_H_

#include "../hcc.h"
#include "../util/json.h"

#define GET "GET "
#define PUT "PUT "

uint16_t pinGet(char *buf, uint16_t dat_p, uint16_t plen);
uint16_t pinPut(char *buf, uint16_t dat_p, uint16_t plen);
uint16_t boardGet(char *buf, uint16_t dat_p, uint16_t plen);
uint16_t boardPut(char *buf, uint16_t dat_p, uint16_t plen);
uint16_t pinGetDescription(char *buf, uint8_t pinId);

static char HEADER_200[] PROGMEM = "HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\r\n";
static char HEADER_500[] PROGMEM = "HTTP/1.0 500 Internal Server Error\r\nContent-Type: application/json\r\n\r\n";
static char HEADER_404[] PROGMEM = "HTTP/1.0 404 Not Found\r\nContent-Type: application/json\r\n\r\n";
static char HEADER_400[] PROGMEM = "HTTP/1.0 400 Bad Request\r\nContent-Type: application/json\r\n\r\n";
static char BAD_PIN_REQUEST[] PROGMEM = "{\"message\":\"404 No resource on pin for this method & url\"}";
static char CANNOT_READ_PINID[] PROGMEM = "{\"message\":\"Cannot read pin number in the request\"}";
static char MAC_SEPARATOR[] PROGMEM = ":";
static char IP_SEPARATOR[] PROGMEM = ".";

static char DOUBLE_ENDL[] = "\r\n\r\n";
#define TCP_CHECKSUM_L_P    0x33
uint16_t addToBufferTCPHex(char *buf, uint16_t pos, uint16_t val);
uint16_t addToBufferTCP(char *buf, uint16_t pos, uint16_t val);
uint16_t addToBufferTCP(char *buf, uint16_t pos, char *mem_s);
uint16_t addToBufferTCP_p(char *buf, uint16_t pos, const prog_char *progmem_s);
uint16_t addToBufferTCP_e(char *buf, uint16_t pos, uint8_t *eeprom_s);


typedef struct s_resource {
  char *method;
  char *query;
  uint16_t (*func)(char *buf, uint16_t dat_p, uint16_t plen);
} t_resource;


uint16_t handleWebRequest(char *buf, uint16_t dataPointer, uint16_t dataLen);

#endif /* NET_SERVER_H_ */
