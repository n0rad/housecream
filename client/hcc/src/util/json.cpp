#include "json.h"

const prog_char *jsonParseValue(char **buffer, const t_json *currentStructure, uint8_t index);


static uint8_t findEndOfValue(char *buf) {
    for (uint8_t i = 0; buf[i]; i++) {
        if (buf[i] == '\t' || buf[i] == '\n' || buf[i] == '\r' || buf[i] == ' '
                || buf[i] == ']' || buf[i] == '}' || buf[i] == ',') {
            return i;
        }
    }
    return -1;
}

static char *skipSpaces(char *buf) {
    while (buf) {
        if (buf[0] != '\t' && buf[0] != '\n' && buf[0] != '\r' && buf[0] != ' ') {
            return &buf[0];
        }
        buf = &buf[1];
    }
    return buf;
}

static const prog_char *jsonParseObject(char **buffer, const t_json *structureList, uint8_t index) {
    char *buf =  *buffer;
    prog_char *keypos;
    do {
        buf = skipSpaces(&buf[1]); // skip '{' and then ',' on each loop

        if (buf[0] != '"') { // start of key
//            DEBUG_PRINT("CCC");
//            DEBUG_PRINT(buf[0]);
//            DEBUG_PRINTLN(buf[1]);
            return JSON_ERROR_NO_KEY_START;
        }
        buf = &buf[1]; // enter inside key
        const t_json *currentStructure = 0;
        for (uint8_t i = 0; (keypos = (prog_char *) pgm_read_word(&structureList[i].key)); i++) {
            int keylen = strlen_P(keypos);
            if (strncmp_P(buf, keypos, keylen) == 0) {
                if (buf[keylen] != '"') {
                    continue; // same key start but its not the buf full key name
                }
                currentStructure = &structureList[i];
                break;
            }
        }
        buf = strchr(buf, '"'); // going to the end of key
        buf = skipSpaces(&buf[1]);

        if (buf[0] != ':') {
            return JSON_ERROR_NO_SEPARATOR;
        }
        buf = skipSpaces(&buf[1]); // skip separator ':' and white spaces
        const prog_char *res = jsonParseValue(&buf, currentStructure, index);
        if (res) {
            return res;
        }
        buf = skipSpaces(buf);
    } while (buf[0] == ',');
    buf = skipSpaces(buf);
    if (buf[0] != '}') { // end of object
//        DEBUG_PRINT("EEE");
//        DEBUG_PRINT(buf[0]);
//        DEBUG_PRINTLN(buf[1]);
        return JSON_ERROR_NO_OBJECT_END;
    }
    buf = skipSpaces(&buf[1]); // going out of object
    *buffer = buf;
    return 0;
}

static const prog_char *jsonParseArray(char **buffer, const t_json *currentStructure) {
    char *buf =  *buffer;
    jsonHandleEndArray endFunc = (jsonHandleEndArray) pgm_read_word(&currentStructure->handleEndArray);
    if (currentStructure && !endFunc) {
        return PSTR("Unexpected array");
    }
    uint8_t count = 0;
    do {
        buf = skipSpaces(&buf[1]); // skip '[' and then ',' on each loop
        const prog_char *res = jsonParseValue(&buf, currentStructure, count);
        if (res) {
            return res;
        }
//        DEBUG_PRINT(">>>S>");
//        DEBUG_PRINT(buf[0]);
//        DEBUG_PRINTLN(buf[1]);
        buf = skipSpaces(buf);
        count++;
    } while (buf[0] == ',');
    buf = skipSpaces(buf);
    if (buf[0] != ']') { // end of object
        return JSON_ERROR_NO_ARRAY_END;
    }

    const prog_char *res = endFunc(count);
    if (res) {
        return res;
    }
    buf = skipSpaces(&buf[1]); // going out of array
    *buffer = buf;
    return 0;
}

const prog_char *jsonParseValue(char **buffer, const t_json *currentStructure, uint8_t index) {
    char *buf =  *buffer;
    buf = skipSpaces(buf);

//    DEBUG_PRINT("STARTING value parse :");
//    DEBUG_PRINT(buf[0]);
//    DEBUG_PRINTLN(buf[1]);
    if (buf[0] == '[') {
//        DEBUG_PRINT("found array");
        const prog_char *res = jsonParseArray(&buf, currentStructure);
        if (res) {
            return res;
        }
    } else if (buf[0] == '{') {
//        DEBUG_PRINTLN("found OBJ");
        const t_json *objStruct = (t_json *) pgm_read_word(&currentStructure->valueStruct);
        const prog_char *res = jsonParseObject(&buf, objStruct, index);
        if (res) {
            return res;
        }
    } else { // parsing string or num value
        jsonHandleValue func = 0;
        if (currentStructure != 0) {
            func = (jsonHandleValue) pgm_read_word(&currentStructure->handleValue);
        }
        uint16_t len;
        const prog_char *res = 0;
        if (buf[0] == '"') {
            buf = &buf[1];
            len = my_strpos(buf, '"');
            if (func) {
                res = func(buf, len, index);
            }
            buf = &buf[len + 1];
        } else {
            len = findEndOfValue(buf);
            if (func) {
                res = func(buf, len, index);
            }
            buf = &buf[len];
        }
        if (res) {
            return res;
        }
    }
    *buffer = buf;
    return 0;
}

const prog_char  *jsonParse(char *buf, const t_json *currentStructure) {
    return jsonParseValue(&buf, currentStructure, 0);
}
