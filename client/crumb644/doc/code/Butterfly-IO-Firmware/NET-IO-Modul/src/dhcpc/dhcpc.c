#include <stdio.h>
#include <string.h>
#include "../uip/uip.h"

#if UIP_CONF_UDP

#include "dhcpc.h"
#include "../uip/timer.h"
#include "../uip/pt.h"
#include "../uip/uip_arp.h"
#include "../driver/CP2200.h"

static struct dhcpc_state status;

//struct __attribute((packed)) dhcp_msg {
struct dhcp_msg {
    u8_t op, htype, hlen, hops;
    u8_t xid[4];
    u16_t secs, flags;
    u8_t ciaddr[4];
    u8_t yiaddr[4];
    u8_t siaddr[4];
    u8_t giaddr[4];
    u8_t chaddr[16];
    u8_t sname[64];
    u8_t file[128];
    u8_t options[312];
};

#define BOOTP_BROADCAST 0x8000

#define DHCP_REQUEST        1
#define DHCP_REPLY          2
#define DHCP_HTYPE_ETHERNET 1
#define DHCP_HLEN_ETHERNET  6
#define DHCP_MSG_LEN      236

#define DHCPC_SERVER_PORT  67
#define DHCPC_CLIENT_PORT  68

#define DHCPDISCOVER  1
#define DHCPOFFER     2
#define DHCPREQUEST   3
#define DHCPDECLINE   4
#define DHCPACK       5
#define DHCPNAK       6
#define DHCPRELEASE   7

#define DHCP_OPTION_SUBNET_MASK   1
#define DHCP_OPTION_ROUTER        3
#define DHCP_OPTION_DNS_SERVER    6
#define DHCP_OPTION_REQ_IPADDR   50
#define DHCP_OPTION_LEASE_TIME   51
#define DHCP_OPTION_MSG_TYPE     53
#define DHCP_OPTION_SERVER_ID    54
#define DHCP_OPTION_REQ_LIST     55
#define DHCP_OPTION_END         255


static const u8_t xid[4] = {0xad, 0xde, 0x12, 0x23};
static const u8_t magic_cookie[4] = {99, 130, 83, 99};

/*---------------------------------------------------------------------------*/
static u8_t * add_msg_type(u8_t *optptr, u8_t type) {
    *optptr++ = DHCP_OPTION_MSG_TYPE;
    *optptr++ = 1;
    *optptr++ = type;
    return optptr;
}

/*---------------------------------------------------------------------------*/
static u8_t * add_server_id(u8_t *optptr) {
    *optptr++ = DHCP_OPTION_SERVER_ID;
    *optptr++ = 4;
    memcpy(optptr, status.serverid, 4);
    return optptr + 4;
}


/*---------------------------------------------------------------------------*/
static u8_t * add_req_ipaddr(u8_t *optptr) {
    *optptr++ = DHCP_OPTION_REQ_IPADDR;
    *optptr++ = 4;
    memcpy(optptr, status.ipaddr, 4);
    return optptr + 4;
}

/*---------------------------------------------------------------------------*/
static u8_t * add_req_options(u8_t *optptr) {
    *optptr++ = DHCP_OPTION_REQ_LIST;
    *optptr++ = 3;
    *optptr++ = DHCP_OPTION_SUBNET_MASK;
    *optptr++ = DHCP_OPTION_ROUTER;
    //*optptr++ = DHCP_OPTION_DNS_SERVER;
    return optptr;
}

/*---------------------------------------------------------------------------*/
static u8_t * add_end(u8_t *optptr) {
    *optptr++ = DHCP_OPTION_END;
    return optptr;
}

