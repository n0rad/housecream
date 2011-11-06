#include <string.h>
#include <stdlib.h>
#include <avr/io.h>
#include <stdio.h>
#include <avr/pgmspace.h>
#include "httpd.h"
#include "httpd-fs.h"
#include "../uip/uip.h"
#include "../uip/uip_arp.h"
#include "../dhcpc/dhcpc.h"
#include "../driver/CP2200.h"
#include "../io-commands.h"
#include "../hw-layout.h"

#if ADC_CHANNELS > 0
    #include "../driver/ADC.h"
#endif

#ifdef EMAIL_APP
    #include "../email_app/email_app.h"
#endif //EMAIL_APP

// Functions for creating dynamic websites.
//
// Functions can perform some action and the can optionally produce some 
// output. 
//
// Functions can be called in two ways.
// 1) Script marker in a shtml file: %! function_name?params
// 2) URL: http://hostname/function_name?parameters
//
// The function "io?params" calls functions from io-commands.c.
//
// To call a function via URL that does not use parameters, enter any
// dummy parameter, otherwise httpd thinks that this is a regular filename
// and no function call.
//
// To produce output, functions write a string into the buffer uip_appdata
// and return the size of the result. The result must not exceed the 
// buffers size, which is about 1400 bytes.
//
// If the name of the function is the same as a filename, this file gets
// loaded after the function returns.


static char file_stat_formatter1[] PROGMEM = "<tr><td>";
static char file_stat_formatter2[] PROGMEM = "</td><td>%5u</td></tr>\n";

// Show how often each file has been accessed

static unsigned short function_file_stats(char* buffer, int bufsize) {
    unsigned short len = 0;
    // list as many files as fit into the buffer
    for (int i = 0; len < bufsize; i++) {
        // name fo the file
        char* name = httpd_fs_getName(i);
        // break if there is no file left anmyore
        if (name == 0) {
            break;
        }
        // we cant simply pass the filename to snprintf because it is in
        // program memory and snprintf requires strings in RAM.
        strncpy_P(buffer + len, file_stat_formatter1, bufsize - len);
        len += sizeof (file_stat_formatter1);
        if (len < bufsize - len) {
            char* name = httpd_fs_getName(i);
            strncpy_P(buffer + len, name, bufsize - len);
            len += strlen_P(name);
        }
        if (len < bufsize - len) {
            int count = httpd_fs_getCount(i);
            len += snprintf_P(buffer + len, bufsize - len, file_stat_formatter2, count);
        }
    }
    return len;
}


//-----------------------------------------------------------------------

static char input_format[] PROGMEM = "%d%c%d%c%d%c%d";

// parse an IP address

void parse_ip_address(u8_t result[], char* source) {
    char c;
    int d0,d1,d2,d3; // needed because sscanf requires int variables
    sscanf_P(source,input_format,&d0,&c,&d1,&c,&d2,&c,&d3);
    // copy the int values to the result bytes.
    result[0]=d0;
    result[1]=d1;
    result[2]=d2;
    result[3]=d3;
}

//-------------------------------------------------------------------------


static char freemem_formatter[] PROGMEM = "%5u";


// Show the largest available block of free memory

static unsigned short function_freememory(char* buffer, int bufsize) {
    //
    void *p = 0;
    size_t size;
    for (size = RAMEND; size > 1; size -= 10) {
        p = malloc(size);
        if (p != 0) {
            break;
        }
    }
    if (p != 0) {
        free(p);
    }
    else {
        size=0;
    }
    return snprintf_P(buffer, bufsize, freemem_formatter,size);
}


//---------------------------------------------------------------------------

#if UIP_STATISTICS

static char ip_stat_formatter[] PROGMEM = "%5u\n%5u\n%5u\n\n"
    "%5u\n%5u\n%5u\n%5u\n%5u\n%5u\n%5u\n\n"
    "%5u\n%5u\n%5u\n%5u\n\n"
    "%5u\n%5u\n%5u\n\n"
    "%5u\n%5u\n%5u\n%5u\n%5u\n%5u\n\n"
    "%5u\n%5u\n%5u\n%5u\n%5u";

