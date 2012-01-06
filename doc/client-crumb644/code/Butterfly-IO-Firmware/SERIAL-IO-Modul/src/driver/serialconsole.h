#ifndef serialconsole_h
#define serialconsole_h

// Enable serial terminal compatibility mode, which means
// Input lines are terminated by a single \n or \r
// Output lines are terminated by \r\n
// In normal mode, lines are always terminated by \n.
#define TERMINAL_MODE 1

// Enable echo on serial interface
#define SERIAL_ECHO 1

// Initialize the serial port and bind it to stdin and stdout to it.
void initserial(void);

#endif //serialconsole_h
