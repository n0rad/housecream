#ifndef __EMAIL_APP_H__
#define __EMAIL_APP_H__


// Plugin to httpd server.
// Call functions for dynamic websites. If the function name is known, then
// it returns the number of bytes of the result. Otherwise it returns -1.
// name is the function name
// parameters is the parameter string (part from the URL after the question mark)
// buffer is the destination for the result string
// bufsize is the size of the buffer
int email_httpd_functions(char* buffer, unsigned short bufsize, char* name, char* parameters);


// Called from the main loop as often as possible
void email_app_main_loop(void);


// Initialize the email application
void email_app_init(void);
  
#endif // __EMAIL_APP_H__ 