// Show network I/O statistics from IP protocol

static unsigned short function_net_stats(char* buffer, int bufsize) {
    return snprintf_P(buffer, bufsize, ip_stat_formatter,
            uip_stat.ip.recv,
            uip_stat.ip.sent,
            uip_stat.ip.drop,

            uip_stat.ip.vhlerr,
            uip_stat.ip.hblenerr,
            uip_stat.ip.lblenerr,
            uip_stat.ip.fragerr,
            uip_stat.ip.chkerr,
            uip_stat.ip.protoerr,
            uip_stat.ip.wronghost,

            uip_stat.icmp.recv,
            uip_stat.icmp.sent,
            uip_stat.icmp.drop,
            uip_stat.icmp.typeerr,

            uip_stat.tcp.recv,
            uip_stat.tcp.sent,
            uip_stat.tcp.drop,

            uip_stat.tcp.chkerr,
            uip_stat.tcp.ackerr,
            uip_stat.tcp.rst,
            uip_stat.tcp.rexmit,
            uip_stat.tcp.syndrop,
            uip_stat.tcp.synrst,
#if UIP_CONF_UDP
            uip_stat.udp.recv,
            uip_stat.udp.sent,
            uip_stat.udp.drop,
            uip_stat.udp.chkerr,
            uip_stat.udp.wrongconn
#else // UIP_CONF_UDP
            0,0,0,0,0
#endif // UIP_CONF_UDP
            );
}

#endif // UIP_STATISTIC



//---------------------------------------------------------------------------


static char tcp_closed[]      PROGMEM="closed";
static char tcp_syn_rcvd[]    PROGMEM="syn-rcvd";
static char tcp_syn_sent[]    PROGMEM="syn-sent";
static char tcp_established[] PROGMEM="established";
static char tcp_fin_wait_1[]  PROGMEM="fin-wait-1";
static char tcp_fin_wait_2[]  PROGMEM="fin-wait-2";
static char tcp_closing[]     PROGMEM="closing";
static char tcp_time_wait[]   PROGMEM="time-wait";
static char tcp_last_ack[]    PROGMEM="last-ack";

static char tcp_conn_formatter1[] PROGMEM = "<tr><td>%d</td><td>%u.%u.%u.%u:%u</td><td>";
static char tcp_conn_formatter2[] PROGMEM = "</td><td>%u</td><td>%u</td><td>%c %c</td></tr>\n";


// Show informations about all TCP connections

static unsigned short function_tcp_connections(char* buffer, int bufsize) {
    unsigned short len = 0;
    // list as many connections as fit into the buffer
    for (int i = 0; len < bufsize && i<UIP_CONF_MAX_CONNECTIONS; i++) {
        len+=snprintf_P(buffer+len, bufsize-len, tcp_conn_formatter1,
                htons(uip_conns[i].lport),
                htons(uip_conns[i].ripaddr[0]) >> 8,
                htons(uip_conns[i].ripaddr[0]) & 0xff,
                htons(uip_conns[i].ripaddr[1]) >> 8,
                htons(uip_conns[i].ripaddr[1]) & 0xff,
                htons(uip_conns[i].rport));
        switch (uip_conns[i].tcpstateflags & UIP_TS_MASK) {
            case UIP_CLOSED:
                strncpy_P(buffer+len,tcp_closed,bufsize-len);
                len+=sizeof(tcp_closed)-1;
                break;
            case UIP_SYN_RCVD:
                strncpy_P(buffer+len,tcp_syn_rcvd,bufsize-len);
                len+=sizeof(tcp_syn_rcvd)-1;
                break;
            case UIP_SYN_SENT:
                strncpy_P(buffer+len,tcp_syn_sent,bufsize-len);
                len+=sizeof(tcp_syn_sent)-1;
                break;
            case UIP_ESTABLISHED:
                strncpy_P(buffer+len,tcp_established,bufsize-len);
                len+=sizeof(tcp_established)-1;
                break;
            case UIP_FIN_WAIT_1:
                strncpy_P(buffer+len,tcp_fin_wait_1,bufsize-len);
                len+=sizeof(tcp_fin_wait_1)-1;
                break;
            case UIP_FIN_WAIT_2:
                strncpy_P(buffer+len,tcp_fin_wait_2,bufsize-len);
                len+=sizeof(tcp_fin_wait_1)-1;
                break;
            case UIP_CLOSING:
                strncpy_P(buffer+len,tcp_closing,bufsize-len);
                len+=sizeof(tcp_closing);
                break;
            case UIP_TIME_WAIT:
                strncpy_P(buffer+len,tcp_time_wait,bufsize-len);
                len+=sizeof(tcp_time_wait)-1;
                break;
            case UIP_LAST_ACK:
                strncpy_P(buffer+len,tcp_last_ack,bufsize-len);
                len+=sizeof(tcp_last_ack)-1;
                break;
        }
        len+=snprintf_P(buffer+len, bufsize-len, tcp_conn_formatter2,
                uip_conns[i].nrtx,
                uip_conns[i].timer,
                (uip_outstanding(&uip_conns[i])) ? '*' : ' ',
                (uip_stopped(&uip_conns[i])) ? '!' : ' ');
    }
    return len;
}


