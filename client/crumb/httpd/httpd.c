/*
Minimalistic web server, that loads files from program memory (flash).
It supports include files and it can execute functions to generate
dynamic content.

The filesystem is in httpd-fsdata.c. All filenames
begin with a slash to indicate that they reside in the root
directory. Space characters are not allowed in filenames.
Subdirectories are not supported. 

Template files for scripts have filename pattern *.shtml.
These files must be written with an 8-bit encoding, e.g.
iso8859-1. Within shtml files, two types of scripting markers
are supported:

%!: file_name              includes a html fragment file here.
%! function_name?params    executes a function 

Functions can also be called via URL, such as
http://hostname/function?parameters. To call a function via URL that does not
use parameters, enter any dummy parameter, otherwise httpd thinks that
this is a regular filename and no function call. The function "io?params" 
calls functions from user-functions.c or io-commands.c.

If the function name is the same as a filename, then the webbrowser
first executes that function, then returns the functions result string
and then returns the content of that file. 

Include files cannot include other files. Nesting of include files is not
supported.

The size of GET request (from GET until end of parameters) must be smaller
than the inputbuffer. The inputbuffer size is defined in httpd-h.

*/

#include "../uip/uip.h"
#include "httpd.h"
#include "httpd-fs.h"
#include "httpd-functions.h"
#include "../inetd/inetd.h"
#include "../driver/CP2200.h"
#include <string.h>
#include <avr/io.h>
#include <avr/pgmspace.h>
#include "../main.h"

#ifdef AUTH
#include "base64.h"
#endif // AUTH



static char method_get[]        PROGMEM="GET ";
static char nameof_index_file[] PROGMEM="/index.shtml";
static char nameof_404_html[]   PROGMEM="/404.html";

// Filename suffixes, used by send_headers to determine content type

static char suffix_html[]  PROGMEM=".html";
static char suffix_shtml[] PROGMEM=".shtml";
static char suffix_css[]   PROGMEM=".css";
static char suffix_png[]   PROGMEM=".png";
static char suffix_gif[]   PROGMEM=".gif";
static char suffix_jpg[]   PROGMEM=".jpg";
static char suffix_jpeg[]  PROGMEM=".jpeg";
static char suffix_txt[]   PROGMEM=".txt";
static char suffix_xml[]   PROGMEM=".xml";

// HTTP content types, used by send_headers

static char type_html[]    PROGMEM="Content-type: text/html\r\n\r\n"; // HTML files can set a no-cache header as meta tag, if required
static char type_css[]     PROGMEM="Content-type: text/css\r\n\r\n";
static char type_png[]     PROGMEM="Content-type: image/png\r\n\r\n";
static char type_gif[]     PROGMEM="Content-type: image/gif\r\n\r\n";
static char type_jpeg[]    PROGMEM="Content-type: image/jpeg\r\n\r\n";
static char type_xml[]     PROGMEM="Content-type: text/xml\r\n\r\n";
static char type_plain[]   PROGMEM="Content-type: text/plain\r\nCache-Control: no-cache\r\r\nPragma: no-cache\r\n\r\n";
static char type_unknown[] PROGMEM="Cache-Control: no-cache\r\nPragma: no-cache\r\n\r\n";

// HTTP Status headers

static char status_200[]   PROGMEM="HTTP/1.1 200 OK\r\nConnection: close\r\n";
static char status_404[]   PROGMEM="HTTP/1.1 404 Not found\r\nConnection: close\r\n";

#ifdef AUTH
// HTTP Header for authorisation
static char auth_header[]  PROGMEM="Authorization: Basic "; // followed by the base64 encrypted authentication key, followed by a line break
static char status_401[]   PROGMEM="HTTP/1.1 401 Auth Required\r\nWWW-Authenticate: Basic realm=\"Controller\"\r\nConnection: close\r\n\r\nAuth Required\r\n";
#endif // AUTH



// decode an URL parameter. The result is stored where the source came from.
// The parameter must be terminated with an ascii 0 or an & character.

