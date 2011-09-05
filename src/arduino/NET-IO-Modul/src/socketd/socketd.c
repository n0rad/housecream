/*
Socket server to call user-functions or io-commands through a RAW TCP socket.
*/

#include "../uip/uip.h"
#include "socketd.h"
#include "../io-commands.h"
#include <string.h>
#include <avr/pgmspace.h>
#include "../httpd/httpd.h"
#include "../driver/CP2200.h"


// Calls a function by name, using the uip buffer for output.
// Returns the number of bytes written to the buffer

static unsigned short socketd_function_dispatcher(void* state) {
    struct application_state* app_state=state;
    io_command(uip_appdata,app_state->inputbuffer);
    return strlen(uip_appdata);
}


// Do something when a connection has been established
static PT_THREAD(handle_connection(struct application_state *app_state))
{
    PSOCK_BEGIN(&app_state->sin);
    #ifdef AUTH
        app_state->isAuthenticated=0;
    #endif // AUTH
    //PSOCK_SEND_STR_P(&app_state->sin, PSTR("Welcome\n"));
       
    // endless loop until connection lost
    while (1) {
    
	// read until newline character
	PSOCK_READTO(&app_state->sin, '\n');
	
	// Search for backspace characters and 
	// delete each as well as the previous character.
	int i=0;
	while (1) {
	    char c=app_state->inputbuffer[i];
	    if (c=='\b' && i>0) {
		strcpy(app_state->inputbuffer+i-1,app_state->inputbuffer+i+1);
		i--;
	    }
	    else if (c=='\n' || c=='\r' || c==0) {
		app_state->inputbuffer[i]=0;
		break;
	    }
	    i++;
	}
	
	// Terminate the command in inputbuffer with 0
	i=0;
	while (1) {
	    char c=app_state->inputbuffer[i];
            if (c=='\n' || c=='\r' || c==0) {
		app_state->inputbuffer[i]=0;
		break;
	    }
	    i++;
	}
	
	// check for exit or quit command
	if (strcmp_P(app_state->inputbuffer,PSTR("quit"))==0 || strcmp_P(app_state->inputbuffer,PSTR("exit"))==0) {
	    PSOCK_CLOSE_EXIT(&app_state->sin);
	}
#ifdef AUTH
        // check for auth command if an authentication username/password is configured
        else if (strncmp_P(app_state->inputbuffer,PSTR("auth"),4)==0 && config.authentication[0]!=0) {
            // compare the confgured authentication key with the input
            if (strcmp(config.authentication,app_state->inputbuffer+5)==0) {
                PSOCK_SEND_STR_P(&app_state->sin,PSTR("Ok\n"));
                app_state->isAuthenticated=1;
            }
            else {
                PSOCK_SEND_STR_P(&app_state->sin,PSTR("failed\n"));
            }
        }
        // if user is not authenticated, tell him to do that
        else if (app_state->isAuthenticated==0 && config.authentication[0]!=0) {
                PSOCK_SEND_STR_P(&app_state->sin,PSTR("auth required\n"));
        }
#endif // AUTH

        // insert other commands here
        // ...

        // all other commands are assumed to be i/o commands from io_commands.c
	else if (app_state->inputbuffer[0]!=0) {           
            PSOCK_GENERATOR_SEND(&app_state->sin, socketd_function_dispatcher, app_state);
        }
    }

    PSOCK_END(&app_state->sin);
}

// Process TCP connection. This is called by inetd in case of events 
// related to a connection and also periodically. 

void socketd_appcall(struct application_state *app_state) {
    // is this a new connection?
    if (uip_connected()) { 
	// Initialize Protosocket. 
	// sin is used for both input and output because this application does only half-duplex communication
	PSOCK_INIT(&app_state->sin, app_state->inputbuffer, sizeof (app_state->inputbuffer));
    }
    handle_connection(app_state);
}