/*---------------------------------------------------------------------------*/
static void create_msg(register struct dhcp_msg *m) {
    m->op = DHCP_REQUEST;
    m->htype = DHCP_HTYPE_ETHERNET;
    m->hlen = sizeof (uip_ethaddr.addr);
    m->hops = 0;
    memcpy(m->xid, xid, sizeof (m->xid));
    m->secs = 0;
    m->flags = HTONS(BOOTP_BROADCAST);
    memcpy(m->ciaddr, uip_hostaddr, sizeof (m->ciaddr));
    memset(m->yiaddr, 0, sizeof (m->yiaddr));
    memset(m->siaddr, 0, sizeof (m->siaddr));
    memset(m->giaddr, 0, sizeof (m->giaddr));
    memcpy(m->chaddr, uip_ethaddr.addr, sizeof (uip_ethaddr.addr));
    memset(&m->chaddr[sizeof (uip_ethaddr.addr)], 0, sizeof (m->chaddr) - sizeof (uip_ethaddr.addr));
    memset(m->sname, 0, sizeof (m->sname));
    memset(m->file, 0, sizeof (m->file));
    memcpy(m->options, magic_cookie, sizeof (magic_cookie));
}

/*---------------------------------------------------------------------------*/
static void send_discover(void) {
    u8_t *end;
    struct dhcp_msg *m = (struct dhcp_msg *) uip_appdata;
    create_msg(m);
    end = add_msg_type(&m->options[4], DHCPDISCOVER);
    end = add_req_options(end);
    end = add_end(end);
    uip_send(uip_appdata, end - (u8_t *) uip_appdata);
}

/*---------------------------------------------------------------------------*/
static void send_request(void) {
    u8_t *end;
    struct dhcp_msg *m = (struct dhcp_msg *) uip_appdata;
    create_msg(m);
    end = add_msg_type(&m->options[4], DHCPREQUEST);
    end = add_server_id(end);
    end = add_req_ipaddr(end);
    end = add_end(end);
    uip_send(uip_appdata, end - (u8_t *) uip_appdata);
}

/*---------------------------------------------------------------------------*/
static u8_t
parse_options(u8_t *optptr, int len) {
    u8_t *end = optptr + len;
    u8_t type = 0;
    while (optptr < end) {
        switch (*optptr) {
            case DHCP_OPTION_SUBNET_MASK:
                memcpy(status.netmask, optptr + 2, 4);
                break;
            case DHCP_OPTION_ROUTER:
                memcpy(status.default_router, optptr + 2, 4);
                break;
            case DHCP_OPTION_DNS_SERVER:
                //    memcpy(status.dnsaddr, optptr + 2, 4);
                    break;
            case DHCP_OPTION_MSG_TYPE:
                type = *(optptr + 2);
                break;
            case DHCP_OPTION_SERVER_ID:
                memcpy(status.serverid, optptr + 2, 4);
                break;
            case DHCP_OPTION_LEASE_TIME:
                memcpy(status.lease_time, optptr + 2, 4);
                break;
            case DHCP_OPTION_END:
                return type;
        }
        optptr += optptr[1] + 2;
    }
    return type;
}

/*---------------------------------------------------------------------------*/
static u8_t
parse_msg(void) {
    struct dhcp_msg *m = (struct dhcp_msg *) uip_appdata;

    if (m->op == DHCP_REPLY &&
            memcmp(m->xid, xid, sizeof (xid)) == 0 &&
            memcmp(m->chaddr, uip_ethaddr.addr, sizeof (uip_ethaddr.addr)) == 0) {
        memcpy(status.ipaddr, m->yiaddr, 4);
        return parse_options(&m->options[4], uip_datalen());
    }
    return 0;
}