void url_decode(char* parameter) {
    char high_nibble;
    char low_nibble;
    while (parameter[0]!=0) {
         if (parameter[0]=='+')
             parameter[0]=' ';
         else if (parameter[0]=='%') {
             // convert the hexadecimal number that follows the "%" to char
             if (parameter[1]<='9') 
                 high_nibble=parameter[1]-'0';
             else 
                 high_nibble=parameter[1]-'A'+10;
             if (parameter[2]<='9') 
                 low_nibble=parameter[2]-'0';
             else 
                 low_nibble=parameter[2]-'A'+10;
             char c=(high_nibble<<4)+low_nibble;
             // replace the "%" char by the new character
             parameter[0]=c;
             //shift the remaining string 2 characters forward.
             memmove(parameter+1,parameter+3,strlen(parameter+3)+1);
         }       
         else if (parameter[0]=='&') {
            parameter[0]=0;
            break;
         }
         parameter++;
    }
}



// Call a function and send its result

static PT_THREAD(call_function(struct application_state *app_state)) {
    PSOCK_BEGIN(&app_state->sout);
    PSOCK_GENERATOR_SEND(&app_state->sout, httpd_function_dispatcher, app_state);
    PSOCK_END(&app_state->sout);
}


// copy the max. allowed number of bytes from file to the output
// buffer uip_appdata.
// After sending, app_state->partlen contains the number of bytes that have been sent,
// which might be less than requested.

static unsigned short generate_part_of_file_P(void *app_state) {
    struct application_state *state = (struct application_state *) app_state;
    if (state->file.len > uip_mss()) {
        state->partlen = uip_mss();
    } else {
        state->partlen = state->file.len;
    }
    memcpy_P(uip_appdata, state->file.data, state->partlen);
    return state->partlen;
}


// Send the whole file app_state->file

static PT_THREAD(send_file_P(struct application_state *app_state)) {
    PSOCK_BEGIN(&app_state->sout);
    do {
        PSOCK_GENERATOR_SEND(&app_state->sout, generate_part_of_file_P, app_state);
        // Move the file pointer forward to prepare for sending the next part-
        app_state->file.len -= app_state->partlen;
        app_state->file.data += app_state->partlen;
    }
    while (app_state->file.len > 0);
    PSOCK_END(&app_state->sout);
}


// Send app_state->partlen bytes of a file starting at app_state->file.data.
// After sending, app_state->partlen contains the number of bytes that have been sent,
// which might be less than requested.

static PT_THREAD(send_part_of_file(struct application_state *app_state)) {
    PSOCK_BEGIN(&app_state->sout);
    PSOCK_SEND_P(&app_state->sout, app_state->file.data, app_state->partlen);
    PSOCK_END(&app_state->sout);
}


// Find the next line break or sapce or asterisk in file data.
// len is the maximum number of bytes to search in.
// Returns the index of the next line break or the string end.

static int next_linebreak_or_space_or_asterisk_P(char* data, int len) {
    int i = 0;
    do {
        char c=pgm_read_byte(data + i);
        if (c=='\n' || c=='\r' || c==' ' || c=='"' || c==0)
            return i;
        i++;
    }
    while (i < len);
    return len;
}


// Find the next script marker in file data.
// len is the maximum number of bytes to search in.
// Returns the index of the nextscript marker or -1.

static int next_scriptmarker_P(char* data, int len) {
    int i = 0;
    len--;
    do {
        if (pgm_read_byte(data + i) == '%' && pgm_read_byte(data + i + 1) == '!')
            return i;
        i++;
    }
    while (i < len);
    return -1;
}


// process the shtml file app_state->file
// This is done in a loop that splits the file data into parts, where the delimiters
// are script markers.

