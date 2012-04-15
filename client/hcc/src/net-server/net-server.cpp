#include "net-server.h"
#include "../hcc.h"
#include <avr/pgmspace.h>
#include <avr/eeprom.h>

#define TCP_CHECKSUM_L_P    0x33

t_resource  p_resource[] = {
  {GET, "/pin/", pinGet},
  {GET, "/ ", rootGet},
  {PUT, "/pin ",  pinPut},
  {PUT, "/ ",  rootPut},
  {0, 0, 0}
};

static char HEADER_200[] PROGMEM = "HTTP/1.0 200 OK\r\nContent-Type: application/json\r\n\r\n";
static char HEADER_500[] PROGMEM = "HTTP/1.0 500 Internal Server Error\r\nContent-Type: application/json\r\n\r\n";
static char HEADER_404[] PROGMEM = "HTTP/1.0 404 Not Found\r\nContent-Type: application/json\r\n\r\n";
static char HEADER_400[] PROGMEM = "HTTP/1.0 400 Bad Request\r\nContent-Type: application/json\r\n\r\n";
static char BAD_PIN_REQUEST[] PROGMEM = "{\"message\":\"404 No resource on pin for this method & url\"}";
static char CANNOT_READ_PINID[] PROGMEM = "{\"message\":\"Cannot read pin number in the request\"}";
static char MAC_SEPARATOR[] PROGMEM = ":";
static char IP_SEPARATOR[] PROGMEM = ".";

uint16_t addToBufferTCPHex(uint8_t *buf, uint16_t pos, uint16_t val) {
    static char value_to_add[4] = {0,0,0,0};
    int j = 0;

    sprintf(value_to_add, "%02X", val);
    while (value_to_add[j]) {
     buf[TCP_CHECKSUM_L_P + 3 + pos] = value_to_add[j++];
     pos++;
    }
    return pos;
}

uint16_t addToBufferTCP(uint8_t *buf, uint16_t pos, uint16_t val) {
    int j = 0;
    char value_to_add[16] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    sprintf(value_to_add, "%d", val);
    while (value_to_add[j]) {
      buf[TCP_CHECKSUM_L_P + 3 + pos] = value_to_add[j++];
      pos++;
    }
    return pos;
}

uint16_t addToBufferTCP(uint8_t *buf, uint16_t pos, char *mem_s) {
    char c;
    while ((c = *(mem_s++))) {
            buf[TCP_CHECKSUM_L_P + 3 + pos] = c;
            pos++;
    }
    return pos;
}

uint16_t addToBufferTCP_p(uint8_t *buf, uint16_t pos, const prog_char *progmem_s) {
    char c;
    while ((c = pgm_read_byte(progmem_s++))) {
            buf[TCP_CHECKSUM_L_P + 3 + pos] = c;
            pos++;
    }
    return pos;
}

uint16_t addToBufferTCP_e(uint8_t *buf, uint16_t pos, uint8_t *eeprom_s) {
    char c;
    while ((c = eeprom_read_byte(eeprom_s++))) {
            buf[TCP_CHECKSUM_L_P + 3 + pos] = c;
            pos++;
    }
    return pos;
}


uint16_t handleWebRequest(uint8_t *buf, uint16_t dataPointer) {
    DEBUG_PRINT(F("Available memory : "));
    DEBUG_PRINTLN(availableMemory());

    uint16_t plen;
    if (criticalProblem_p) {
        plen = addToBufferTCP_p(buf, 0, HEADER_500);
        plen = addToBufferTCP_p(buf, plen, PSTR("{\"message\":\""));
        plen = addToBufferTCP_p(buf, plen, criticalProblem_p);
        plen = addToBufferTCP_p(buf, plen, PSTR("\"}"));
        return plen;
    }

    boolean managed = false;
    for (int i = 0; p_resource[i].method; i++) {
        int reslen = strlen(p_resource[i].method);
        if (strncmp(p_resource[i].method, (char *) & (buf[dataPointer]), reslen) == 0
                && strncmp(p_resource[i].query, (char *) & (buf[dataPointer + reslen]), strlen(p_resource[i].query)) == 0) {
          plen = p_resource[i].func(buf, dataPointer + reslen, plen);
          managed = true;
          break;
        }
    }
    if (!managed) {
        plen = addToBufferTCP_p(buf, 0, HEADER_404);
        plen = addToBufferTCP_p(buf, plen, PSTR("{\"message\":\"404, No resource for this method & url\"}"));
    }
    return plen;
}