//---------------------------------------------------------------------------

#if UIP_CONF_UDP

static char dhcpc_initial[]           PROGMEM="not started";
static char dhcpc_sent_discover[]     PROGMEM="sent discover";
static char dhcpc_offer[]             PROGMEM="offer received";
static char dhcpc_sent_request[]      PROGMEM="sent request";
static char dhcpc_config[]            PROGMEM="config received";
static char dhcpc_nak[]               PROGMEM="nak received";
static char dhcpc_timeout[]           PROGMEM="timeout";
static char dhcpc_sent_refresh[]      PROGMEM="sent refresh";
static char dhcpc_refresh_succeeded[] PROGMEM="refresh succeeded";

// Show informations about all TCP connections

static unsigned short function_dhcp_status(char* buffer, int bufsize) {
    struct dhcpc_state* status=dhcpc_get_status();
    int len=0;
    switch (status->state) {
        case DHCPC_STATE_INITIAL:
            strncpy_P(buffer,dhcpc_initial,bufsize);
            len=sizeof(dhcpc_initial)-1;
            break;
        case DHCPC_STATE_SENT_DISCOVER:
            strncpy_P(buffer,dhcpc_sent_discover,bufsize);
            len=sizeof(dhcpc_sent_discover)-1;
            break;
        case DHCPC_STATE_OFFER_RECEIVED:
            strncpy_P(buffer,dhcpc_offer,bufsize);
            len=sizeof(dhcpc_offer)-1;
            break;
        case DHCPC_STATE_SENT_REQUEST:
            strncpy_P(buffer,dhcpc_sent_request,bufsize);
            len=sizeof(dhcpc_sent_request)-1;
            break;
        case DHCPC_STATE_CONFIG_RECEIVED:
            strncpy_P(buffer,dhcpc_config,bufsize);
            len=sizeof(dhcpc_config)-1;
            break;
        case DHCPC_STATE_NAK_RECEIVED:
            strncpy_P(buffer,dhcpc_nak,bufsize);
            len=sizeof(dhcpc_nak)-1;
            break;
        case DHCPC_STATE_TIMEOUT:
            strncpy_P(buffer,dhcpc_timeout,bufsize);
            len=sizeof(dhcpc_timeout)-1;
            break;
        case DHCPC_STATE_SENT_REFRESH:
            strncpy_P(buffer,dhcpc_sent_refresh,bufsize);
            len=sizeof(dhcpc_sent_refresh)-1;
            break;
        case DHCPC_STATE_REFRESH_SUCCEEDED:
            strncpy_P(buffer,dhcpc_refresh_succeeded,bufsize);
            len=sizeof(dhcpc_refresh_succeeded)-1;
            break;
        default:
            // return a dummy because a null string is not allowed
            buffer[0]='?';
            len=1;
    }
    return len;
}

#endif // UIP_CONF_UDP

//------------------------------------------------------------------------------

