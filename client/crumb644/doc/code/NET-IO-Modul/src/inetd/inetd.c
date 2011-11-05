/*

The inetd is called by uIP for each incoming TCP connection.
It calls the correct application depending on port number.

Port 80 -> httpd
Port 23 -> socketd
remote Port 25 -> smtpc

*/

#include "../uip/uip.h"
#include "inetd.h"
#include "../socketd/socketd.h"
#include "../httpd/httpd.h"

#ifdef EMAIL_APP
#include "../smtpc/smtpc.h"
#endif //EMAIL_APP


// Dispatch events for TCP connections to the correct application.
// This is called by uIP in case of events related to a connection 
// and also periodically.

void inetd_appcall(void) {
    // Get the application state structure for the current connection
    struct application_state *app_state = (struct application_state *) &(uip_conn->appstate);
    
    if (uip_conn->lport == HTONS(80)) 
        httpd_appcall(app_state);
    else if (uip_conn->lport == HTONS(23))
        socketd_appcall(app_state);
#ifdef EMAIL_APP
    else if (uip_conn->rport == HTONS(25))
        smtpc_appcall(app_state);
#endif // EMAIL_APP
}


// Initialize the applications and tell uIP on what ports we want to listen.

void inetd_init(void) {   
    uip_listen(HTONS(80));
    uip_listen(HTONS(23));
}
