#ifndef JSON_H
#define JSON_H

#include <stdint.h>
#include <avr/pgmspace.h>

#include "../hcc.h"
#include "mylibc.h"


static char JSON_ERROR_NO_OBJECT_START[] PROGMEM = "Cannot find object start";
static char JSON_ERROR_NO_OBJECT_END[] PROGMEM = "Cannot find object end";
static char JSON_ERROR_NO_KEY_START[] PROGMEM = "Cannot find key start";
static char JSON_ERROR_NO_VALUE_END[] PROGMEM = "Cannot find value end";
static char JSON_ERROR_NO_SEPARATOR[] PROGMEM = "Cannot find key-value separator";

// notifies : [{notifyValue: 42.4, notifyCondition : "sup_or_equal"}, {notifyValue.3, notifyCondition : "inf_or_equal"}]

typedef prog_char *(*jsonHandleValue)(char *PGMkey, char *val, uint16_t valLen, uint8_t index);

typedef struct s_json {
    const prog_char *key;
    jsonHandleValue handleValue; // handle a value
    s_json *valueStruct;                           // handle an object
    uint8_t isArray;
} t_json;

prog_char *jsonParse(char *buf, t_json *structure);

#endif