// get ip address, gateways, netmask  and authentication 

static char tcp_address_formatter[] PROGMEM = "%u.%u.%u.%u";

// get the current ip address
static unsigned short function_get_current_ip(char* buffer, int bufsize) {
    u8_t adr[4];
    uip_gethostaddr(adr);
    return snprintf_P(buffer, bufsize, tcp_address_formatter,adr[0],adr[1],adr[2],adr[3]);
}

// get the current netmask
static unsigned short function_get_current_mask(char* buffer, int bufsize) {
    u8_t adr[4];
    uip_getnetmask(adr);
    return snprintf_P(buffer, bufsize, tcp_address_formatter,adr[0],adr[1],adr[2],adr[3]);
}

// get the current gateway
static unsigned short function_get_current_gw(char* buffer, int bufsize) {
    u8_t adr[4];
    uip_getdraddr(adr);
    return snprintf_P(buffer, bufsize, tcp_address_formatter,adr[0],adr[1],adr[2],adr[3]);
}

// get the configured ip address
static unsigned short function_get_configured_ip(char* buffer, int bufsize) {
    u8_t adr[4];
    uip_gethostaddr(adr);
    return snprintf_P(buffer, bufsize, tcp_address_formatter,config.ipaddr[0],config.ipaddr[1],config.ipaddr[2],config.ipaddr[3]);
}

// get the configured netmask
static unsigned short function_get_configured_mask(char* buffer, int bufsize) {
    u8_t adr[4];
    uip_getnetmask(adr);
    return snprintf_P(buffer, bufsize, tcp_address_formatter,config.netmask[0],config.netmask[1],config.netmask[2],config.netmask[3]);
}

// get the configured gateway
static unsigned short function_get_configured_gw(char* buffer, int bufsize) {
    return snprintf_P(buffer, bufsize, tcp_address_formatter,config.gateway[0],config.gateway[1],config.gateway[2],config.gateway[3]);
}

// get the configured authentication username:password
static unsigned short function_get_configured_auth(char* buffer, int bufsize) {
    if (config.authentication[0]==0 || config.authentication[0]==255) {
        return 0;
    }
    else {
        strcpy(buffer,config.authentication);
        return strlen(config.authentication);
    }
}



//------------------------------------------------------------------------------

// get a dhcp enabled flag for the config form

static unsigned short function_dhcp_selected(char* buffer, int bufsize) {
    if (config.dhcpenable) {
        return snprintf_P(buffer, bufsize, PSTR("selected"));
    }
    else {
        // return a dummy because a null string is not allowed
        buffer[0]=' ';
        return 1;
    }
}

//------------------------------------------------------------------------------

// get MAC address

static char mac_formatter[] PROGMEM = "%02x:%02x:%02x:%02x:%02x:%02x";

static unsigned short function_get_mac(char* buffer, int bufsize) {
    return snprintf_P(buffer, bufsize, mac_formatter,
            uip_ethaddr.addr[0],
            uip_ethaddr.addr[1],
            uip_ethaddr.addr[2],
            uip_ethaddr.addr[3],
            uip_ethaddr.addr[4],
            uip_ethaddr.addr[5]);
}


//------------------------------------------------------------------------------

static char dhcp[] PROGMEM = "dhcp=";
static char ip[]   PROGMEM = "ip=";
static char mask[] PROGMEM = "mask=";
static char gw[]   PROGMEM = "gw=";
static char auth[] PROGMEM = "auth=";

// process form data from config.shtml

