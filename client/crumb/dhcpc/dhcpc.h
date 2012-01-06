#ifndef __DHCPC_H__
#define __DHCPC_H__

#include "../uip/timer.h"
#include "../uip/pt.h"

#define DHCPC_STATE_INITIAL           0
#define DHCPC_STATE_SENT_DISCOVER     1
#define DHCPC_STATE_OFFER_RECEIVED    2
#define DHCPC_STATE_SENT_REQUEST      3
#define DHCPC_STATE_CONFIG_RECEIVED   4
#define DHCPC_STATE_NAK_RECEIVED      5
#define DHCPC_STATE_TIMEOUT           6
#define DHCPC_STATE_SENT_REFRESH      7
#define DHCPC_STATE_REFRESH_SUCCEEDED 8

struct dhcpc_state {
    struct pt pt;              // Protothread of the DHCP client
    u8_t state;                // Status, see values DHCPC_STATE_xx values
    struct uip_udp_conn *conn; // Connection of the UPD socket
    struct timer timer;        // Timer for waiting for repsonse
    u8_t retries;              // Counter for retries
    u8_t enabled;              // 1, if the DHCP client is enabled

    // the result:
    u16_t serverid[2];         // received ip address of the DHCP server
    u16_t lease_time[2];       // received lease time in seconds
    u16_t ipaddr[2];           // received ip address
    u16_t netmask[2];          // received network mask
    u16_t default_router[2];   // received default router
    //u16_t dnsaddr[2];        // received dns address
};

// initialize the DHCP client
void dhcpc_init(void);

// called by uIP when it executes the DHCP client
void dhcpc_appcall(void);

// may be used to query the DHCP client status
struct dhcpc_state* dhcpc_get_status(void);

//typedef struct dhcpc_state uip_udp_appstate_t;
typedef char uip_udp_appstate_t;
#define UIP_UDP_APPCALL dhcpc_appcall


#endif /* __DHCPC_H__ */
