#include "server.h"

uint16_t handleWebRequest(char *buf, uint16_t dataPointer, uint16_t dataLen) {
    DEBUG_PRINT(PSTR("Available memory : "));
    DEBUG_PRINTLN(availableMemory());
    DEBUG_PRINT(PSTR("DATA LEN : "));
    DEBUG_PRINTLN(dataLen);
    DEBUG_PRINTLN(&buf[dataPointer]);

    uint16_t plen;
    if (dataLen >= 999) {
        plen = addToBufferTCP_P(buf, 0, HEADER_413);
        plen = addToBufferTCP_P(buf, plen, PSTR("{\"message\":\"413: Request is too big, please be gentle\"}"));
        return plen;
    }

    if (criticalProblem_p) {
        plen = addToBufferTCP_P(buf, 0, HEADER_500);
        plen = addToBufferTCP_P(buf, plen, PSTR("{\"message\":\""));
        plen = addToBufferTCP_P(buf, plen, criticalProblem_p);
        plen = addToBufferTCP_P(buf, plen, PSTR("\"}"));
        return plen;
    }
    if (definitionError) {
        DEBUG_PRINTLN(definitionError);
        plen = addToBufferTCP_P(buf, 0, HEADER_500);
        plen = addToBufferTCP_P(buf, plen, PSTR("{\"message\":\""));
        plen = addToBufferTCP(buf, plen, definitionError);
        plen = addToBufferTCP_P(buf, plen, PSTR("\"}"));
        return plen;
    }

    uint8_t managed = false;
    for (int i = 0; resources[i].method; i++) {
        int reslen = strlen(resources[i].method);
        if (strncmp(resources[i].method, (char *) & (buf[dataPointer]), reslen) == 0
                && strncmp(resources[i].query, (char *) & (buf[dataPointer + reslen]), strlen(resources[i].query)) == 0) {
          plen = resources[i].func((char*)buf, dataPointer + reslen, dataLen);
          managed = true;
          break;
        }
    }
    if (!managed) {
        plen = addToBufferTCP_P(buf, 0, HEADER_404);
        plen = addToBufferTCP_P(buf, plen, PSTR("{\"message\":\"404: No resource for this method & url\"}"));
    }
    return plen;
}