/*---------------------------------------------------------------------------*/
static
PT_THREAD(handle_dhcp(void)) {
    PT_BEGIN(&status.pt);

    status.retries = 0;
    do {
        // send DHCP discover
        send_discover();
        status.state = DHCPC_STATE_SENT_DISCOVER;
        // wait for DHCP offer
        timer_set(&status.timer, CLOCK_SECOND * 2); // 3 seconds
        PT_WAIT_UNTIL(&status.pt, uip_newdata() || timer_expired(&status.timer));
        // if the answer was a DHCP offer
        if (uip_newdata()) {
            // if we received an answer, check if it was an OFFER
            if (parse_msg() == DHCPOFFER) {
                // received OFFER, now we need to send the REQUEST
                uip_flags &= ~UIP_NEWDATA;
                status.state = DHCPC_STATE_OFFER_RECEIVED;
                // send DHCP request
                send_request();
                status.state = DHCPC_STATE_SENT_REQUEST;
                timer_set(&status.timer, CLOCK_SECOND * 3); // 3 seconds
                PT_WAIT_UNTIL(&status.pt, uip_newdata() || timer_expired(&status.timer));
                if (uip_newdata()) {
                    // if we received an answer, check if it was an ACK or NAK
                    if (parse_msg() == DHCPACK) {
                        uip_flags &= ~UIP_NEWDATA;
                        status.state = DHCPC_STATE_CONFIG_RECEIVED;
                        
                        // apply the received settings
                        uip_setnetmask(status.netmask);
                        uip_sethostaddr(status.ipaddr);
                        uip_setdraddr(status.default_router);
                        
                        // now we need to refresh the request 
                        // in an endless loop after half of the release time
                        while (1) {
                            timer_set(&status.timer, CLOCK_SECOND / 2); // 500ms
                            do {
                                PT_WAIT_UNTIL(&status.pt, timer_expired(&status.timer));
                                timer_reset(&status.timer);
                                status.lease_time[0]--;
                                if (status.lease_time[0] == 0) {
                                    status.lease_time[1]--;
                                }
                            }                               
                            while (status.lease_time[1] > 0 || status.lease_time[0] > 0);
                            // send DHCP request
                            send_request();
                            status.state = DHCPC_STATE_SENT_REFRESH;
                            timer_set(&status.timer, CLOCK_SECOND * 3); // 3 seconds
                            PT_WAIT_UNTIL(&status.pt, uip_newdata() || timer_expired(&status.timer));
                            if (uip_newdata()) {
                                // if we received an answer, check if it was an ACK or NAK
                                if (parse_msg() == DHCPACK) {
                                    uip_flags &= ~UIP_NEWDATA;
                                    status.state = DHCPC_STATE_REFRESH_SUCCEEDED;
                                }
                            } else {
                                status.state = DHCPC_STATE_TIMEOUT;
                            }
                        }
                        
                    } else if (parse_msg() == DHCPNAK) {
                        status.state = DHCPC_STATE_NAK_RECEIVED;
                    }
                } else {
                    status.state = DHCPC_STATE_TIMEOUT;
                }
            }
        } else {
            status.state = DHCPC_STATE_TIMEOUT;
        }
    }
    while (++status.retries < 30);
    
    // DHCP client failed, apply static settings
    uip_sethostaddr(config.ipaddr);
    uip_setnetmask(config.netmask);
    uip_setdraddr(config.gateway);

    // DHCP client ends here.
    // Enter an endless loop to prevent that the client restarts.
    while (1) {
	PT_YIELD(&status.pt);
    }
    PT_END(&status.pt);
}

/*---------------------------------------------------------------------------*/
void
dhcpc_init(void) {
    uip_ipaddr_t addr;

    // initially, the IP-Address must be 0.0.0.0
    // otherwise the DHCP server would NAK the request
    uip_ipaddr(addr, 0, 0, 0, 0);
    uip_sethostaddr(addr);
    status.state = DHCPC_STATE_INITIAL;
    // Destination is the broadcast address
    uip_ipaddr(addr, 255, 255, 255, 255);
    status.conn = uip_udp_new(&addr, HTONS(DHCPC_SERVER_PORT));
    if (status.conn != NULL) {
        uip_udp_bind(status.conn, HTONS(DHCPC_CLIENT_PORT));
    }

    PT_INIT(&status.pt);
}

/*---------------------------------------------------------------------------*/
void dhcpc_appcall(void) {
    handle_dhcp();
}


struct dhcpc_state* dhcpc_get_status(void) {
    return &status;
};

#endif // UIP_CONF_UDP