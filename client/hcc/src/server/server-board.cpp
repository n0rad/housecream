#include "server-board.h"

uint16_t boardGet(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource) {
    plen = startResponseHeader(&buf, HEADER_200);
    plen = addToBufferTCP_P(buf, plen, PSTR("{\"software\":\"HouseCream Client\", \"version\":\""));
    plen = addToBufferTCP_P(buf, plen, hcc_version);

    plen = addToBufferTCP_P(buf, plen, PSTR("\",\"hardware\":\""));
    plen = addToBufferTCP_P(buf, plen, PSTR(HARDWARE));

    plen = addToBufferTCP_P(buf, plen, JSON_NAME);
    plen = addToBufferTCP_E(buf, plen, settingsBoardGetName_E());

    plen = addToBufferTCP_P(buf, plen, JSON_DESCRIPTION);
    plen = addToBufferTCP_P(buf, plen, boardDescription.description);

    plen = addToBufferTCP_P(buf, plen, PSTR("\",\"notifyUrl\":\""));
    plen = addToBufferTCP_E(buf, plen, settingsBoardGetNotifyUrl_E());

    uint8_t ip[4];
    settingsBoardGetIP(ip);
    plen = addToBufferTCP_P(buf, plen, PSTR("\",\"ip\":\""));
    plen = addToBufferTCP(buf, plen, (uint16_t) ip[0]);
    plen = addToBufferTCP(buf, plen, '.');
    plen = addToBufferTCP(buf, plen, (uint16_t) ip[1]);
    plen = addToBufferTCP(buf, plen, '.');
    plen = addToBufferTCP(buf, plen, (uint16_t) ip[2]);
    plen = addToBufferTCP(buf, plen, '.');
    plen = addToBufferTCP(buf, plen, (uint16_t) ip[3]);

    plen = addToBufferTCP_P(buf, plen, PSTR("\",\"port\":"));
    plen = addToBufferTCP(buf, plen, settingsBoardGetPort());

    plen = addToBufferTCP_P(buf, plen, PSTR(",\"freeMemory\":"));
    plen = addToBufferTCP(buf, plen, (uint16_t) getFreeMemory());

    plen = addToBufferTCP_P(buf, plen, PSTR(",\"pinIds\":["));
    uint8_t i = 0;
    for (; i < pinInputSize; i++) {
        if (i) {
            plen = addToBufferTCP(buf, plen, ',');
        }
        plen = addToBufferTCP(buf, plen, (uint16_t) pgm_read_byte(&pinInputDescription[i].pinId));
    }
    for (uint8_t j = 0; j < pinOutputSize; j++) {
        if (i || j) {
            plen = addToBufferTCP(buf, plen, ',');
        }
        plen = addToBufferTCP(buf, plen, (uint16_t) pgm_read_byte(&pinOutputDescription[j].pinId));
    }
    plen = addToBufferTCP(buf, plen, ']');

    uint8_t mac[6];
    configBoardGetMac(mac);
    plen = addToBufferTCP_P(buf, plen, PSTR(",\"mac\":\""));
    plen = addToBufferTCPHex(buf, plen, mac[0]);
    plen = addToBufferTCP(buf, plen, ':');
    plen = addToBufferTCPHex(buf, plen, mac[1]);
    plen = addToBufferTCP(buf, plen, ':');
    plen = addToBufferTCPHex(buf, plen, mac[2]);
    plen = addToBufferTCP(buf, plen, ':');
    plen = addToBufferTCPHex(buf, plen, mac[3]);
    plen = addToBufferTCP(buf, plen, ':');
    plen = addToBufferTCPHex(buf, plen, mac[4]);
    plen = addToBufferTCP(buf, plen, ':');
    plen = addToBufferTCPHex(buf, plen, mac[5]);

    plen = addToBufferTCP_P(buf, plen, JSON_STR_END);
    return plen;
}

uint16_t boardPut(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource) {
    const prog_char *error = jsonParse(&buf[dat_p], &boardPutObj);
    if (error) {
        plen = startResponseHeader(&buf, HEADER_400);
        plen = appendErrorMsg_P(buf, plen, error);
    } else {
        plen = startResponseHeader(&buf, HEADER_200);
    }
    return plen;
}

uint16_t boardReset(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource) {
    needReboot = true;
    plen = startResponseHeader(&buf, HEADER_200);
    return plen;
}

uint16_t boardReInit(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource) {
    settingsSave();
    needReboot = true;
    plen = startResponseHeader(&buf, HEADER_200);
    return plen;
}

uint16_t boardNotify(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource) {
    plen = startResponseHeader(&buf, HEADER_200);
    clientBoardNotify(BOARD_NOTIFY_NOTIF);
    return plen;
}
