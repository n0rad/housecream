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
        {PIN_PARAM_NOTIFY_COND, settingsPinSetNotifyCond},
        {PIN_PARAM_NOTIFY_VALUE, settingsPinSetNotifyValue},
        {0, 0}
};

const t_json pinPutElements[] PROGMEM = {
        {PIN_PARAM_ID, settingsPinSetId},
        {PARAM_NAME, settingsPinSetName},
        {PARAM_DESCRIPTION, settingsPinSetDescription},
        {PIN_PARAM_DIRECTION, settingsPinSetDirection},
        {PIN_PARAM_TYPE, settingsPinSetType},
        {PIN_PARAM_VALUEMIN, settingsPinSetValueMin},
        {PIN_PARAM_VALUEMAX, settingsPinSetValueMax},
        {PIN_PARAM_NOTIFIES, 0, (t_json *) pinPutNotifiesElements, settingsPinHandlePinNotifyArray}, // array of objects
//        {PIN_PARAM_VALUE, settingsPinSetValue},
        {0, 0}
};

const t_json pinPutObj PROGMEM = {0, 0, (t_json *)pinPutElements, 0};

#endif
