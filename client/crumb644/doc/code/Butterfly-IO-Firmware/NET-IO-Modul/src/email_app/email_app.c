/*
    Email application. It monitors Port D pins for Low-to-High transitions and sends a notification
    mail for such changes.
    
    All pins of Port D that have a configured message text are monitored. The application enables
    pull-up's for these pins. If you attach capaciators to the inputs, ensure that they load up to
    high level within one second, because monitoring starts after that time.
*/


#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <avr/pgmspace.h>
#include "../driver/CP2200.h"
#include "../uip/uip.h"
#include "../smtpc/smtpc.h"
#include "../httpd/httpd.h"
#include "email_app.h"

// Defines the bits that will be monitored in Port D.
static uint8_t bitmask;

// Job queue for sending eMails. There is one bit for each
// input pin of Port D
static uint8_t queue;

// Last status of port D, used to detect changes.
static uint8_t lastStatus;

// timer, used to disable the port monitor for a while
// after initialisation
static struct timer myTimer;

// Wheter monitoring port D is enabled or not
static uint8_t disabled;

// Addresses of strings in ethernet controllers flash memory
#define ADR_SERVER 512
#define ADR_USER 1024
#define ADR_PASSWORD 1536
#define ADR_FROM 2048
#define ADR_TEXT0 2560
#define ADR_TEXT1 3072
#define ADR_TEXT2 3584
#define ADR_TEXT3 4096
#define ADR_TEXT4 4608
#define ADR_TEXT5 5120
#define ADR_TEXT6 5632
#define ADR_TEXT7 6144
#define ADR_TO 6656

//------------------------------------------------------------------------------------
// Additional function to access the ethernet controllers flash memory
//-----------------------------------------------------------------------------------

// Read a String from the flash memory at the given adress and encode it into HTML.

static void readFlashStringEncoded(char* buffer, u16_t adr) {
    char c;
    write_CP2200(FLASHADDRL,adr & 0xFF);
    write_CP2200(FLASHADDRH,adr >> 8);
    do {
        c=read_CP2200(FLASHAUTORD);
        // Replace ascii 255 by ascii 0.
        // Empty flash cells have that value
        if (c==255)
          c=0;
        // Replace forbidden characters by their escape sequences
        if (c=='&') {
            strcpy_P(buffer,PSTR("&amp;"));
            buffer+=5;
        }
        else if (c=='<') {
            strcpy_P(buffer,PSTR("&lt;"));
            buffer+=4;
        }
        else if (c=='>') {
            strcpy_P(buffer,PSTR("&gt;"));
            buffer+=4;
        }
        else if (c=='\"') {
            strcpy_P(buffer,PSTR("&quot;"));
            buffer+=6;
        }
        else {
            buffer[0]=c;
            buffer++;
        }
    }
    while (c!=0);
}



//------------------------------------------------------------------------------------
// Plugin to the httpd server
//------------------------------------------------------------------------------------


static char nameof_econfig_shtml[]   PROGMEM = "econfig.shtml";
static char nameof_smtp_stats[]      PROGMEM = "smtp-stats";
static char nameof_smtpIp[]          PROGMEM = "smtpIp";
static char nameof_smtpUser[]        PROGMEM = "smtpUser";
static char nameof_smtpPassword[]    PROGMEM = "smtpPw";
static char nameof_from[]            PROGMEM = "from";
static char nameof_to[]              PROGMEM = "to";
static char nameof_text0[]           PROGMEM = "text0";
static char nameof_text1[]           PROGMEM = "text1";
static char nameof_text2[]           PROGMEM = "text2";
static char nameof_text3[]           PROGMEM = "text3";
static char nameof_text4[]           PROGMEM = "text4";
static char nameof_text5[]           PROGMEM = "text5";
static char nameof_text6[]           PROGMEM = "text6";
static char nameof_text7[]           PROGMEM = "text7";
static char hiddenstyle[]            PROGMEM = "display:none";

// process form data from econfig.shtml

