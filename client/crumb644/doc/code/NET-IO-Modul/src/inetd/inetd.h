#ifndef __INETD_H__
#define __INETD_H__

#include "../uip/psock.h"
#include "../httpd/httpd-fs.h"

#define STATE_INPUT   0
#define STATE_OUTPUT  1
#define STATE_CLOSE   2

// Application state data, that are needed to process an TCP
// connection.

// For each possible connection, the memory contains one static
// set of this structure, which is used by all inet applications
// (currently socketd and httpd. dhcpd uses udp protocol which
// is not related to this structure).

// Because of the nature of protothreads and protosockets, the
// applications cannot store their state in stack variables,
// they must store everything here.


struct application_state {
  
// Protosockets for input and output
  struct psock sin, sout;
   
// Protothreads for generating output
  struct pt outputpt,scriptpt;
  
// The size of the input buffer limits the length of commands in socket connection and the length of URL's in HTTP requests.
  #define inputbuffer_size 100

// Buffer for input. 
// There is no output buffer because we write directly into the uIP appdata buffer.
  char inputbuffer[inputbuffer_size];
  
#ifdef AUTH
// Buffer for backup of the URL while processing other lines of input.
  char authbuffer[inputbuffer_size];
  
// Flag if authentication has been done
  char isAuthenticated;
#endif // AUTH
  
// step of processing (input or output)
  char state;

// pointer to the filename or function name (within inputbufer)
  char* filename;

// pointer to the GET or function parameters (within inputbufer)
  char* parameters;
  
// Pointer to file content and length of the current file.
// During processing, the pointer gets increased,
// and the length value gets decreased.
  struct httpd_fs_file file;

// Backup of file, used when loading an include file.
  struct httpd_fs_file file_backup;

// length of the current part that we want to send, and
// after sending, the number of bytes that have been sent which may be
// less that the requested length.
  int partlen;
};



// Interface to uIP, see uipopt.h line 486.

void inetd_init(void);
void inetd_appcall(void);

#define UIP_APPCALL     inetd_appcall
typedef struct application_state uip_tcp_appstate_t;

#endif /* __INETD_H__ */
