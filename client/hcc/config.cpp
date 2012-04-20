#include "src/config-manager.h"

const t_boardDescription boardDescription PROGMEM = {
    {0x54, 0x55, 0x58, 0x10, 0x00, 0xF5},           // mac
    "window in front of the house not powered from POE but only by a transfo",   // description
};

const t_pinDescription pinDescriptions[NUMBER_OF_PINS] PROGMEM = {
/* 0*/  {PIN_INPUT, PIN_DIGITAL, 0, 1, noConversion, defaultPinRead, defaultPinWrite, "switch number 1"},
/* 1*/  {PIN_INPUT, PIN_DIGITAL, 0, 1023, noConversion, defaultPinRead, defaultPinWrite, "temperature captor for window2"},
/* 2*/  {PIN_NOTUSED, PIN_DIGITAL, 0, 1, noConversion, defaultPinRead, defaultPinWrite, ""},
/* 3*/  {PIN_INPUT, PIN_ANALOG, 0, 1, noConversion, defaultPinRead, defaultPinWrite, ""},
/* 4*/  {PIN_OUTPUT, PIN_DIGITAL, 0, 1, noConversion, defaultPinRead, defaultPinWrite, ""},
/* 5*/  {PIN_INPUT, PIN_ANALOG, 0, 1, noConversion, defaultPinRead, defaultPinWrite, ""},
/* 6*/  {PIN_OUTPUT, PIN_ANALOG, 0, 1, noConversion, defaultPinRead, defaultPinWrite, ""},
/* 7*/  {PIN_INPUT, PIN_DIGITAL, 0, 1, noConversion, defaultPinRead, defaultPinWrite, ""},
/* 8*/  {PIN_INPUT, PIN_DIGITAL, 0, 1, noConversion, defaultPinRead, defaultPinWrite, ""},
/* 9*/  {PIN_OUTPUT, PIN_DIGITAL, 0, 1, noConversion, defaultPinRead, defaultPinWrite, ""},
/*10*/  {PIN_RESERVED, 0, 0, 1, noConversion, defaultPinRead, defaultPinWrite, ""},
/*11*/  {PIN_RESERVED, 0, 0, 1, noConversion, defaultPinRead, defaultPinWrite, ""},
/*12*/  {PIN_RESERVED, 0, 0, 1, noConversion, defaultPinRead, defaultPinWrite, ""},
/*13*/  {PIN_RESERVED, 0, 0, 1, noConversion, defaultPinRead, defaultPinWrite, ""},
/*14*/  {PIN_OUTPUT, PIN_ANALOG, 0, 1023, noConversion, defaultPinRead, defaultPinWrite, ""},
/*15*/  {PIN_INPUT, PIN_ANALOG, 0, 1023, noConversion, defaultPinRead, defaultPinWrite, ""},
/*16*/  {PIN_INPUT, PIN_ANALOG, 0, 1023, noConversion, defaultPinRead, defaultPinWrite, ""},
/*17*/  {PIN_INPUT, PIN_ANALOG, 0, 1023, noConversion, defaultPinRead, defaultPinWrite, ""},
/*18*/  {PIN_INPUT, PIN_ANALOG, 0, 1023, noConversion, defaultPinRead, defaultPinWrite, ""},
/*19*/  {PIN_INPUT, PIN_ANALOG, 0, 1023, noConversion, defaultPinRead, defaultPinWrite, ""},
};

///////////

/** A copy of this struct is saved in EEPROM and can be modified through the rest API */
const t_config defaultConfig PROGMEM = {
    {
        {192, 168, 42, 245},                            // ip
        80,                                             // port
        "window1 controller",                           // name
    },
    {
        "http://192.168.42.86:8080/hcs/ws/event",       // notify url
    },
    {
/* 0*/  "pin0",                                  // pin names
/* 1*/  "pin1",
/* 2*/  "pin2",
/* 3*/  "pin3",
/* 4*/  "pin4",
/* 5*/  "pin5",
/* 6*/  "pin6",
/* 7*/  "pin7",
/* 8*/  "pin8",
/* 9*/  "pin9",
/*10*/  "pin10",
/*11*/  "pin11",
/*12*/  "pin12",
/*13*/  "pin13",
/*14*/  "pin14",
/*15*/  "pin15",
/*16*/  "pin16",
/*17*/  "pin16",
/*18*/  "pin16",
/*19*/  "pin16",
    }, {
/* 0*/  0,  {PIN_NOTIFY_OVER_EQ, 1},                    // pin notify Condition
/* 1*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/* 2*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/* 3*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/* 4*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/* 5*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/* 6*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/* 7*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/* 8*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/* 9*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/*10*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/*11*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/*12*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/*13*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/*14*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/*15*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/*16*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/*17*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/*18*/  42, {PIN_NOTIFY_OVER_EQ, 1},
/*19*/  42, {PIN_NOTIFY_OVER_EQ, 1},
    },
    CONFIG_VERSION
};