static PT_THREAD(handle_script(struct application_state *app_state)) {
    PT_BEGIN(&app_state->scriptpt);
    // repeat until end of file data
    while (app_state->file.len > 0) {
        // check if the data start with a script marker
        int marker = next_scriptmarker_P(app_state->file.data, 1);
        if (marker >= 0) {
            // check if this is an include command
            if (pgm_read_byte(app_state->file.data + 2) == ':') {
                // it is an include file
                // safe the current file data pointer+length because sending an include file will overwrite them.
                app_state->file_backup.data = app_state->file.data;
                app_state->file_backup.len = app_state->file.len;
                app_state->file_backup.index = app_state->file.index;
                // open and send the include file
                int index=httpd_fs_open_P(app_state->file.data + 4, &app_state->file); // filename,destination
                if (index>=0) {
                    PT_WAIT_THREAD(&app_state->scriptpt, send_file_P(app_state));
                    // count access to the file that has been sent
                    httpd_fs_countAccess(app_state->file.index);
                }
                // restore the saved data pointer+length
                app_state->file.data = app_state->file_backup.data;
                app_state->file.len = app_state->file_backup.len;
                app_state->file.index = app_state->file_backup.index;
            } 
            else {
                // it is a function call
                // Copy function name and parameters to the input buffer and
                // set the filename and parameters pointers to the correct positions
                // withing inputbuffer and terminate these strings with a 0.
                app_state->filename=app_state->inputbuffer;
                app_state->parameters=NULL;
                int i=0;
                while (1) {
                    char c=pgm_read_byte(app_state->file.data+3+i);
                    if (c=='?') {
                         app_state->parameters=app_state->inputbuffer+i+1;
                         app_state->inputbuffer[i]=0;
                    }
                    else if (c==' ' || c=='\r' || c=='\n' || c=='"' || c==0) {
                        app_state->inputbuffer[i]=0;
                        break;
                    }
                    else {
                        app_state->inputbuffer[i]=c;
                    }
                    i++;
                }
                if (app_state->parameters==NULL) {
                    app_state->parameters=app_state->inputbuffer+i;
                }
                // call the function dispatcher
                PT_WAIT_THREAD(&app_state->scriptpt, call_function(app_state));
            }
            // restore the saved data pointer+length
            // and move the file pointer to the end of the current script tag
            int linebreak = next_linebreak_or_space_or_asterisk_P(app_state->file.data+4, app_state->file.len-4);
            app_state->file.data+=linebreak+4;
            app_state->file.len-=linebreak+4;

        } else {
            // the data does not start with a script marker

            // see if the data contains a script marker somewhere else
            int scriptmarker = next_scriptmarker_P(app_state->file.data, app_state->file.len);
            if (scriptmarker >= 0) {
                // limit the part length to the max. allowed size (uip_mss)
                if (scriptmarker > uip_mss()) {
                    app_state->partlen = uip_mss();
                    // We decrease uip_mss by 8 to improve performance. This hack is described in the uIP documentation.
                } else {
                    app_state->partlen = scriptmarker;
                }
                // send the part that comes before the next script mark
                PT_WAIT_THREAD(&app_state->scriptpt, send_part_of_file(app_state));
                // move the file pointer forward to prepare sending the next part.
                app_state->file.data += app_state->partlen;
                app_state->file.len -= app_state->partlen;
            } else {
                // there is no script marker anymore
                // send the whole file
                PT_WAIT_THREAD(&app_state->scriptpt, send_file_P(app_state));
                app_state->file.len = 0; // reached end of file, end the loop
            }
        }
    }
    PT_END(&app_state->scriptpt);
}


// Send HTTP headers, depending on the filename suffix

static PT_THREAD(send_headers(struct application_state *app_state, const char *statushdr)) {
    PSOCK_BEGIN(&app_state->sout);
    PSOCK_SEND_STR_P(&app_state->sout, statushdr);
    char* ptr = strrchr(app_state->filename, '.');
    if (ptr == NULL) {
        PSOCK_SEND_STR_P(&app_state->sout, type_unknown);
    } else if (strcmp_P(ptr, suffix_html) == 0) {
        PSOCK_SEND_STR_P(&app_state->sout, type_html);
    } else if (strcmp_P(ptr, suffix_shtml) == 0) {
        PSOCK_SEND_STR_P(&app_state->sout, type_html);
    } else if (strcmp_P(ptr, suffix_css) == 0) {
        PSOCK_SEND_STR_P(&app_state->sout, type_css);
    } else if (strcmp_P(ptr, suffix_png) == 0) {
        PSOCK_SEND_STR_P(&app_state->sout, type_png);
    } else if (strcmp_P(ptr, suffix_gif) == 0) {
        PSOCK_SEND_STR_P(&app_state->sout, type_gif);
    } else if (strcmp_P(ptr, suffix_jpg) == 0) {
        PSOCK_SEND_STR_P(&app_state->sout, type_jpeg);
    } else if (strcmp_P(ptr, suffix_jpeg) == 0) {
        PSOCK_SEND_STR_P(&app_state->sout, type_jpeg);
    } else if (strcmp_P(ptr, suffix_txt) == 0) {
        PSOCK_SEND_STR_P(&app_state->sout, type_plain);
    } else {
        PSOCK_SEND_STR_P(&app_state->sout, type_unknown);
    }
    PSOCK_END(&app_state->sout);
}