static unsigned short function_econfig(char* buffer, char* parameters) {
    // find the settings in parameters and parse them
    url_decode(parameters);
    if (strstr_P(parameters,nameof_smtpIp)==parameters) {
        writeFlashString(parameters+sizeof(nameof_smtpIp),ADR_SERVER);
        email_app_init();
    }
    else if (strstr_P(parameters,nameof_smtpUser)==parameters) {
        writeFlashString(parameters+sizeof(nameof_smtpUser),ADR_USER);
    }
    else if (strstr_P(parameters,nameof_smtpPassword)==parameters) {
        writeFlashString(parameters+sizeof(nameof_smtpPassword),ADR_PASSWORD);
    }
    else if (strstr_P(parameters,nameof_from)==parameters) {
        writeFlashString(parameters+sizeof(nameof_from),ADR_FROM);
    }
    else if (strstr_P(parameters,nameof_text0)==parameters) {
        writeFlashString(parameters+sizeof(nameof_text0),ADR_TEXT0);
        email_app_init();
    }
    else if (strstr_P(parameters,nameof_text1)==parameters) {
        writeFlashString(parameters+sizeof(nameof_text1),ADR_TEXT1);
        email_app_init();
    }
    else if (strstr_P(parameters,nameof_text2)==parameters) {
        writeFlashString(parameters+sizeof(nameof_text2),ADR_TEXT2);
        email_app_init();
    }
    else if (strstr_P(parameters,nameof_text3)==parameters) {
        writeFlashString(parameters+sizeof(nameof_text3),ADR_TEXT3);
        email_app_init();
    }
    else if (strstr_P(parameters,nameof_text4)==parameters) {
        writeFlashString(parameters+sizeof(nameof_text4),ADR_TEXT4);
    }
    else if (strstr_P(parameters,nameof_text5)==parameters) {
        writeFlashString(parameters+sizeof(nameof_text5),ADR_TEXT5);
        email_app_init();
    }    
    else if (strstr_P(parameters,nameof_text6)==parameters) {
        writeFlashString(parameters+sizeof(nameof_text6),ADR_TEXT6);
        email_app_init();
    }  
    else if (strstr_P(parameters,nameof_text7)==parameters) {
        writeFlashString(parameters+sizeof(nameof_text7),ADR_TEXT7);
        email_app_init();
    }  
    else if (strstr_P(parameters,nameof_to)==parameters) {
        writeFlashString(parameters+sizeof(nameof_to),ADR_TO);
        email_app_init();
    }  
    // return a dummy because a null string is not allowed
    buffer[0]=' ';
    buffer[1]=0;
    return 1;
}




// Display statistics for the SMTP-Counters page

static char smtp_stat_formatter[] PROGMEM = "%5u\n%5u\n%5u\n%5u\n%5u\n%5u\n%5u";

static unsigned short function_smtp_stats(char* buffer,  unsigned short bufsize) {
    return snprintf_P(buffer, bufsize, smtp_stat_formatter,
        smtp_stats.sent,
        smtp_stats.timeout_err,
        smtp_stats.ehlo_err,
        smtp_stats.auth_err,
        smtp_stats.from_err,
        smtp_stats.rcpt_err,
        smtp_stats.data_err);
}



// Call functions for dynamic websites. If the function name is known, then
// it returns the number of bytes of the result. Otherwise it returns -1.
// name is the function name
// parameters is the parameter string (part from the URL after the question mark)
// buffer is the destination for the result string
// bufsize is the size of the buffer

int email_httpd_functions(char* buffer, unsigned short bufsize, char* name, char* parameters) {
    buffer[0]=0;   
    if (strcmp_P(name,nameof_econfig_shtml)==0) {
        function_econfig(buffer,parameters);
    }
    else if (strcmp_P(name,nameof_smtp_stats)==0) {
        function_smtp_stats(buffer,bufsize);
    }
    else if (strcmp_P(name,nameof_smtpIp)==0) {
        readFlashStringEncoded(buffer,ADR_SERVER);
    }
    else if (strcmp_P(name,nameof_smtpUser)==0) {
        readFlashStringEncoded(buffer,ADR_USER);
    }
    else if (strcmp_P(name,nameof_smtpPassword)==0) {
        readFlashStringEncoded(buffer,ADR_PASSWORD);
    }
    else if (strcmp_P(name,nameof_from)==0) {
        readFlashStringEncoded(buffer,ADR_FROM);
    }
    else if (strcmp_P(name,nameof_text0)==0) {
        readFlashStringEncoded(buffer,ADR_TEXT0);
    }
    else if (strcmp_P(name,nameof_text1)==0) {
        readFlashStringEncoded(buffer,ADR_TEXT1);
    }
    else if (strcmp_P(name,nameof_text2)==0) {
        readFlashStringEncoded(buffer,ADR_TEXT2);
    }
    else if (strcmp_P(name,nameof_text3)==0) {
        readFlashStringEncoded(buffer,ADR_TEXT3);
    }
    else if (strcmp_P(name,nameof_text4)==0) {
        readFlashStringEncoded(buffer,ADR_TEXT4);
    }
    else if (strcmp_P(name,nameof_text5)==0) {
        readFlashStringEncoded(buffer,ADR_TEXT5);
    }
    else if (strcmp_P(name,nameof_text6)==0) {
        readFlashStringEncoded(buffer,ADR_TEXT6);
    }
    else if (strcmp_P(name,nameof_text7)==0) {
        readFlashStringEncoded(buffer,ADR_TEXT7);
    }
    else if (strcmp_P(name,nameof_to)==0) {
        readFlashStringEncoded(buffer,ADR_TO);
    }
    else {
        return -1;
    }
    return strlen(buffer);
}


