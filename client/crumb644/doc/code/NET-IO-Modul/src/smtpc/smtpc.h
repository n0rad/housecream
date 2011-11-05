#ifndef __SMTPC_H__
#define __SMTPC_H__

#include "../uip/uip.h"
#include <stdint.h>


// called by inetd periodically and in case of connection events
void smtpc_appcall(struct application_state *app_state);

// Trigger sending an eMail
// return values:
//   0 = success
//  -1 = application already in use
//  -2 = no resource free for new IP connection
int8_t sendMail(int server, int username, int password, int from, int to, int subject);

// Structure for the statistics
struct smtp_statistics {
    uint16_t sent;         // number of sent messages
    uint16_t timeout_err;  // number of errors because of timeout
    uint16_t ehlo_err;     // number of errors because ehlo command failed
    uint16_t auth_err;     // number of errors because authentication failed
    uint16_t from_err;     // number of errors because sender rejected
    uint16_t rcpt_err;     // number of errors because recipient rejected
    uint16_t data_err;     // number of errors because content rejected
};

// Storage for the configuration
extern struct smtp_statistics smtp_stats;


#endif /* __SMTPC_H__ */