// Generate output

static PT_THREAD(handle_output(struct application_state *app_state)) {
    PT_BEGIN(&app_state->outputpt);
    
#ifdef AUTH
    // check if the caller is authenticated
    if (app_state->isAuthenticated==0) {
        // send a HTTP 401 header = Auth Required
        PSOCK_SEND_STR_P(&app_state->sout, status_401);
    }
    else 
#endif // AUTH
    {
        // try to load the file
        httpd_fs_open(app_state->filename, &app_state->file);

        // If we have parameters, then this is a direct function call
        if (app_state->parameters[0]!=0) {
            // send a HTTP header
            PT_WAIT_THREAD(&app_state->outputpt, send_headers(app_state, status_200));
            // call the function dispatcher. The function name is the filename
            // but without the first slash.
            app_state->filename++;
            PT_WAIT_THREAD(&app_state->outputpt, call_function(app_state));
            app_state->filename--; // restore the filename
            // if there is a file with the same name as the function, then load and display it.
            if (app_state->file.index >=0 ) {
                // If the file is an .shtml file, then spawn a new sub thread that
                // processes the script file.
                char *ptr = strchr(app_state->filename, '.');
                if (ptr != NULL && strcmp_P(ptr, suffix_shtml) == 0) {
                    PT_INIT(&app_state->scriptpt);
                    PT_WAIT_THREAD(&app_state->outputpt, handle_script(app_state));
                }
                else {
                    // It's not a script file, send the file
                    PT_WAIT_THREAD(&app_state->outputpt, send_file_P(app_state));
                }
                // count access to the file that has been sent
                httpd_fs_countAccess(app_state->file.index);
            }
        }
        else {
            // If we dont have parameters, it is not a function call.
            // If the file does not exists, return HTTP error page
            if (app_state->file.index < 0) {
                httpd_fs_open_P(nameof_404_html, &app_state->file);
                strcpy_P(app_state->filename, nameof_404_html);
                PT_WAIT_THREAD(&app_state->outputpt, send_headers(app_state, status_404));
                PT_WAIT_THREAD(&app_state->outputpt, send_file_P(app_state));
            }
            else {
                // File exists, send HTTP status 200 and filename specific header
                PT_WAIT_THREAD(&app_state->outputpt, send_headers(app_state, status_200));
                // If the file is an .shtml file, then spawn a new sub thread that
                // processes the script file.
                char *ptr = strchr(app_state->filename, '.');
                if (ptr != NULL && strcmp_P(ptr, suffix_shtml) == 0) {
                    PT_INIT(&app_state->scriptpt);
                    PT_WAIT_THREAD(&app_state->outputpt, handle_script(app_state));
                }
                else {
                    // It's not a script file, send the file
                    PT_WAIT_THREAD(&app_state->outputpt, send_file_P(app_state));
                }
            }
            // count access to the file that has been sent
            httpd_fs_countAccess(app_state->file.index);
        }
    }        
    PSOCK_CLOSE(&app_state->sout);
    app_state->state = STATE_CLOSE;
    PT_END(&app_state->outputpt);
}


// Read the incoming HTTP request "GET /filename?parameters HTTP/1.1..."

