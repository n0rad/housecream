#include "src/config-manager.h"

const t_boardDescription boardDescription PROGMEM = {
    {0x54, 0x55, 0x58, 0x10, 0x00, 0xF5},          // mac
    "window1, not powered from POE but only by a transfo", // description
};

const t_pinDescription pinDescriptions[NUMBER_OF_PINS] PROGMEM = {
    {PIN_INPUT, PIN_ANALOG, "temperature captor for window1"},
    {PIN_NOTUSED, PIN_ANALOG, "temperature captor for window2"},
};

///////////

/** A copy of this struct is saved in EEPROM and can be modified through the rest API */
const t_config defaultConfig PROGMEM = {
    {
        {192, 168, 42, 245},                        // ip
        80,                                         // port
        "window1 controller",                       // name
    }, {
        "192.168.42.86:8080/hcs/ws/event",          // notify url (without http://)
    }, {
        "Windowtemp1",
        "Windowtemp2",
    }, {
        0,  {PIN_NOTIFY_OVER_EQ, 1},
        42, {PIN_NOTIFY_OVER_EQ, 1},
    },
    CONFIG_VERSION
};
