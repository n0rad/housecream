/*
    SMTP client, sends eMail for the eMail application.
    
    If it does not work, use a program like WireShark to snoop the network traffic.
    
    This application uses the app_state->inputbuffer also for output to reduce RAM usage. This is 
    possible because of the half-duplex communication in SMTP.    
*/

#include <stdint.h>
#include <string.h>
#include <avr/pgmspace.h>
#include "../uip/uip.h"
#include "smtpc.h"
#include "../email_app/email_app.h"
#include "../httpd/httpd-functions.h"
#include "../driver/CP2200.h"
#include "../httpd/base64.h"

// Is true if the application active. Only one instance can run at a time.
static uint8_t isBusy;

// Timer, used to cancel sending if it takes too long.
static struct timer myTimer;

// Statistics counters
struct smtp_statistics smtp_stats;


// Addresses of strings in ethernet controller flash memory that will be sent.
static int Username;
static int Password;
static int From;
static int To;
static int Subject;


// Do something when a connection has been established
// For all commands we check only the first digit of the answer.
// 2xx = Command accepted or welcome message
// 3xx = Command accepted, requires input data
// 5xx = Error
static PT_THREAD(handle_connection(struct application_state *app_state))
{
    PSOCK_BEGIN(&app_state->sin);   

    // read a line and check if it starts with "220" = welcome
    PSOCK_READTO(&app_state->sin,'\n');   
    if(app_state->inputbuffer[0]!='2') {
        smtp_stats.ehlo_err++;
        PSOCK_CLOSE_EXIT(&app_state->sin);
    }    
    
    PSOCK_SEND_STR_P(&app_state->sin, PSTR("EHLO butterfly\r\n"));
    // read a line and check if it starts with "250" = Ok
    PSOCK_READTO(&app_state->sin,'\n');   
    if(app_state->inputbuffer[0]!='2') {
        smtp_stats.ehlo_err++;
        PSOCK_CLOSE_EXIT(&app_state->sin);
    }

    PSOCK_SEND_STR_P(&app_state->sin, PSTR("AUTH LOGIN\r\n"));   
    
    // The previous answer might contain multiple lines that start with 250.
    // Now read all lines until we get something else.
    while (app_state->inputbuffer[0]=='2') {
        PSOCK_READTO(&app_state->sin,'\n');  
    }
    // Check if we received either
    // "334" = enter username or 
    // "503" = auth command used where not advertised
    if(app_state->inputbuffer[0]!='5') {
        if(app_state->inputbuffer[0]!='3') {
            smtp_stats.auth_err++;
            PSOCK_CLOSE_EXIT(&app_state->sin);
        }
        
        // We store the unencrypted username at the end of the input buffer, where we have some free space.
        // This reduces RAM usage.
        readFlashString(app_state->inputbuffer+60,Username);
        encode64(app_state->inputbuffer,app_state->inputbuffer+60);
        PSOCK_SEND_STR(&app_state->sin, app_state->inputbuffer); 
        PSOCK_SEND_STR_P(&app_state->sin, PSTR("\r\n"));
        // read a line and check if it starts with "334" = enter password
        PSOCK_READTO(&app_state->sin,'\n');   
        if(app_state->inputbuffer[0]!='3') {
            smtp_stats.auth_err++;
            PSOCK_CLOSE_EXIT(&app_state->sin);
        }    
        
        // We store the unencrypted password at the end of the input buffer, where we have some free space.
        // This reduces RAM usage.
        readFlashString(app_state->inputbuffer+60,Password);
        encode64(app_state->inputbuffer,app_state->inputbuffer+60);
        PSOCK_SEND_STR(&app_state->sin, app_state->inputbuffer); 
        PSOCK_SEND_STR_P(&app_state->sin, PSTR("\r\n"));
        // read a line and check if it starts with "235" = authentication succeeded.
        PSOCK_READTO(&app_state->sin,'\n');   
        if(app_state->inputbuffer[0]!='2') {
            smtp_stats.auth_err++;
            PSOCK_CLOSE_EXIT(&app_state->sin);
        }    
    }
    
    PSOCK_SEND_STR_P(&app_state->sin, PSTR("MAIL FROM: "));
    readFlashString(app_state->inputbuffer,From);
    PSOCK_SEND_STR(&app_state->sin,app_state->inputbuffer); 
    PSOCK_SEND_STR_P(&app_state->sin, PSTR("\r\n"));
    // read a line and check if it starts with "250" = Ok
    PSOCK_READTO(&app_state->sin,'\n');   
    if(app_state->inputbuffer[0]!='2') {
        smtp_stats.from_err++;
        PSOCK_CLOSE_EXIT(&app_state->sin);
    }
    
    PSOCK_SEND_STR_P(&app_state->sin, PSTR("RCPT TO: "));
    readFlashString(app_state->inputbuffer,To);
    PSOCK_SEND_STR(&app_state->sin,app_state->inputbuffer); 
    PSOCK_SEND_STR_P(&app_state->sin, PSTR("\r\n"));
    // read a line and check if it starts with "250" = Accepted
    PSOCK_READTO(&app_state->sin,'\n');   
    if(app_state->inputbuffer[0]!='2') {
        smtp_stats.rcpt_err++;
        PSOCK_CLOSE_EXIT(&app_state->sin);
    }
    
    PSOCK_SEND_STR_P(&app_state->sin, PSTR("DATA\r\n")); 
    // read a line and check if it starts with "354" = enter message
    PSOCK_READTO(&app_state->sin,'\n');   
    if(app_state->inputbuffer[0]!='3') {
        smtp_stats.data_err++;
        PSOCK_CLOSE_EXIT(&app_state->sin);
    }
    
    PSOCK_SEND_STR_P(&app_state->sin, PSTR("From: <"));
    readFlashString(app_state->inputbuffer,From);
    PSOCK_SEND_STR(&app_state->sin,app_state->inputbuffer); 
    PSOCK_SEND_STR_P(&app_state->sin, PSTR(">\r\nTo: <"));
    readFlashString(app_state->inputbuffer,To);
    PSOCK_SEND_STR(&app_state->sin,app_state->inputbuffer); 
    PSOCK_SEND_STR_P(&app_state->sin, PSTR(">\r\nSubject: "));
    readFlashString(app_state->inputbuffer,Subject);
    PSOCK_SEND_STR(&app_state->sin,app_state->inputbuffer); 
    PSOCK_SEND_STR_P(&app_state->sin, PSTR("\r\n\r\n"));
    PSOCK_SEND_STR_P(&app_state->sin, PSTR("Sent by Butterfly I/O firmware"));   // message text
    PSOCK_SEND_STR_P(&app_state->sin, PSTR("\r\n.\r\n"));
    // read a line and check if it starts with "250" = ok
    PSOCK_READTO(&app_state->sin,'\n');   
    if(app_state->inputbuffer[0]!='2') {
        smtp_stats.data_err++;
        PSOCK_CLOSE_EXIT(&app_state->sin);
    }
    
    smtp_stats.sent++;
    PSOCK_SEND_STR_P(&app_state->sin, PSTR("QUIT\r\n"));
    // read a line and ignore the answer: "221" = closing connection
    PSOCK_READTO(&app_state->sin,'\n');   
    
    PSOCK_CLOSE_EXIT(&app_state->sin);
    PSOCK_END(&app_state->sin);
}


