#ifndef __SOCKETD_H__
#define __SOCKETD_H__

#include "../uip/uip.h"

// called by inetd periodically and in case of connection events
void socketd_appcall(struct application_state *app_state);


#endif /* __SOCKETD_H__ */
