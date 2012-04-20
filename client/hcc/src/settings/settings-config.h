#ifndef SETTINGS_CONFIG_H
#define SETTINGS_CONFIG_H

#include <avr/pgmspace.h>

#include "../../config.h"

#define CONFIG_VERSION "hcc0"

///////////////////////////////////////////////////////////////
// BOARD
///////////////////////////////////////////////////////////////
#define CONFIG_BOARD_MAC_SIZE sizeof(uint8_t) * 6
#define CONFIG_BOARD_DESCRIPTION_SIZE sizeof(char) * 101
#define CONFIG_BOARD_IP_SIZE sizeof(uint8_t) * 4
#define CONFIG_BOARD_PORT_SIZE sizeof(uint16_t)
#define CONFIG_BOARD_NOTIFY_SIZE sizeof(char) * 51
#define CONFIG_BOARD_NAME_SIZE sizeof(char) * 21

typedef struct s_boardDescription {
    uint8_t mac[6];
    char description[CONFIG_BOARD_DESCRIPTION_SIZE];
} t_boardDescription;

typedef struct s_boardInfo {
    uint8_t ip[4];
    uint16_t port;
    char name[CONFIG_BOARD_NAME_SIZE];
} t_boardInfo;

typedef struct s_boardData {
    char notifyurl[CONFIG_BOARD_NOTIFY_SIZE];
} t_boardData;

///////////////////////////////////////////////////////////////
// PIN
///////////////////////////////////////////////////////////////
#define PIN_INPUT 0
#define PIN_OUTPUT 1
#define PIN_NOTUSED 2
#define PIN_RESERVED 3

#define PIN_ANALOG 1
#define PIN_DIGITAL 2

#define PIN_NOTIFY_OVER_EQ 1
#define PIN_NOTIFY_UNDER_EQ 2

#define PIN_NUMBER_OF_NOTIFY 4

typedef struct s_notify {
    char condition;
    uint16_t value;
} t_notify;

#include "../pin/pin-manager.h"

typedef struct s_pinDescription {
    int8_t direction;     // PIN_INPUT, PIN_OUTPUT, PIN_NOTUSED, PIN_RESERVED
    char type;          // PIN_ANALOG, PIN_DIGITAL
    uint16_t valueMin;  // for input pin : min value as input for transform function (usually 0)
                        // for output pin : min value as input for transform function that will not result under 0
    uint16_t valueMax;  // for input pin : max value as input for transform function (usually 1023)
                        // for output pin : max value as input for transform function that will not result over 255
    PinValueConversion convertValue; // for input pin : convert the 0-1023 to the wanted display value
                                     // for output pin : convert the display value to 0-255
    PinRead read; // (used for input pin only)
    PinWrite write; // (used for output pin only)
    char description[101];
} t_pinDescription;

typedef struct s_pinInfo {
    char name[21];
} t_pinInfo;

typedef struct s_pinData {
    uint16_t lastValue; // output only
    t_notify notifies[PIN_NUMBER_OF_NOTIFY];
} t_pinData;

///////////////////////////////////////////////////////////////
// STRUCTURE OF EEPROM
///////////////////////////////////////////////////////////////
typedef struct s_config {
   t_boardInfo boardInfo;
   t_boardData boardData;
   t_pinInfo pinInfos[NUMBER_OF_PINS];
   t_pinData pinData[NUMBER_OF_PINS];
   char version[5];
} t_config;

/* User configuration */
extern const t_config defaultConfig;
extern const t_boardDescription boardDescription;
extern const t_pinDescription pinDescriptions[NUMBER_OF_PINS];

#endif
