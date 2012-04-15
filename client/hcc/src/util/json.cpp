#include "json.h"
#include "../hcc.h"

// notifies : [{notifyValue: 42.4, notifyCondition : "sup_or_equal"}, {notifyValue.3, notifyCondition : "inf_or_equal"}]

static char findEndOfValue(char *buf) {
    for (uint8_t i; buf[i]; i++) {
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


static void DEBUG_p(const prog_char *progmem_s) {
    char c;
    while ((c = pgm_read_byte(progmem_s++))) {
        DEBUG_PRINT(c);
    }
    DEBUG_PRINTLN(" ");
}

static char *parseKeyValue(char **buffer, t_json *structure) {
    char *buf =  *buffer;
    char *keypos;
    if (buf[0] != '"') { // start of key
        return JSON_ERROR_NO_KEY_START;
    }
    buf = &buf[1]; // enter inside key
    for (uint8_t i = 0; (keypos = (char*) pgm_read_word(&structure[i].key)); i++) {
        int keylen = strlen_P(keypos);
        if (strncmp_P(buf, keypos, keylen) == 0) {
            if (buf[keylen] != '"') {
                continue; // same key start but its not the buf full key name
            }
            buf = &buf[keylen + 1]; // going out of key
            buf = skipSpaces(buf);
            if (buf[0] != ':') {
                return JSON_ERROR_NO_SEPARATOR;
            }
            buf = skipSpaces(&buf[1]); // skip separator ':' and white spaces

            if (buf[0] == '[') {
                return PSTR("nested array not implemented");
            } else if (buf[0] == '{') {
                return PSTR("nested object not implemented");
            } else { // call handleValue
                jsonHandleValue func;
                func = (jsonHandleValue) pgm_read_word(&structure[i].handleValue);
                char *res;
                int len;
                if (buf[0] == '"') {
                    DEBUG_PRINTLN("value with quotes");
                    buf = &buf[1];
                    len = my_strpos(buf, '"');
                    if (len == -1) {
                        return JSON_ERROR_NO_VALUE_END;
                    }
                    res = func(keypos, buf, len, 0);
                    buf = &buf[len + 1];
                } else {
                    DEBUG_PRINTLN("value without quotes");
                    len = findEndOfValue(buf);
                    res = func(keypos, buf, len, 0);
                    buf = &buf[len];
                }
                if (res) {
                    return res;
                }
                break;
            }
        }
    }
    *buffer = buf;
    return 0;
}

static char *parseObject(char *buf, t_json *structure) {
    buf = skipSpaces(buf);
    if (buf[0] != JSON_OBJECT_START) {
        return JSON_ERROR_NO_OBJECT_START;
    }
    do {
        buf = &buf[1]; // skip { or ,
        buf = skipSpaces(buf);

        char *fail = parseKeyValue(&buf, structure);
        if (fail) {
            return fail;
        }
        buf = skipSpaces(buf);
    } while (buf[0] == ',');

    if (buf[0] != '}') { // end of object
        return JSON_ERROR_NO_OBJECT_END;
    }
    return 0;
}


char *jsonParse(char *buf, t_json *structure) {
    return parseObject(buf, structure);
//    return 0;
}

