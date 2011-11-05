#ifndef __HTTPD_H__
#define __HTTPD_H__

#include "../uip/uip.h"

// called by inetd periodically and in case of connection events
void httpd_appcall(struct application_state *app_state);

// decode an URL parameter. The result is stored where the source came from.
// The parameter must be terminated with an ascii 0 or an & character.
void url_decode(char* parameter);

#endif /* __HTTPD_H__ */
