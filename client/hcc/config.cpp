#include "src/config.h"

float tempConversion(float value) {
    return value * 50 / 1023;
}

const t_boardDescription boardDescription PROGMEM = {
    {0x54, 0x55, 0x58, 0x10, 0x00, 0xF5},           // mac
    {192, 168, 42, 245},                            // ip
    80,                                             // port
    "window1 controller",                           // name
    "window in front of the house not powered from POE but only by a transfo",   // description
    "http://192.168.12.2:8080/hcs/ws/event",       // notify url
};

// INPUT
const t_pinInputDescription pinInputDescription[] PROGMEM = {
        {1, DIGITAL, "door1 open captor", {{OVER_EQ, 1},{UNDER_EQ, 0},{0,0},{0,0}}, noInputConversion, defaultPinRead, "magnetic captor in the upper part"},
        {2, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {3, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {4, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {5, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {6, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {7, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {8, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {9, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {10, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {11, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {12, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {13, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {14, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {15, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {16, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {17, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
//        {18, ANALOG, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {-1}
};

// OUTPUT
const t_pinOutputDescription pinOutputDescription[] PROGMEM = {
        {0, ANALOG, "variator for light 1", 0, 255, 255, noOutputConversion, defaultPinWrite, "optocoupler isolated and triac / no zero detection"},
        {19, ANALOG, "variator for light 1", 0, 255, 255, noOutputConversion, defaultPinWrite, "optocoupler isolated and triac / no zero detection"},
        {-1}
};
