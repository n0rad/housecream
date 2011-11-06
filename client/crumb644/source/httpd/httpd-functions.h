#ifndef __HTTPD_FUNCTIONS_H__
#define __HTTPD_FUNCTIONS_H__

#include "httpd.h"

// Calls a function by name.
// state->filename is the function name string
// state->parameters is the parameters string

unsigned short httpd_function_dispatcher(void* state);

// parse an IP address

void parse_ip_address(u8_t result[], char* source);

#endif /* __HTTPD_FUNCTIONS_H__ */

