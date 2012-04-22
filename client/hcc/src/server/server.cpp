#include "server.h"

uint16_t startResponseHeader(char *buf, const prog_char *codeMsg) {
    uint16_t plen;
    plen = addToBufferTCP_P(buf, 0, HEADER_HTTP);
    plen = addToBufferTCP_P(buf, plen, HEADER_CONTENT);
    plen = addToBufferTCP_P(buf, plen, codeMsg);
    plen = addToBufferTCP_P(buf, plen, DOUBLE_ENDL);
    return plen;
}

uint16_t appendErrorMsg_P(char *buf, uint16_t plen, const prog_char *msg) {
    plen = addToBufferTCP_P(buf, plen, ERROR_MSG_START);
    plen = addToBufferTCP_P(buf, plen, msg);
    plen = addToBufferTCP_P(buf, plen, JSON_STR_END);
    return plen;
}

uint16_t appendErrorMsg(char *buf, uint16_t plen, char *msg) {
    plen = addToBufferTCP_P(buf, plen, ERROR_MSG_START);
    plen = addToBufferTCP(buf, plen, msg);
    plen = addToBufferTCP_P(buf, plen, JSON_STR_END);
    return plen;
}

uint16_t handleWebRequest(char *buf, uint16_t dataPointer, uint16_t dataLen) {
        DEBUG_p(PSTR("memory "));
        DEBUG_PRINTLN(getFreeMemory());

    DEBUG_p(PSTR("DATA LEN : "));
    DEBUG_PRINTLN(dataLen);
    DEBUG_PRINTLN(&buf[dataPointer]);

    uint16_t plen;
    if (dataLen >= 999) {
        plen = startResponseHeader(buf, HEADER_413);
        plen = appendErrorMsg_P(buf, plen, PSTR("Too big"));
        return plen;
    }

    if (criticalProblem_p) {
        plen = startResponseHeader(buf, HEADER_500);
        plen = appendErrorMsg_P(buf, plen, criticalProblem_p);
        return plen;
    }
    if (definitionError) {
        plen = startResponseHeader(buf, HEADER_500);
        plen = appendErrorMsg(buf, plen, definitionError);
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
        plen = startResponseHeader(buf, HEADER_404);
        plen = appendErrorMsg_P(buf, plen, PSTR("No resource for this method & url"));
    }
    return plen;
}
