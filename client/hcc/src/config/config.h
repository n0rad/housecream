#ifndef CONFIG_H_
#define CONFIG_H_

#include <avr/pgmspace.h>
#include "../pin/pin-manager.h"

///////////////////////////////////////////////////////////////
// BOARD
///////////////////////////////////////////////////////////////
#define CONFIG_BOARD_MAC_SIZE           6
#define CONFIG_BOARD_DESCRIPTION_SIZE   101
#define CONFIG_BOARD_IP_SIZE            4
#define CONFIG_BOARD_PORT_SIZE          sizeof(uint16_t)
#define CONFIG_BOARD_NOTIFY_SIZE        51
#define CONFIG_BOARD_NAME_SIZE          21

typedef struct s_boardDescription {
    uint8_t mac[CONFIG_BOARD_MAC_SIZE];
    uint8_t ip[CONFIG_BOARD_IP_SIZE];
    uint16_t port;
    prog_char name[CONFIG_BOARD_NAME_SIZE];
    prog_char description[CONFIG_BOARD_DESCRIPTION_SIZE];
    prog_char notifyurl[CONFIG_BOARD_NOTIFY_SIZE];
} t_boardDescription;

////////////////////////////////////////////////////////////
// PIN
///////////////////////////////////////////////////////////////
#define ANALOG 1
#define DIGITAL 2

#define OVER_EQ 1
#define UNDER_EQ 2

#define PIN_NUMBER_OF_NOTIFY 4

#define CONFIG_PIN_NAME_SIZE 21
#define CONFIG_PIN_DESCRIPTION_SIZE 101

typedef struct s_notify {
    uint8_t condition;
    float value; // value after conversion (this is why its a float)
} t_notify;

typedef struct s_pinInputDescription {
    int8_t pinId;           // unique pin id on board
    int8_t type;            // ANALOG, DIGITAL
    uint8_t pullup;         // enable internal pullup resistor
    prog_char name[CONFIG_PIN_NAME_SIZE];
    t_notify notifies[PIN_NUMBER_OF_NOTIFY];
    PinInputConversion convertValue; // convert the 0-1023 to a display value (ex: float for temperature)
    PinRead read;           // function to read value
    prog_char description[CONFIG_PIN_DESCRIPTION_SIZE];
} t_pinInputDescription;


typedef struct s_pinOutputDescription {
    int8_t pinId;         // unique pin id on board
    int8_t type;          // ANALOG, DIGITAL
    prog_char name[CONFIG_PIN_NAME_SIZE];
    float valueMin;       // for output pin : min value as input for transform function that will not result under 0 (display value)
    float valueMax;       // for output pin : max value as input for transform function that will not result over 255 (display value)
    float startValue;     // output start value the first time the board run (use saved state in eeprom in later boot)
    PinOutputConversion convertValue; // convert the display value to a 0-255 value
    PinWrite write;       // function to write value
    prog_char description[CONFIG_PIN_DESCRIPTION_SIZE];
} t_pinOutputDescription;


extern const t_pinInputDescription pinInputDescription[];
extern const t_pinOutputDescription pinOutputDescription[];
extern const t_boardDescription boardDescription;

#endif
