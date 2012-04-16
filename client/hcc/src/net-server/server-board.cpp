#include "../hcc.h"
#include <string.h>

uint16_t boardGet(char *buf, uint16_t dat_p, uint16_t plen) {
    plen = addToBufferTCP_p(buf, 0, HEADER_200);
    plen = addToBufferTCP_p(buf, plen, PSTR("{\"software\":\"HouseCream Client\", \"version\":\""));
    plen = addToBufferTCP_p(buf, plen, PSTR(HCC_VERSION));

    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"hardware\":\""));
    plen = addToBufferTCP_p(buf, plen, PSTR(HARDWARE));

    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"name\":\""));
    plen = addToBufferTCP_e(buf, plen, getConfigBoardName_e());

    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"technicalDescription\":\""));
    plen = addToBufferTCP_p(buf, plen, boardDescription.technicalDescription);

    plen = addToBufferTCP_p(buf, plen, PSTR("\",\"notifyUrl\":\""));
    plen = addToBufferTCP_p(buf, plen, getConfigNotifyUrl());

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

//for (int i = 0; i < plen; i++) {
//    if (buf[i] >= ' ' && buf[i] <= '~') {
//        DEBUG_PRINT(buf[i]);
//    } else {
//        DEBUG_PRINT(" ");
//        DEBUG_PRINT((int)buf[i]);
//        DEBUG_PRINT(" ");
//    }
//}
//DEBUG_PRINTLN(" ");

char *sampleHandler(char* PGMkey, char *buf, uint16_t len, uint8_t index) {
    DEBUG_PRINT("HERE>>");
    for (int i = 0; i < len; ++i) {
        DEBUG_PRINT(buf[i]);
    }
    DEBUG_PRINTLN(" ");
    return 0;
}

static char BOARDPUT_PARAM_NAME[] PROGMEM = "name";
static char BOARDPUT_PARAM_NOTIFYURL[] PROGMEM = "notifyUrl";
static char BOARDPUT_PARAM_TECHNICALDESC[] PROGMEM = "technicalDescription";
static char BOARDPUT_PARAM_IP[] PROGMEM = "ip";
static char BOARDPUT_PARAM_PORT[] PROGMEM = "port";

t_json boardPutElements[] PROGMEM = {
        {BOARDPUT_PARAM_NAME, setConfigBoardName},
        {BOARDPUT_PARAM_NOTIFYURL, sampleHandler},
        {BOARDPUT_PARAM_TECHNICALDESC, sampleHandler},
        {BOARDPUT_PARAM_IP, sampleHandler},
        {BOARDPUT_PARAM_PORT, sampleHandler},
        {0, 0}
};

uint16_t boardPut(char *buf, uint16_t dat_p, uint16_t plen) {
    char *data = &strstr(&buf[dat_p], DOUBLE_ENDL)[4];
    char *error = jsonParse(data, boardPutElements);
    if (error) {
        plen = addToBufferTCP_p(buf, 0, HEADER_400);
        plen = addToBufferTCP_p(buf, plen, PSTR("{\"message\":\""));
        plen = addToBufferTCP_p(buf, plen, error);
        plen = addToBufferTCP_p(buf, plen, PSTR("\"}"));
    } else {
        plen = addToBufferTCP_p(buf, 0, HEADER_200);
    }
    return plen;
}
