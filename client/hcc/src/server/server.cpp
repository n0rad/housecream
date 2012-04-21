#include "server.h"

uint16_t handleWebRequest(char *buf, uint16_t dataPointer, uint16_t dataLen) {
        DEBUG_p(PSTR("memory "));
        DEBUG_PRINTLN(getFreeMemory());

    DEBUG_p(PSTR("DATA LEN : "));
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
        plen = addToBufferTCP_P(buf, 0, HEADER_500);
        plen = addToBufferTCP_P(buf, plen, PSTR("{\"message\":\""));
        plen = addToBufferTCP(buf, plen, definitionError);
        plen = addToBufferTCP_P(buf, plen, PSTR("\"}"));
        return plen;
    }

    uint8_t managed = false;
    prog_char *methodPos;
    for (int i = 0; (methodPos = (prog_char *) pgm_read_word(&resources[i].method)); i++) {
        uint16_t reslen = strlen_P(methodPos);
        uint16_t querylen = strlen_P((prog_char *) pgm_read_word(&resources[i].query));
        prog_char *queryPos = (prog_char *) pgm_read_word(&resources[i].query);
        if (strncmp_P((char *) &(buf[dataPointer]), methodPos, reslen) == 0
                && strncmp_P((char *) & (buf[dataPointer + reslen]), queryPos, querylen) == 0) {
            ResourceFunc func = (ResourceFunc) pgm_read_word(&resources[i].resourceFunc);
            plen = func((char*)buf, dataPointer + reslen, dataLen);
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
