#ifndef _IO_H
#define _IO_H

#include <stdint.h>

extern uint8_t isBusy;

// Universal commands to control the external I/O lines.

// Execute an I/O command and returns the result as string.

void io_command(char* result, char* command);

#endif	/* _IO_H */

