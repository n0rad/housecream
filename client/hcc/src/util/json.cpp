#include "json.h"

// notifies : [{notifyValue: 42.4, notifyCondition : "sup_or_equal"}, {notifyValue.3, notifyCondition : "inf_or_equal"}]

static char findEndOfValue(char *buf) {
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

static prog_char *parseKeyValue(char **buffer, const t_json *structure) {
    char *buf =  *buffer;
    prog_char *keypos;
    if (buf[0] != '"') { // start of key
        return JSON_ERROR_NO_KEY_START;
    }
    buf = &buf[1]; // enter inside key
    boolean managed = false;
    for (uint8_t i = 0; (keypos = (prog_char *) pgm_read_word(&structure[i].key)); i++) {
        int keylen = strlen_P(keypos);
        if (strncmp_P(buf, keypos, keylen) == 0) {
            if (buf[keylen] != '"') {
                continue; // same key start but its not the buf full key name
            }
            managed = true;
            buf = &buf[keylen + 1]; // going out of key
            buf = skipSpaces(buf);
            if (buf[0] != ':') {
                return JSON_ERROR_NO_SEPARATOR;
            }
            buf = skipSpaces(&buf[1]); // skip separator ':' and white spaces
            if (buf[0] == '[') {
                return PSTR("nonested");
            } else if (buf[0] == '{') {
                return PSTR("nonested");
            } else { // call handleValue
                jsonHandleValue func;
                func = (jsonHandleValue) pgm_read_word(&structure[i].handleValue);
                char *res;
                int len;
                if (buf[0] == '"') {
//                    DEBUG_p(PSTR("value with quotes"));
                    buf = &buf[1];
                    len = my_strpos(buf, '"');
                    if (len == -1) {
                        return JSON_ERROR_NO_VALUE_END;
                    }
                    res = func(keypos, buf, len, 0);
                    DEBUG_PRINT(buf[0]);
                    DEBUG_PRINTLN(buf[1]); // seems to affect program
                    buf = &buf[len + 1];
                } else {
//                    DEBUG_p(PSTR("value without quotes"));
                    len = findEndOfValue(buf);
                    res = func(keypos, buf, len, 0);
                    buf = &buf[len + 1];
                }
                if (res) {
                    return res;
                }
                break;
            }
        }
    }
    if (!managed) {
        DEBUG_p(PSTR("unmanaged param"));
        buf = &buf[my_strpos(buf, '"') + 1]; // skip key
        buf = skipSpaces(buf);
        if (buf[0] != ':') {
            return JSON_ERROR_NO_SEPARATOR;
        }
        buf = skipSpaces(&buf[1]);
        if (buf[0] == '"') { // skip value with quotes
            buf = &buf[my_strpos(&buf[1], '"') + 2];
        } else if (buf[0] == '{') { // TODO manage nested object as the end may be the end of an inner obj
            buf = &buf[my_strpos(&buf[1], '}') + 2];
        } else if (buf[0] == '[') { // TODO manage nested array
            buf = &buf[my_strpos(&buf[1], ']') + 2];
        } else {
            buf = &buf[findEndOfValue(buf) + 1];
        }
    }
    *buffer = buf;
    return 0;
}

static prog_char *parseObject(char *buf, const t_json *structure) {
    buf = skipSpaces(buf);
    if (buf[0] != '{') {
        return JSON_ERROR_NO_OBJECT_START;
    }
    do {
        buf = skipSpaces(&buf[1]);  // skip { or ,
        char *fail = parseKeyValue(&buf, structure);
        if (fail) {
            return fail;
        }
        buf = skipSpaces(buf);
    } while (buf[0] == ',');
//    DEBUG_PRINTLN(buf[0]);
//    DEBUG_PRINTLN(buf[1]);
//    DEBUG_PRINTLN(buf[2]);
//    DEBUG_PRINTLN(buf[3]);
//    DEBUG_PRINTLN(buf[4]);
    if (buf[0] != '}') { // end of object
        return JSON_ERROR_NO_OBJECT_END;
    }
    return 0;
}


prog_char *jsonParse(char *buf, const t_json *structure) {
    return parseObject(buf, structure);
//    return 0;
}

