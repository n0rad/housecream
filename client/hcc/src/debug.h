#ifndef DEBUGUTILS_H
#define DEBUGUTILS_H

#include "Arduino.h"

void DEBUG_P(const prog_char *progmem);
void DEBUG_E(const char *eeprom);


#ifdef DEBUG
    #include "hcc.h"

    #define DEBUG_PRINT_FULL(str)    \
        Serial.print(millis());     \
        Serial.print(": ");    \
        Serial.print(__PRETTY_FUNCTION__); \
        Serial.print(' ');      \
        Serial.print(__FILE__);     \
        Serial.print(':');      \
        Serial.print(__LINE__);     \
        Serial.print(' ');      \
        Serial.println(str);

    #define DEBUG_START()   \
        Serial.print(millis());     \
        Serial.print(": ");    \
        Serial.print(__PRETTY_FUNCTION__); \
        Serial.print(' ');      \
        Serial.print(__FILE__);     \
        Serial.print(':');      \
        Serial.print(__LINE__);     \
        Serial.print(' ');

    #define DEBUG_PRINTLN(str)      Serial.println(str)
    #define DEBUG_PRINT(str)      Serial.print(str)
    #define DEBUG_PRINTDEC(num)   Serial.print(num, DEC)
    #define DEBUG_WRITE(str, len)   Serial.write(str, len)
//  #define DEBUG_HEXDUMP(x, y) hexDump(x, y)

#else

    #define DEBUG_PRINT_FULL(str)
    #define DEBUG_PRINT(str)
    #define DEBUG_START()
    #define DEBUG_PRINTDEC(num)
    #define DEBUG_PRINTLN(str)
    #define DEBUG_WRITE(str, len)

#endif

#endif