//------------------------------------------------------------------------------------
// Plugin to the main loop
//------------------------------------------------------------------------------------

// Initialize the email application

void email_app_init(void) {
    bitmask=0;
    queue=0;
    disabled=1;
    // Enable port monitoring only if server and recipient are configured
    if (readFlashChar(ADR_SERVER) && readFlashChar(ADR_TO)) {    
        // Check what text settings are not empty and enable mnitoring the corresponding bits
        if (readFlashChar(ADR_TEXT0)) {
            bitmask|=1;
        }
        if (readFlashChar(ADR_TEXT1)) {
            bitmask|=2;
        }
        if (readFlashChar(ADR_TEXT2)) {
            bitmask|=4;
        }
        if (readFlashChar(ADR_TEXT3)) {
            bitmask|=8;
        }
        if (readFlashChar(ADR_TEXT4)) {
            bitmask|=16;
        }
        if (readFlashChar(ADR_TEXT5)) {
            bitmask|=32;
        }
        if (readFlashChar(ADR_TEXT6)) {
            bitmask|=64;
        }
        if (readFlashChar(ADR_TEXT7)) {
            bitmask|=128;
        }   
        // enable pull-ups on all PIN's that we monitor
        PORTD |= bitmask;
        // disable monitoring for one second
        timer_set(&myTimer, CLOCK_SECOND);   
        // note current status of Port D
        lastStatus=PIND;
    }
}


// Called from the main loop as often as possible
// For each PIN that changed from low to high, send an eMail.
// If sending failed, retry when the function gets called next time.

void email_app_main_loop(void) {
  
    // Check if disable-timer has expired
    if (disabled) {
        if (timer_expired(&myTimer))
          disabled=0;
        return;
    }
  
    // Find out what pins changed from low to high and set those bits in the queue
    queue |= PIND & bitmask & (~lastStatus);
    lastStatus=PIND;
    
    // For each bit in the queue trigger sensing an eMail.
    // If it fails, retry during next loop.    
    if (queue & 1) { // has PD0 changed?
        if (sendMail(ADR_SERVER,ADR_USER,ADR_PASSWORD,ADR_FROM,ADR_TO,ADR_TEXT0)==0) 
            queue &= ~1;
    }
    else if (queue & 2) {
        if (sendMail(ADR_SERVER,ADR_USER,ADR_PASSWORD,ADR_FROM,ADR_TO,ADR_TEXT1)==0) 
            queue &= ~2;
    }
    else if (queue & 4) { 
        if (sendMail(ADR_SERVER,ADR_USER,ADR_PASSWORD,ADR_FROM,ADR_TO,ADR_TEXT2)==0) 
            queue &= ~4;
    }
    else if (queue & 8) {
        if (sendMail(ADR_SERVER,ADR_USER,ADR_PASSWORD,ADR_FROM,ADR_TO,ADR_TEXT3)==0) 
            queue &= ~8;
    }
    else if (queue & 16) { 
         if (sendMail(ADR_SERVER,ADR_USER,ADR_PASSWORD,ADR_FROM,ADR_TO,ADR_TEXT4)==0) 
            queue &= ~16;
    }
    else if (queue & 32) {
         if (sendMail(ADR_SERVER,ADR_USER,ADR_PASSWORD,ADR_FROM,ADR_TO,ADR_TEXT5)==0) 
            queue &= ~32;
    }
    else if (queue & 64) { 
        if (sendMail(ADR_SERVER,ADR_USER,ADR_PASSWORD,ADR_FROM,ADR_TO,ADR_TEXT6)==0) 
            queue &= ~64;
    }
    else if (queue & 128) {
        if (sendMail(ADR_SERVER,ADR_USER,ADR_PASSWORD,ADR_FROM,ADR_TO,ADR_TEXT7)==0) 
            queue &= ~128;
    }
}



