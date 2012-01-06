/**
 * \addtogroup apps
 * @{
 */

/**
 * \defgroup httpd Web server
 * @{
 * The uIP web server is a very simplistic implementation of an HTTP
 * server. It can serve web pages and files from a read-only ROM
 * filesystem, and provides a very small scripting language.

 */

/**
 * \file
 *         Web server
 * \author
 *         Adam Dunkels <adam@sics.se>
 */


/*
 * Copyright (c) 2004, Adam Dunkels.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * This file is part of the uIP TCP/IP stack.
 *
 * Author: Adam Dunkels <adam@sics.se>
 *
 * $Id: httpd.c,v 1.2 2006/06/11 21:46:38 adam Exp $
 */

#include "uip.h"
#include "httpd.h"
#include "httpd-fs.h"
#include "httpd-cgi.h"
#include "http-strings.h"

#include <string.h>

#define STATE_WAITING 0
#define STATE_OUTPUT  1

#define ISO_nl      0x0a
#define ISO_space   0x20
#define ISO_bang    0x21
#define ISO_percent 0x25
#define ISO_period  0x2e
#define ISO_slash   0x2f
#define ISO_colon   0x3a

#include <avr/pgmspace.h>

extern void uart_puts_P (PGM_P s);
extern void uart_puts (char *s);
extern int uart_putc(unsigned char c);
unsigned int t=0;