// called by inetd periodically and in case of connection events

void smtpc_appcall(struct application_state *app_state) {
    if(uip_closed() || uip_aborted()) {
        // Mark the application as non-busy after connection close
        isBusy=0;
    }
    else if (uip_timedout()) {
        isBusy=0;
        smtp_stats.timeout_err++;
    }
    if (uip_connected()) { 
        // connected to the mailserver.
        // set timeout to 20 seconds
        timer_set(&myTimer,CLOCK_SECOND*20); 
	// start a thread that sends the eMail.
	// sin is used for both input and output because this application does only half-duplex communication
	PSOCK_INIT(&app_state->sin, app_state->inputbuffer, sizeof (app_state->inputbuffer));
    }
    if (timer_expired(&myTimer)) {
        smtp_stats.timeout_err++;
        uip_close();
    } else      
        handle_connection(app_state);
}


// Trigger sending an eMail
// The input parameters are adresses of strings in ethernet controller flash memory
// return values:
//   0 = success
//  -1 = application already in use
//  -2 = no resource free for new IP connection

int8_t sendMail(int server, int username, int password, int from, int to, int subject) {
    struct uip_conn *conn;
    u8_t ipaddr[4];
    char serverAddress[20];
    // reject if the application is busy
    if (isBusy) 
      return -1;
    // copy parameters tolocal variables
    Username=username;
    Password=password;
    From=from;
    To=to;
    Subject=subject;
    // try to start a new connection to the mail server
    readFlashString(serverAddress,server);
    parse_ip_address(ipaddr, serverAddress);
    conn=uip_connect((uip_ipaddr_t*)(&ipaddr), HTONS(25));
    if (conn == NULL)
        return -2;    
    // At this point, the connection is not established, we only requested 
    // that uIP shall try to connect. We do not know if that connections
    // establishes successfully.
    isBusy=1;
    return 0;
}
