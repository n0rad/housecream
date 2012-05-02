#include "src/config/config.h"

float tempConversion(float value) {
    return value * 50 / 1023;
}

const t_boardDescription boardDescription PROGMEM = {
    {0x54, 0x55, 0x58, 0x10, 0x00, 0xF5},           // mac
    {192, 168, 42, 245},                            // ip
    80,                                             // port
    "window1 controller",                           // name
    "window in front of the house not powered from POE but only by a transfo",   // description
    "http://192.168.42.211:8080",                   // notify url
};

// INPUT
const t_pinInputDescription pinInputDescription[] PROGMEM = {
//        {1, DIGITAL, 0, "door1 open captor", {{OVER_EQ, 1},{UNDER_EQ, 0},{0,0},{0,0}}, noInputConversion, defaultPinRead, "magnetic captor in the upper part"},
        {2, DIGITAL, 0, "switch red", {{OVER_EQ, 1},{UNDER_EQ, 0},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
        {4, DIGITAL, 0, "switch black", {{OVER_EQ, 1},{UNDER_EQ, 0},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
//        {6, ANALOG, 0, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
//        {7, DIGITAL, 0, "door1 outside temp", {{OVER_EQ, 0},{UNDER_EQ, 1},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
//        {8, DIGITAL, 0, "door1 outside temp", {{OVER_EQ, 0},{UNDER_EQ, 1},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
//        {9, ANALOG, 0, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
//        {14, ANALOG, 0, "door1 outside temp", {{0, 21.5},{0, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
//        {15, ANALOG, 0, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
//        {16, ANALOG, 0, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
//        {17, ANALOG, 0, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
//        {18, ANALOG, 0, "door1 outside temp", {{OVER_EQ, 21.5},{UNDER_EQ, 4},{0,0},{0,0}}, noInputConversion, defaultPinRead, "lm35 temperature captor"},
//        {19, DIGITAL, 0, "variator for light 1", {{0, 0},{0, 1},{0,0},{0,0}}, noInputConversion, defaultPinRead, "optocoupler isolated and triac / no zero detection"},
        {-1}
};

// OUTPUT
const t_pinOutputDescription pinOutputDescription[] PROGMEM = {
//        {6, ANALOG, "variator for light 1", 0, 255, 10, noOutputConversion, defaultPinWrite, "optocoupler isolated and triac / no zero detection"},
//        {7, DIGITAL, "variator for light 1", 0, 1, 0, noOutputConversion, defaultPinWrite, "optocoupler isolated and triac / no zero detection"},
        {3, DIGITAL, "out green", 0, 1, 0, noOutputConversion, defaultPinWrite, "optocoupler isolated and triac / no zero detection"},
        {5, DIGITAL, "out red", 0, 1, 0, noOutputConversion, defaultPinWrite, "optocoupler isolated and triac / no zero detection"},
        {-1}
};
