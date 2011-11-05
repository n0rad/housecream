#ifndef __SERIALD_H__
#define __SERIALD_H__

// Enable serial terminal compatibility mode, which means
// Input lines are terminated by a single \n or \r
// Output lines are terminated by \r\n
// In normal mode, lines are always terminated by \n.
#define TERMINAL_MODE 1

// Enable echo on serial interface
#define SERIAL_ECHO 1

// Initialisation
void seriald_init(void);

#endif /* __SERIALD_H__ */
