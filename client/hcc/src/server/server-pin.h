#ifndef SERVER_PIN_H
#define SERVER_PIN_H

#include <avr/pgmspace.h>
#include <stdio.h>

#include "../hcc.h"
#include "../settings/settings-pin.h"

uint16_t pinGet(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource);
uint16_t pinPut(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource);
uint16_t pinPutValue(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource);
uint16_t pinGetValue(char *buf, uint16_t dat_p, uint16_t plen, t_webRequest *webResource);

#include "server.h"


const prog_char PIN_MIN[] PROGMEM = "\",\"valueMin\":";
const prog_char PIN_MAX[] PROGMEM = ",\"valueMax\":";

const prog_char PIN_PARAM_ID[] PROGMEM = "id";
const prog_char PIN_PARAM_DIRECTION[] PROGMEM = "direction";
const prog_char PIN_PARAM_TYPE[] PROGMEM = "type";
const prog_char PIN_PARAM_VALUEMIN[] PROGMEM = "valueMin";
const prog_char PIN_PARAM_VALUEMAX[] PROGMEM = "valueMax";
const prog_char PIN_PARAM_NOTIFIES[] PROGMEM = "notifies";
const prog_char PIN_PARAM_NOTIFY_COND[] PROGMEM = "notifyCondition";
const prog_char PIN_PARAM_NOTIFY_VALUE[] PROGMEM = "notifyValue";
const prog_char PIN_PARAM_VALUE[] PROGMEM = "value";
const prog_char PIN_PARAM_STARTVALUE[] PROGMEM = "startValue";

const t_json pinPutNotifiesElements[] PROGMEM = {
        {PIN_PARAM_NOTIFY_COND, setConfigPinNotifyCond},
        {PIN_PARAM_NOTIFY_VALUE, setConfigPinNotifyValue},
        {0, 0}
};

const t_json pinPutElements[] PROGMEM = {
        {PIN_PARAM_ID, setConfigPinId},
        {PARAM_NAME, setConfigPinName},
        {PARAM_DESCRIPTION, setConfigPinDescription},
        {PIN_PARAM_DIRECTION, setConfigPinDirection},
        {PIN_PARAM_TYPE, setConfigPinType},
        {PIN_PARAM_VALUEMIN, setConfigPinValueMin},
        {PIN_PARAM_VALUEMAX, setConfigPinValueMax},
        {PIN_PARAM_NOTIFIES, 0, (t_json *) pinPutNotifiesElements, handlePinNotifyEndArray}, // array of objects
        {PIN_PARAM_VALUE, setConfigPinValue},
        {0, 0}
};

const t_json pinPutObj PROGMEM = {0, 0, (t_json *)pinPutElements, 0};

#endif