uint16_t rootGet(uint8_t *buf, uint16_t dat_p, uint16_t plen) {
    plen = addToBufferTCP_p(buf, 0, HEADER_200);
    plen = addToBufferTCP_p(buf, plen, PSTR("{\"software\":\"HouseCream Client\", \"version\":\""));
    plen = addToBufferTCP_p(buf, plen, PSTR(HCC_VERSION));

    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"hardware\":\""));
    plen = addToBufferTCP_p(buf, plen, PSTR(HARDWARE));

    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"name\":\""));
    plen = addToBufferTCP_p(buf, plen, boardDescription.name);

    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"description\":\""));
    plen = addToBufferTCP_p(buf, plen, boardDescription.description);

    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"technicalDescription\":\""));
    plen = addToBufferTCP_p(buf, plen, boardDescription.technicalDescription);

    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"notifyUrl\":\""));
    plen = addToBufferTCP_p(buf, plen, boardDescription.notifyurl);

    uint8_t ip[4];
    getConfigIP(ip);
    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"ip\":\""));
    plen = addToBufferTCP(buf, plen, ip[0]);
    plen = addToBufferTCP_p(buf, plen, IP_SEPARATOR);
    plen = addToBufferTCP(buf, plen, ip[1]);
    plen = addToBufferTCP_p(buf, plen, IP_SEPARATOR);
    plen = addToBufferTCP(buf, plen, ip[2]);
    plen = addToBufferTCP_p(buf, plen, IP_SEPARATOR);
    plen = addToBufferTCP(buf, plen, ip[3]);

    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"port\":"));
    plen = addToBufferTCP(buf, plen, getConfigPort());

    plen = addToBufferTCP_p(buf, plen, PSTR(",\"numberOfPin\":"));
    plen = addToBufferTCP(buf, plen, NUMBER_OF_PINS);

    uint8_t mac[6];
    getConfigMac(mac);
    plen = addToBufferTCP_p(buf, plen, PSTR(",\"mac\":\""));
    plen = addToBufferTCPHex(buf, plen, mac[0]);
    plen = addToBufferTCP_p(buf, plen, MAC_SEPARATOR);
    plen = addToBufferTCPHex(buf, plen, mac[1]);
    plen = addToBufferTCP_p(buf, plen, MAC_SEPARATOR);
    plen = addToBufferTCPHex(buf, plen, mac[2]);
    plen = addToBufferTCP_p(buf, plen, MAC_SEPARATOR);
    plen = addToBufferTCPHex(buf, plen, mac[3]);
    plen = addToBufferTCP_p(buf, plen, MAC_SEPARATOR);
    plen = addToBufferTCPHex(buf, plen, mac[4]);
    plen = addToBufferTCP_p(buf, plen, MAC_SEPARATOR);
    plen = addToBufferTCPHex(buf, plen, mac[5]);

    plen = addToBufferTCP_p(buf, plen, PSTR("\"}"));
    return plen;
}

uint16_t rootPut(uint8_t *buf, uint16_t dat_p, uint16_t plen) {
    return 0;
}

uint16_t pinPut(uint8_t *buf, uint16_t dat_p, uint16_t plen) {
    return 0;
}

uint16_t pinGet(uint8_t *buf, uint16_t dat_p, uint16_t plen) {
    uint8_t pinId;
    char request[15] = { 0 };

    //sscanf will always return 0 or 2 because it read HTTP/1.1 when there is nothing after the pinId
    int found = sscanf((char *) &buf[dat_p + 5], "%d%s", &pinId, request);
    if (found != 0) {
        if (pinId < 0 || pinId > NUMBER_OF_PINS - 1) {
            plen = addToBufferTCP_p(buf, 0, HEADER_400);
            plen = addToBufferTCP_p(buf, plen, PSTR("{\"message\":\"PinId overflow\"}"));
            return plen;
        }
        if (strncmp("/value", request, 5) == 0) {
            plen = addToBufferTCP_p(buf, 0, HEADER_200);
            plen = addToBufferTCP(buf, plen, getPinValue(pinId));
            return plen;
        } else if ((request[0] == '/' && !request[1])
                || strncmp("HTTP/", request, 5) == 0) {
            plen = pinGetDescription(buf, pinId);
            return plen;
        } else if (strncmp("/info", request, 4) == 0) {
            plen = addToBufferTCP_p(buf, 0, HEADER_200);
            plen = addToBufferTCP_p(buf, plen, PSTR("{info}"));
            return plen;
        } else {
            plen = addToBufferTCP_p(buf, 0, HEADER_404);
            plen = addToBufferTCP_p(buf, plen, BAD_PIN_REQUEST);
            return plen;
        }
    } else {
        plen = addToBufferTCP_p(buf, 0, HEADER_400);
        plen = addToBufferTCP_p(buf, plen, CANNOT_READ_PINID);
        return plen;
    }
    return plen;
}

uint16_t pinGetDescription(uint8_t *buf, uint8_t pinId) {
    uint16_t plen;

    plen = addToBufferTCP_p(buf, 0, HEADER_200);
    plen = addToBufferTCP_p(buf, plen, PSTR("{\"technicalDescription\":\""));
    plen = addToBufferTCP_p(buf, plen, (const prog_char *)&pinDescriptions[pinId].technicalDesc);

    uint8_t direction = pgm_read_byte(&pinDescriptions[pinId].direction);
    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"direction\":\""));
    plen = addToBufferTCP_p(buf, plen, pinDirection[direction]);

    uint8_t type = pgm_read_byte(&pinDescriptions[pinId].type);
    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"type\":\""));
    plen = addToBufferTCP_p(buf, plen, pinType[type]);

//    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"valueMin\":\""));
//    plen = addToBufferTCP_p(buf, plen, type ? PSTR("0") : PSTR("0"));
//
//    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"valueMax\":\""));
//    plen = addToBufferTCP_p(buf, plen, type ? PSTR("1") : PSTR("1"));


//  plen = addToBuffer(buf, plen, "\",\"pullUp\":\"");
//  plen = addToBuffer(buf, plen, current.pullUp);
//
//
//  plen = addToBuffer(buf, plen, "\",\"valueStep\":\"");
//  plen = addToBuffer(buf, plen, current.valueStep);

    plen = addToBufferTCP_p(buf, plen, PSTR("\"}"));
    return plen;
}




//
//
///**
// * Add a char * to buf buffer.
// * @return new position in buf
// */
//uint16_t addToBuffer(uint8_t *buf, unsigned long pos, char *value, unsigned int len) {
//  memcpy(&buf[TCP_CHECKSUM_L_P + 3 + pos], value, len);
//  return pos + len;
//}
//
///**
// * Add a char * to buf buffer.
// * @return new position in buf
// */
//uint16_t addToBuffer(uint8_t *buf, unsigned long pos, char *value) {
//  size_t size = strlen(value);
//  memcpy(&buf[TCP_CHECKSUM_L_P + 3 + pos], value, size);
//  return pos + size;
//}
//