/*---------------------------------------------------------------------------*/
static unsigned short
generate_part_of_file(void *state)
{
  struct httpd_state *s = (struct httpd_state *)state;

  if(s->file.len > uip_mss()) {
    s->len = uip_mss();
  } else {
    s->len = s->file.len;
  }
  memcpy_P(uip_appdata, s->file.data, s->len);
	
	/*uart_puts_P(PSTR("\n\r\x1B[1mAppdata\x1B[0m:>>>"));
	for (t=0;t<s->len;t++)
		if (((char*)uip_appdata)[t]!='\n')
			//uart_putc( ((unsigned char *)uip_appdata)[t]);
			uart_putc(((char*)uip_appdata)[t]);
	uart_puts_P(PSTR("<<<\r\n"));*/
  
  return s->len;
}
/*---------------------------------------------------------------------------*/
static
PT_THREAD(send_file(struct httpd_state *s))
{
	PSOCK_BEGIN(&s->sout);
	//uart_puts_P(PSTR("\n\r\x1B[1mSend File\x1B[0m"));
	do {
    PSOCK_GENERATOR_SEND(&s->sout, generate_part_of_file, s);
    s->file.len -= s->len;
    s->file.data += s->len;
  } while(s->file.len > 0);
	PSOCK_END(&s->sout);
}
/*---------------------------------------------------------------------------*/
static
PT_THREAD(send_part_of_file(struct httpd_state *s))
{
  PSOCK_BEGIN(&s->sout);


	/*uart_puts_P(PSTR("\n\r\x1B[1mSendPartOfFile\x1B[0m :>>>"));
	for (t=0;t<s->len;t++)
		if (((char*)uip_appdata)[t]!='\n')
			//	uart_putc( ((unsigned char *)uip_appdata)[t]);
			uart_putc(pgm_read_byte((char *)(s->file.data+t)));
	uart_puts_P(PSTR("<<<\r\n"));*/
  
  PSOCK_SEND_P(&s->sout, s->file.data, s->len);
  
  PSOCK_END(&s->sout);
}
/*---------------------------------------------------------------------------*/
/*char *strchr_P(char *s, char c)
{
	while ((pgm_read_byte(s) != 0) && (pgm_read_byte(s) != c))
		s++;
	if (pgm_read_byte(s) == 0)
		return(0);
	else
		return(s);
}*/
/*---------------------------------------------------------------------------*/
static void
next_scriptstate(struct httpd_state *s)
{
  char *p;
  p = (char *) strchr_P(s->scriptptr, ISO_nl) + 1;
  s->scriptlen -= (unsigned short)(p - s->scriptptr);
  s->scriptptr = p;
}
/*---------------------------------------------------------------------------*/
static
PT_THREAD(handle_script(struct httpd_state *s))
{
  char *ptr;
	PT_BEGIN(&s->scriptpt);
	uart_puts_P(PSTR("\r\nSCRIPT"));
	while(s->file.len > 0) {

    /* Check if we should start executing a script. */
    if((pgm_read_byte(s->file.data) == ISO_percent) &&
       (pgm_read_byte(s->file.data + 1) == ISO_bang)) {
      s->scriptptr = s->file.data + 3;
      s->scriptlen = s->file.len - 3;
			if(pgm_read_byte(s->scriptptr - 1) == ISO_colon) {

				httpd_fs_open_P(s->scriptptr + 1, &s->file);
			PT_WAIT_THREAD(&s->scriptpt, send_file(s));
			
      } else {
				PT_WAIT_THREAD(&s->scriptpt, httpd_cgi(s->scriptptr)(s, s->scriptptr));
			//uart_puts_P(PSTR("\n\r\x1B[1mEnd of Script\x1B[0m"));
      }
      next_scriptstate(s);
      
      /* The script is over, so we reset the pointers and continue
			sending the rest of the file. */
      s->file.data = s->scriptptr;
      s->file.len = s->scriptlen;
    } else {
      /* See if we find the start of script marker in the block of HTML
	 to be sent. */
			//uart_puts_P(PSTR("\n\r\x1B[1mscript marker\x1B[0m"));
      if(s->file.len > uip_mss()) {
				s->len = uip_mss();
      } else {
				s->len = s->file.len;
      }

			if(pgm_read_byte(s->file.data) == ISO_percent) {
				ptr = (char *) strchr_P(s->file.data + 1, ISO_percent);
      } else {
				ptr = (char *) strchr_P(s->file.data, ISO_percent); 
      }
      if(ptr != NULL && ptr != s->file.data) {
				s->len = (int)(ptr - s->file.data);
				if(s->len >= uip_mss()) {
					s->len = uip_mss();
				}
      }
			//uart_puts_P(PSTR("\n\r\x1B[1msend ???\x1B[0m"));
      PT_WAIT_THREAD(&s->scriptpt, send_part_of_file(s));
      s->file.data += s->len;
      s->file.len -= s->len;
    }
  }
  
  PT_END(&s->scriptpt);
}
/*---------------------------------------------------------------------------*/
static
PT_THREAD(send_headers(struct httpd_state *s, const char *statushdr))
{
  char *ptr;
  PSOCK_BEGIN(&s->sout);
  PSOCK_SEND_STR_P(&s->sout, statushdr);
	
  ptr = strrchr(s->filename, ISO_period);
	
	if(ptr == NULL) {
    PSOCK_SEND_STR_P(&s->sout, http_content_type_binary);
  } else if(strncmp_P(ptr, http_html, 5) == 0 ||
	    strncmp_P(ptr, http_shtml, 6) == 0) {
	  PSOCK_SEND_STR_P(&s->sout, http_content_type_html);
  } else if(strncmp_P(ptr, http_css, 4) == 0) {
    PSOCK_SEND_STR_P(&s->sout, http_content_type_css);
  } else if(strncmp_P(ptr, http_png, 4) == 0) {
    PSOCK_SEND_STR_P(&s->sout, http_content_type_png);
  } else if(strncmp_P(ptr, http_gif, 4) == 0) {
    PSOCK_SEND_STR_P(&s->sout, http_content_type_gif);
  } else if(strncmp_P(ptr, http_jpg, 4) == 0) {
    PSOCK_SEND_STR_P(&s->sout, http_content_type_jpg);
  } else {
    PSOCK_SEND_STR_P(&s->sout, http_content_type_plain);
  }
  PSOCK_END(&s->sout);
}
/*---------------------------------------------------------------------------*/
static
PT_THREAD(handle_output(struct httpd_state *s))
{
	char *ptr;
  PT_BEGIN(&s->outputpt);
	//uart_puts_P(PSTR("\n\r\x1B[1mHandle Output\x1B[0m"));
  if(!httpd_fs_open(s->filename, &s->file))
	{
    httpd_fs_open_P(http_404_html, &s->file);
    strcpy_P(s->filename, http_404_html);
    PT_WAIT_THREAD(&s->outputpt,send_headers(s,http_header_404));
    PT_WAIT_THREAD(&s->outputpt,send_file(s));
  } 
	else 
	{
    PT_WAIT_THREAD(&s->outputpt,send_headers(s,http_header_200));
		
		// s->filename = RAM
    ptr = strchr(s->filename, ISO_period);
    if(ptr != NULL && strncmp_P(ptr, http_shtml, 6) == 0) 
		{
      PT_INIT(&s->scriptpt);
      PT_WAIT_THREAD(&s->outputpt, handle_script(s));
    } 
		else 
		{
      PT_WAIT_THREAD(&s->outputpt,send_file(s));
    }
  }
  PSOCK_CLOSE(&s->sout);
  PT_END(&s->outputpt);
}
/*---------------------------------------------------------------------------*/
static
PT_THREAD(handle_input(struct httpd_state *s))
{
	unsigned int i;
	
	PSOCK_BEGIN(&s->sin);
	
  PSOCK_READTO(&s->sin, ISO_space);

  if(strncmp_P(s->inputbuf, http_get, 4) != 0) 
	{
    PSOCK_CLOSE_EXIT(&s->sin);
  }
  PSOCK_READTO(&s->sin, ISO_space);
	
  if(s->inputbuf[0] != ISO_slash) 
	{
    PSOCK_CLOSE_EXIT(&s->sin);
  }
	
  if(s->inputbuf[1] == ISO_space) 
	{
    strncpy_P(s->filename, http_index_html, sizeof(s->filename));
  } 
	else 
	{
    s->inputbuf[PSOCK_DATALEN(&s->sin) - 1] = 0;
    strncpy(s->filename, &s->inputbuf[0], sizeof(s->filename));
  }
	s->state = STATE_OUTPUT;
	while(1) 
	{
    PSOCK_READTO(&s->sin, ISO_nl);
	  if(strncmp_P(s->inputbuf, http_referer, 8) == 0) 
		{
      s->inputbuf[PSOCK_DATALEN(&s->sin) - 2] = 0;
			//uart_puts(">>http_referer<<\r\n");
			uart_puts(s->inputbuf);
			uart_puts("\r\n");
      /*      httpd_log(&s->inputbuf[9]);*/
			// check URL for '?'
			for (i=0;((s->inputbuf[i]!=0) && (s->inputbuf[i]!='?'));i++);
			if (s->inputbuf[i]=='?')
			{
			uart_puts_P(PSTR("found ?\r\n"));
				// compare path / file
				if(strncmp(s->inputbuf+i-strlen("io.shtml"), "io.shtml", strlen("io.shtml")) == 0)
				{
					uart_puts_P(PSTR("found io.shtml\r\n"));
					// compare query
					if(strncmp(s->inputbuf+i+1, "LED1=1", strlen("LED1=1")) == 0)
					{
						// LED1=1
						PORTC &= ~(1<<PC0); // clear PC0 = LED ON
						
						uart_puts_P(PSTR("LED=1\r\n"));

					}
					if(strncmp(s->inputbuf+i+1, "LED1=0",strlen("LED1=0")) == 0)
					{
						// LED1=0
						PORTC |= (1<<PC0); // set PC0 = LED OFF
						uart_puts_P(PSTR("LED=0\r\n"));
					}
				}
			}
    }
  }
  
  PSOCK_END(&s->sin);
}
/*---------------------------------------------------------------------------*/
static void
handle_connection(struct httpd_state *s)
{
  handle_input(s);
  if(s->state == STATE_OUTPUT) {
    handle_output(s);
  }
}
/*---------------------------------------------------------------------------*/
void
httpd_appcall(void)
{
  struct httpd_state *s = (struct httpd_state *)&(uip_conn->appstate);

	//uart_puts_P(PSTR("\n\r\x1B[1mAppcall\x1B[0m"));
	
  if(uip_closed() || uip_aborted() || uip_timedout()) {
  } else if(uip_connected()) {
    PSOCK_INIT(&s->sin, s->inputbuf, sizeof(s->inputbuf) - 1);
    PSOCK_INIT(&s->sout, s->inputbuf, sizeof(s->inputbuf) - 1);
    PT_INIT(&s->outputpt);
    s->state = STATE_WAITING;
    /*    timer_set(&s->ti3mer, CLOCK_SECOND * 100);*/
    s->timer = 0;
    handle_connection(s);
		} else if(s != NULL) {
			if(uip_poll()) {
				++s->timer;
				if(s->timer >= 20) {
					uip_abort();
				}
			} else {
      s->timer = 0;
    }
    handle_connection(s);
  } else {
    uip_abort();
  }
}
/*---------------------------------------------------------------------------*/
/**
 * \brief      Initialize the web server
 *
 *             This function initializes the web server and should be
 *             called at system boot-up.
 */
void
httpd_init(void)
{
  uip_listen(HTONS(80));
}
/*---------------------------------------------------------------------------*/
/** @} */