static PT_THREAD(handle_input(struct application_state *app_state)) {
    PSOCK_BEGIN(&app_state->sin);
    // read until the first space to get the HTTP request method
    PSOCK_READTO(&app_state->sin, ' ');
    // check if this is a GET request
    if (strncmp_P(app_state->inputbuffer, method_get, sizeof(method_get)-1) != 0) {
        PSOCK_CLOSE_EXIT(&app_state->sin);
    }
    // read until the second space to get the filename and parameters
    PSOCK_READTO(&app_state->sin, ' ');
    // check if the filename starts with a slash
    if (app_state->inputbuffer[0] != '/') {
        PSOCK_CLOSE_EXIT(&app_state->sin);
    }
    DEBUG_PRINTLN(app_state->inputbuffer);
    // if the filename is "/", then replace it by the name of the index file
    if (app_state->inputbuffer[1] == ' ') {
        strcpy_P(app_state->inputbuffer, nameof_index_file);
    }
    // Set the filename and parameters pointers to the correct positions
    // withing inputbuffer and terminate these strings with a 0.
    app_state->filename=app_state->inputbuffer;
    app_state->parameters=NULL;
    int i=0;
    while (1) {
        char c=app_state->inputbuffer[i];
        if (c=='?') {
             app_state->parameters=app_state->inputbuffer+i+1;
             app_state->inputbuffer[i]=0;
        }
        else if (c==' ' || c==0) {
            app_state->inputbuffer[i]=0;
            break;
        }
        i++;
    }
    if (app_state->parameters==NULL) {
        app_state->parameters=app_state->inputbuffer+i;
    }
    
#ifdef AUTH
    if (config.authentication[0]==0) {
        app_state->isAuthenticated=1;
    }   
    else {
        app_state->isAuthenticated=0;
        // back-up the filename and params
        memcpy(app_state->authbuffer,app_state->inputbuffer,sizeof(app_state->authbuffer));
        // read the HTTP headers until an empty line, which marks the end of the headers
        do {
            PSOCK_READTO(&app_state->sin, '\n');   
            // check if we got an authorisation header
            if (strncmp_P(app_state->inputbuffer, auth_header, sizeof(auth_header)-1) == 0) {
                char* receivedpw=app_state->inputbuffer+sizeof(auth_header)-1;
                // remove line break and space after received key
                int i=0;
                while (receivedpw[i]!='\n' && receivedpw[i]!='\r' && receivedpw[i]!=' ') {
                  i++;
                }
                receivedpw[i]=0;
                // We store the encoded authentication key at the end of the input buffer, 
                // where we have some free space. This reduces RAM usage.
                char* encodedpw=app_state->inputbuffer+60;
                encode64(encodedpw,config.authentication);
                // compare the received and the encoded configured keys.
                if (strcmp(encodedpw,receivedpw)==0) {
                    app_state->isAuthenticated=1;
                }
                break; // skip all following headers, we are not interested in them
            }
        } 
        while (app_state->inputbuffer[0]!='\r' && app_state->inputbuffer[0]!='\n'); // empty line
        // recover the inputbuffer which holds filename and parameters
        memcpy(app_state->inputbuffer,app_state->authbuffer,sizeof(app_state->inputbuffer));
    }
#endif // AUTH
    
    // enable generation of output
    app_state->state = STATE_OUTPUT;
    PSOCK_END(&app_state->sin);
}


// Process application events. This is called by uIP whenever an event related to
// the connection occurs and also periodically

void httpd_appcall(struct application_state *app_state) {
    if (uip_closed() || uip_aborted() || uip_timedout()) {
	// ignore
    }
    else if (uip_connected()) { // is this a new connection?
	// Initialize Protosockets for input and output
	PSOCK_INIT(&app_state->sin, app_state->inputbuffer, sizeof (app_state->inputbuffer));
	PSOCK_INIT(&app_state->sout, app_state->inputbuffer, sizeof (app_state->inputbuffer));
	// Initialize Protothread for producing output
	PT_INIT(&app_state->outputpt);
	app_state->outputpt=app_state->outputpt;
	app_state->state = STATE_INPUT;
	handle_input(app_state);
	if (app_state->state == STATE_OUTPUT) {
	    handle_output(app_state);
	}
    }
    else if (app_state != NULL) {
	// process events for an open connection
	if (app_state->state == STATE_INPUT) {
	    handle_input(app_state);
	}
	if (app_state->state == STATE_OUTPUT) {
	    handle_output(app_state);
	}
        /*
        if (app_state->state == STATE_CLOSE) {
            uip_close();
        }
        */
    }
}



