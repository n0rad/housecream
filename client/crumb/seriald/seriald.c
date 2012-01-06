/*
Serial port daemon to call user_functions and io-commands.
The serial port daemon is interrupt driven.
 */

#include "seriald.h"
#include "../io-commands.h"
#include <string.h>
#include <util/setbaud.h>
#include <avr/interrupt.h> 
#include <avr/pgmspace.h>


// Buffer for commands and results
// Larger buffer allow larger result strings in io-commands.c
// but more than the size of UIP_CONF_BUFFER_SIZE will not work
// over ethernet.
static char buffer[101];

// length of the current command
static unsigned short cmd_len;


// Write a character to the serial port
static void serial_write(char c) {
    #if TERMINAL_MODE
        if (c=='\n') {
            loop_until_bit_is_set(UCSR0A, UDRE0);
            UDR0='\r';
        }
    #endif //TERMINAL_MODE
    loop_until_bit_is_set(UCSR0A, UDRE0);
    UDR0 = c;
}


// Write a string from program memory to the serial port
static void serial_writestr_P(const char* s) {
    char c=pgm_read_byte(s);
    while (c!=0) {
        serial_write(c);
	c=pgm_read_byte(++s);
    }    
}

// Write a string from ram to the serial port
void serial_writestr(char* s) {
    while (*s != 0) {
        serial_write(*s);
	s++;
    }    
}

void serial_writestrln(char* s) {
	serial_writestr(s);
	serial_writestr("\n");
}


void serial_writeDec(int i) {
	char str[12];
	snprintf(str, sizeof(str), "%d", i);
	serial_writestr(str);
}

// Read a character from serial port
static int serial_read(void) {
    char c=UDR0;
    #if SERIAL_ECHO
        loop_until_bit_is_set(UCSR0A, UDRE0);
        UDR0 = c;   
    #endif // SERIAL_ECHO
    #if TERMINAL_MODE
        if (c=='\r') {
            c='\n';
            #ifdef SERIAL_ECHO
                loop_until_bit_is_set(UCSR0A, UDRE0);
                UDR0 = c; 
            #endif // SERIAL_ECHO
        }
    #endif //TERMINAL_MODE
    return c;
}


void seriald_init(void) {
    // clear the command buffer, wait for first command
    cmd_len=0;
    // set baud rate
    UBRR0H = UBRRH_VALUE;
    UBRR0L = UBRRL_VALUE;
    #if USE_2X
        UCSR0A |= (1 << U2X0);
    #else
        UCSR0A &= ~(1 << U2X0);
    #endif
    // enable receiver and transmitter and Rx interrupts
    UCSR0B = (1<<RXEN0) | (1<<TXEN0)  | (1 << RXCIE0);
    // framing format 8N1
    UCSR0C = (1<<UCSZ01) | (1<<UCSZ00);
    // enable interrupts
    sei();
}

// USART0 RX interrupt
ISR (USART0_RX_vect ) { 
    char c=serial_read();
    // check for buffer overflow
    if (cmd_len==sizeof(buffer)) {
        serial_writestr_P(PSTR("\nCommand line too long\n"));
	cmd_len=0;
    }
    else {
        // collect characters until end of line
	if (c=='\b' && cmd_len>1) { 
	    // backspace deletes the last received character
	    cmd_len-=1;
        }
        else if (c=='\n' || c=='\r') {
	    buffer[cmd_len]=0;
	    // execute the command, using the buffer for both input and output (reduces RAM usage)
	    io_command(buffer,buffer);
	    // print the result
	    serial_writestr(buffer);
	    // wait for the next command
	    cmd_len=0;
	}
	else {
	    buffer[cmd_len++]=c;
	}
    }
}
