#ifndef JSON_H
#define JSON_H

#include <stdint.h>
#include <avr/pgmspace.h>

#include "../hcc.h"
#include "mylibc.h"

static char JSON_OBJECT_START  = '{';
static char JSON_OBJECT_END  = '}';
static char JSON_VALUE_SEPARATOR  = ':';
static char JSON_ELEM_SEPARATOR  = ',';
static char JSON_STR_WRAPPER  = '"';
static char JSON_ARRAY_START  = '[';
static char JSON_ARRAY_END  = ']';

static char JSON_ERROR_NO_OBJECT_START[] PROGMEM = "Cannot find object start";
static char JSON_ERROR_NO_OBJECT_END[] PROGMEM = "Cannot find object end";
static char JSON_ERROR_NO_KEY_START[] PROGMEM = "Cannot find key start";
static char JSON_ERROR_NO_VALUE_END[] PROGMEM = "Cannot find value end";
static char JSON_ERROR_NO_SEPARATOR[] PROGMEM = "Cannot find key-value separator";

// notifies : [{notifyValue: 42.4, notifyCondition : "sup_or_equal"}, {notifyValue.3, notifyCondition : "inf_or_equal"}]

typedef char *(*jsonHandleValue)(char *PGMkey, char *val, uint16_t valLen, uint8_t index);

typedef struct s_json {
    char *key;
    jsonHandleValue handleValue; // handle a value
    s_json *valueStruct;                           // handle an object
    char isArray;
} t_json;

char *jsonParse(char *buf, t_json *structure);

#endif
