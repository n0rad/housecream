#include "server.h"

uint16_t startResponseHeader(char **buf, const prog_char *codeMsg) {
    *buf = &((*buf)[TCP_CHECKSUM_L_P + 3]);
    uint16_t plen;
    plen = addToBufferTCP_P(*buf, 0, HEADER_HTTP);
    plen = addToBufferTCP_P(*buf, plen, codeMsg);
    plen = addToBufferTCP_P(*buf, plen, HEADER_CONTENT);
    plen = addToBufferTCP_P(*buf, plen, DOUBLE_ENDL);
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

ResourceFunc currentFunc = 0;
uint8_t currentPinId = 0;


uint16_t handleWebRequest(char *buf, uint16_t dataPointer, uint16_t dataLen) {
    DEBUG_p(PSTR("memory "));
    DEBUG_PRINTLN(getFreeMemory());

    DEBUG_p(PSTR("DATA :"));
    DEBUG_PRINTLN(&buf[dataPointer]);

    uint16_t plen;
    if (dataLen >= 999) {
        plen = startResponseHeader(&buf, HEADER_413);
        plen = appendErrorMsg_P(buf, plen, PSTR("Too big"));
        return plen;
    }

    if (criticalProblem_p) {
        plen = startResponseHeader(&buf, HEADER_500);
        plen = appendErrorMsg_P(buf, plen, criticalProblem_p);
        return plen;
    }
    if (definitionError) {
        plen = startResponseHeader(&buf, HEADER_500);
        plen = appendErrorMsg(buf, plen, definitionError);
        return plen;
    }
    DEBUG_PRINTLN('G');

    if (currentFunc != 0) { // we were waiting for data packet
        DEBUG_PRINTLN("WAITDATARECEIVED");
        plen = currentFunc(buf, dataPointer, dataLen, currentPinId);
        currentFunc = 0;
        return plen;
    }

    DEBUG_PRINTLN('H');

    prog_char *methodPos;
    int i = 0;
    for (; (methodPos = (prog_char *) pgm_read_word(&resources[i].method)); i++) {
        uint16_t querylen = strlen_P((prog_char *) pgm_read_word(&resources[i].query));
        prog_char *queryPos = (prog_char *) pgm_read_word(&resources[i].query);
        if (strncmp_P((char *) &(buf[dataPointer]), methodPos, 4) == 0
                && strncmp_P((char *) & (buf[dataPointer + 4]), queryPos, querylen) == 0) {

            prog_char *suffixPos = (prog_char *) pgm_read_word(&resources[i].suffix);
            if (' ' == pgm_read_byte(&queryPos[querylen - 1])) {
               break;
            } else {
                currentPinId = atoi(&buf[dataPointer + 4 + querylen]);
                uint8_t j = dataPointer + 4 + querylen;
                for (; buf[j] >= '0' && buf[j] <= '9'; j++);
                if (suffixPos == 0 && (buf[j] == ' ' || (buf[j] == '/' && buf[j + 1] == ' '))) {
                    currentFunc = (ResourceFunc) pgm_read_word(&resources[i].resourceFunc);
                    break;
                } else if (strncmp_P((char *) & (buf[j]), suffixPos, strlen_P(suffixPos)) == 0) {
                    currentFunc = (ResourceFunc) pgm_read_word(&resources[i].resourceFunc);
                    break;
                }
            }
        }
    }

    if (!methodPos) {
        DEBUG_PRINTLN('A');
        plen = startResponseHeader(&buf, HEADER_404);
        plen = appendErrorMsg_P(buf, plen, PSTR("No resource for this method & url"));
    } else {
        currentFunc = (ResourceFunc) pgm_read_word(&resources[i].resourceFunc);
        if (methodPos == GET) { // GET do not need data, calling func directly
            DEBUG_PRINTLN('B');
            plen = currentFunc((char*)buf, 0, dataLen, 0);
        } else {
            DEBUG_PRINTLN('I');
            uint16_t endPos = strstrpos_P(&buf[dataPointer], DOUBLE_ENDL);
            if (endPos == -1) {
                DEBUG_PRINTLN('C');
                plen = startResponseHeader(&buf, HEADER_400);
                plen = appendErrorMsg_P(buf, plen, PSTR("Double endl not found"));
            } else {
                uint16_t dataStartPos = dataPointer + endPos + 4;
                if (dataStartPos == dataLen) { // NO DATA IN THIS PACKET
                    DEBUG_PRINTLN('D');
                    return 0;
                } else if (currentPinId < 0 || currentPinId > NUMBER_OF_PINS - 1) {
                    DEBUG_PRINTLN('E');
                    plen = startResponseHeader(&buf, HEADER_400);
                    plen = appendErrorMsg_P(buf, plen, PSTR("PinId overflow"));
                } else {
                    DEBUG_PRINTLN('F');
                    plen = currentFunc((char *)buf, dataStartPos, dataLen, currentPinId);
                }
            }
        }
    }
    currentFunc = 0;
    return plen;
}
