#include "seriald/seriald.h"

#ifdef DEBUG
  #define DEBUG_PRINT(x)      serial_writestr(x)
  #define DEBUG_PRINTDEC(x)   serial_writeDec(x)
  #define DEBUG_PRINTLN(x)    serial_writestrln(x)
//  #define DEBUG_WRITE(x, y)   Serial.write(x, y)
//  #define DEBUG_HEXDUMP(x, y) hexDump(x, y)
#else
  #define DEBUG_PRINT(x)
  #define DEBUG_PRINTDEC(x)
  #define DEBUG_PRINTLN(x)
  #define DEBUG_WRITE(x, y)
  #define DEBUG_HEXDUMP(x, y)
#endif