static unsigned short function_config(char* buffer, char* parameters) {
    // find the settings in parameters and parse them
    char* position;
    int value;
    position=strstr_P(parameters,dhcp);
    if (position!=NULL) {
        value=atoi(position+sizeof(dhcp)-1);
        config.dhcpenable=value;
    }
    position=strstr_P(parameters,ip);
    if (position!=NULL) {
        parse_ip_address(config.ipaddr,position+sizeof(ip)-1);
        uip_sethostaddr(&config.ipaddr);
    }
    position=strstr_P(parameters,mask);
    if (position!=NULL) {
        parse_ip_address(config.netmask,position+sizeof(mask)-1);
        uip_setnetmask(&config.netmask);
    }
    position=strstr_P(parameters,gw);
    if (position!=NULL) {
        parse_ip_address(config.gateway,position+sizeof(gw)-1);
        uip_setdraddr(&config.gateway);
    }
    position=strstr_P(parameters,auth);
    if (position!=NULL) {
        char* param=position+sizeof(auth)-1;
        url_decode(param);
        // copy the parameter to the config
        strcpy(config.authentication,param);
    }
    writeFlashConfig();
    // return a dummy because a null string is not allowed
    buffer[0]=' ';
    buffer[1]=0;
    return 1;
}

//--------------------------------------------------------------------------


static char port_formatter[]  PROGMEM="0x%02X = %c%c%c%c%c%c%c%c";

// Status of an led.
// The parameter may be A, B, C, D, E, F, G for the port name

static unsigned short function_port_status(char* buffer, int bufsize, char* parameters) {
    int value=0;
    switch (parameters[0]) {
      
    #ifdef PINA
      case 'A': value=PINA; break;
    #endif // PINA
    
    #ifdef PINB
      case 'B': value=PINB; break;
    #endif // PINB
    
    #ifdef PINC
      case 'C': value=PINC; break;
    #endif // PINC

    #ifdef PIND
      case 'D': value=PIND; break;
    #endif // PIND
    
    #ifdef PINE
      case 'E': value=PINE; break;
    #endif // PINE
    
    #ifdef PINF
      case 'F': value=PINF; break;
    #endif // PINF
    
    #ifdef PING
      case 'G': value=PING; break;
    #endif // PING
    
    #ifdef PINH
      case 'H': value=PINH; break;
    #endif // PINH
 
      default: strcpy_P(buffer, PSTR("n.a.")); return 4;
    }  

      
    return snprintf_P(buffer, bufsize, port_formatter,
            value,
            (value>>7 & 1)+'0',
            (value>>6 & 1)+'0',
            (value>>5 & 1)+'0',
            (value>>4 & 1)+'0',
            (value>>3 & 1)+'0',
            (value>>2 & 1)+'0',
            (value>>1 & 1)+'0',
            (value & 1)+'0');
}


//--------------------------------------------------------------------------


// Status of an ADC channel.
// The parameter may be in range 0-7

static unsigned short function_adc_status(char* buffer, int bufsize, char* parameters) {
    #if ADC_CHANNELS > 0
        int i=atoi(parameters);
        if (i<ADC_CHANNELS) {
            // wait until no other thread executes this function
            while (isBusy);
            isBusy=1;
            // read ADC input
            int value=getADC(i);
            isBusy=0;
            // print the result
            #ifdef EXT_AREF
               return snprintf_P(buffer, bufsize, PSTR("0x0%03X = %.2f V"),value,getAREF()*value);
            #else
                return snprintf_P(buffer, bufsize, PSTR("0x0%03X"),value);
            #endif
        }
    #endif
    strcpy_P(buffer,PSTR("n.a."));
    return 4;
}


//--------------------------------------------------------------------------

// Call an I/O command from io-commands.c

static unsigned short function_io(char* buffer, char* parameters) {    
    // execute the io command
    io_command(buffer,parameters);
    return strlen(buffer);        
}


//------------------------------------------------------------------------------


// names of the functions for the script marker in shtml files

static char nameof_file_stats[]      PROGMEM = "file-stats";
static char nameof_net_stats[]       PROGMEM = "net-stats";
static char nameof_tcp_connections[] PROGMEM = "tcp-conn";
static char nameof_freememory[]      PROGMEM = "freemem";
static char nameof_dhcp_status[]     PROGMEM = "dhcp-status";
static char nameof_getip[]           PROGMEM = "getip";
static char nameof_getmask[]         PROGMEM = "getmask";
static char nameof_getgw[]           PROGMEM = "getgw";
static char nameof_currentip[]       PROGMEM = "currentip";
static char nameof_currentmask[]     PROGMEM = "currentmask";
static char nameof_currentgw[]       PROGMEM = "currentgw";
static char nameof_getauth[]         PROGMEM = "getauth";
static char nameof_dhcp_select[]     PROGMEM = "dhcpsel";
static char nameof_config[]          PROGMEM = "config.shtml";
static char nameof_getmac[]          PROGMEM = "getmac";
static char nameof_port_status[]     PROGMEM = "portstatus";
static char nameof_adc_status[]      PROGMEM = "adcstatus";
static char nameof_canauth[]         PROGMEM = "canauth";
static char nameof_io[]              PROGMEM = "io";
static char unknown_func[]           PROGMEM = "Unknown function %s, params=%s\n";

