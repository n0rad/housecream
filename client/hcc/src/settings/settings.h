#ifndef SETTINGS_H
#define SETTINGS_H

#include <stdlib.h>
#include <stdint.h>
#include <avr/pgmspace.h>
#include <avr/eeprom.h>

#include "../config/config.h"
#include "../hcc.h"
#include "../pin/pin-manager.h"
#include "../util/mylibc.h"


const prog_char IP_CHARACTERS[] PROGMEM = "0123456789.";

const prog_char TOO_MANY_NOTIFY[] PROGMEM = "Too many notify";
const prog_char NO_NOTIFY_OUTPUT[] PROGMEM = "No notify on output";
const prog_char CANNOT_SET_MIN_VAL[] PROGMEM = "valueMin cannot be set";
const prog_char CANNOT_SET_MAX_VAL[] PROGMEM = "valueMax cannot be set";
const prog_char DESCRIPTION_CANNOT_BE_SET[] PROGMEM = "description cannot be set";
const prog_char NAME_TOO_LONG[] PROGMEM = "name is too long";
const prog_char NOTIFY_VAL_OVERFLOW[] PROGMEM = "notify val overflow on pin%d";
const prog_char PIN_DEFINE_TWICE[] PROGMEM = "pin%d is define twice";
const prog_char PIN_TYPE_INVALID[] PROGMEM = "invalid type on pin%d";
const prog_char PIN_START_INVALID[] PROGMEM = "invalid start value for pin%d";
const prog_char PIN_MIN_INVALID[] PROGMEM = "invalid min value for pin%d";
const prog_char PIN_MAX_INVALID[] PROGMEM = "invalid max value for pin%d";

void settingsLoad();
void settingsReload();
void settingsSave();

const prog_char CONFIG_VERSION[] PROGMEM = "hc1";
const prog_char STR_INPUT[] PROGMEM = "INPUT";
const prog_char STR_OUTPUT[] PROGMEM = "OUTPUT";
const prog_char PIN_TYPE_ANALOG[] PROGMEM = "ANALOG";
const prog_char PIN_TYPE_DIGITAL[] PROGMEM = "DIGITAL";
const prog_char PIN_NOTIFICATION_SUP[] PROGMEM = "SUP_OR_EQUAL";
const prog_char PIN_NOTIFICATION_INF[] PROGMEM = "INF_OR_EQUAL";


extern uint8_t pinInputSize;
extern uint8_t pinOutputSize;
extern const char *pinDirection[];
extern const char *pinType[];
extern const char *pinNotification[];

extern uint8_t NotifyDstIp[4];
extern uint16_t notifyDstPort;
extern char notifyUrlPrefix[36];
extern t_notify **pinNotifies;


/////////////////////////////////////////////////////////////
// EEPROM structure
/////////////////////////////////////////////////////////////
typedef struct s_boardSettings {
    uint8_t ip[CONFIG_BOARD_IP_SIZE];
    uint16_t port;
    char name[CONFIG_BOARD_NAME_SIZE];
    char notifyUrl[CONFIG_BOARD_NOTIFY_SIZE];
} t_boardSettings;

typedef struct s_pinInputSettings {
    prog_char name[CONFIG_PIN_NAME_SIZE];
    t_notify notifies[PIN_NUMBER_OF_NOTIFY];
} t_pinInputSettings;

typedef struct s_pinOutputSettings {
    char name[CONFIG_PIN_NAME_SIZE];
    float lastValue;
} t_pinOutputSettings;

#ifdef DOC_ONLY // eeprom structure that is not real because input and output size are dynamic
typedef struct s_settings {
    t_boardSettings boardSettings;
    t_pinInputSettings inputSettings[]; // array of size of pinInputDescription determined at runtime
    t_pinOutputSettings outputSettings[]; // array of size of pinOutputDescription determined at runtime
    char version[4];
} t_settings;
#endif


#endif