static char hiddenstyle[]            PROGMEM = "display:none";

//-------------------------------------------------------------------------------------


// Calls a function by name
// state->filename is the function name string
// state->parameters is the parameters string

unsigned short httpd_function_dispatcher(void* state) {
    struct application_state* app_state=state;
    char* name = app_state->filename;
    char* parameters = app_state->parameters;
    char* buffer = uip_appdata;
    unsigned short bufsize = UIP_APPDATA_SIZE;
    
    if (strcmp_P(name,nameof_io)==0) {
        return function_io(buffer,parameters);
    }
    else if (strcmp_P(name, nameof_file_stats) == 0) {
        return function_file_stats(buffer, bufsize);
    }
#if UIP_STATISTICS
    else if (strcmp_P(name, nameof_net_stats) == 0) {
        return function_net_stats(buffer, bufsize);
    }
#endif // UIP_STATISTICS
    else if (strcmp_P(name, nameof_tcp_connections) == 0) {
        return function_tcp_connections(buffer, bufsize);
    }
    else if (strcmp_P(name, nameof_freememory) == 0) {
        return function_freememory(buffer, bufsize);
    }
#if UIP_CONF_UDP
    else if (strcmp_P(name, nameof_dhcp_status) == 0) {
        return function_dhcp_status(buffer, bufsize);
    }
    else if (strcmp_P(name, nameof_dhcp_select) == 0) {
        return function_dhcp_selected(buffer, bufsize);
    }
#endif // UIP_CONF_UDP
    else if (strcmp_P(name, nameof_getip) == 0) {
        return function_get_configured_ip(buffer, bufsize);
    }
    else if (strcmp_P(name, nameof_getmask) == 0) {
        return function_get_configured_mask(buffer, bufsize);
    }
    else if (strcmp_P(name, nameof_getgw) == 0) {
        return function_get_configured_gw(buffer, bufsize);
    }
    else if (strcmp_P(name, nameof_getauth) == 0) {
        return function_get_configured_auth(buffer, bufsize);
    }
    else if (strcmp_P(name, nameof_currentip) == 0) {
        return function_get_current_ip(buffer, bufsize);
    }
    else if (strcmp_P(name, nameof_currentmask) == 0) {
        return function_get_current_mask(buffer, bufsize);
    }
    else if (strcmp_P(name, nameof_currentgw) == 0) {
        return function_get_current_gw(buffer, bufsize);
    }
    else if (strcmp_P(name, nameof_config) == 0) {
        return function_config(buffer, parameters);
    }
    else if (strcmp_P(name, nameof_getmac) == 0) {
        return function_get_mac(buffer, bufsize);
    }
    else if (strcmp_P(name,nameof_port_status)==0) {
        return function_port_status(buffer,bufsize,parameters);
    }
    else if (strcmp_P(name,nameof_adc_status)==0) {
        return function_adc_status(buffer,bufsize,parameters);
    }
    else if (strcmp_P(name,nameof_canauth)==0) {
#ifdef AUTH
        return 0;
#else
        strcpy_P(buffer,hiddenstyle);
        return sizeof(hiddenstyle)-1;
#endif // AUTH
    }
#ifdef EMAIL_APP
    int i=email_httpd_functions(buffer,bufsize,name,parameters);
    if (i>=0) {
        return i;
    }
#endif // EMAIL_APP
    // the function name is unknown.
    return sprintf_P(buffer,unknown_func,name,parameters);
}

