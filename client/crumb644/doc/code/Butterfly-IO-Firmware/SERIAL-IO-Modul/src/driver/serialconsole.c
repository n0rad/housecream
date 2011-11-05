#include "serialconsole.h"
#include <util/setbaud.h>
#include <stdio.h>
#include <stdlib.h>
#include <avr/io.h>

// Files for input and output through serial port.
static int serial_write(char, FILE *);
static int serial_read(FILE *);
static FILE serialPort = FDEV_SETUP_STREAM(serial_write, serial_read, _FDEV_SETUP_RW);

#ifdef TXEN0   // Controller with multiple serial ports

    #ifdef USE_SERIAL2  // configured to use the second serial port

        // Write a character to the serial port
        static int serial_write(char c, FILE *f) {
            #if TERMINAL_MODE
                if (c=='\n') {
                    loop_until_bit_is_set(UCSR1A, UDRE1);
                    UDR1='\r';
                }
            #endif //TERMINAL_MODE
            loop_until_bit_is_set(UCSR1A, UDRE1);
            UDR1 = c;
            return 0;
        }

        // Read a character from serial port
        static int serial_read(FILE *f) {
            loop_until_bit_is_set(UCSR1A, RXC1);
            char c=UDR1;
            #if SERIAL_ECHO
                loop_until_bit_is_set(UCSR1A, UDRE1);
                UDR1 = c;
            #endif // SERIAL_ECHO
            #if TERMINAL_MODE
                if (c=='\r') {
                    c='\n';
                    #ifdef SERIAL_ECHO
                        loop_until_bit_is_set(UCSR1A, UDRE1);
                        UDR1 = c;
                    #endif // SERIAL_ECHO
                }
            #endif //TERMINAL_MODE
            return c;
        }


        // Initialize the serial port
        void initserial(void) {
            // set baudrate
            UBRR1H = UBRRH_VALUE;
            UBRR1L = UBRRL_VALUE;
            #if USE_2X
                UCSR1A |= (1 << U2X0);
            #else
                UCSR1A &= ~(1 << U2X0);
            #endif
            // enable receiver and transmitter
            UCSR1B = (1<<RXEN1) | (1<<TXEN1);
            // framing format 8N1
            UCSR1C = (1<<UCSZ11) | (1<<UCSZ10);
            // Bind stdout and stdin to the serial port
            stdout = &serialPort;
            stdin = &serialPort;
        }

    #else // Configured to use the first serial port

            // Write a character to the serial port
        static int serial_write(char c, FILE *f) {
            #if TERMINAL_MODE
                if (c=='\n') {
                    loop_until_bit_is_set(UCSR0A, UDRE0);
                    UDR0='\r';
                }
            #endif //TERMINAL_MODE
            loop_until_bit_is_set(UCSR0A, UDRE0);
            UDR0 = c;
            return 0;
        }

        // Read a character from serial port
        static int serial_read(FILE *f) {
            loop_until_bit_is_set(UCSR0A, RXC0);
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


        // Initialize the serial port
        void initserial(void) {
            // set baudrate
            UBRR0H = UBRRH_VALUE;
            UBRR0L = UBRRL_VALUE;
            #if USE_2X
                UCSR0A |= (1 << U2X0);
            #else
                UCSR0A &= ~(1 << U2X0);
            #endif
            // enable receiver and transmitter
            UCSR0B = (1<<RXEN0) | (1<<TXEN0);
            // framing format 8N1
            UCSR0C = (1<<UCSZ01) | (1<<UCSZ00);
            // Bind stdout and stdin to the serial port
            stdout = &serialPort;
            stdin = &serialPort;
        }
        
    #endif

#else // Controller has only a single serial port

    // Write a character to the serial port
    static int serial_write(char c, FILE *f) {
        #if TERMINAL_MODE
            if (c=='\n') {
                loop_until_bit_is_set(UCSRA, UDRE);
                UDR='\r';
            }
        #endif //TERMINAL_MODE
        loop_until_bit_is_set(UCSRA, UDRE);
        UDR = c;
        return 0;
    }

    // Read a character from serial port
    static int serial_read(FILE *f) {
        loop_until_bit_is_set(UCSRA, RXC);
        char c=UDR;
        #if SERIAL_ECHO
            loop_until_bit_is_set(UCSRA, UDRE);
            UDR = c;
        #endif // SERIAL_ECHO
        #if TERMINAL_MODE
            if (c=='\r') {
                c='\n';
                #ifdef SERIAL_ECHO
                    loop_until_bit_is_set(UCSRA, UDRE);
                    UDR = c;
                #endif // SERIAL_ECHO
            }
        #endif //TERMINAL_MODE
        return c;
    }


    // Initialize the serial port
    void initserial(void) {
        // set baudrate
        UBRRH = UBRRH_VALUE;
        UBRRL = UBRRL_VALUE;
        #if USE_2X
            UCSRA |= (1 << U2X);
        #else
            UCSRA &= ~(1 << U2X);
        #endif
        // enable receiver and transmitter
        UCSRB = (1<<RXEN) | (1<<TXEN);
        // framing format 8N1
        #ifdef URSEL
            UCSRC = (1<<URSEL) | (1<<UCSZ1) | (1<<UCSZ0);
        #else 
            UCSRC = (1<<UCSZ1) | (1<<UCSZ0);
        #endif
        // Bind stdout stdin to the serial port
        stdout = &serialPort;
        stdin = &serialPort;
    }

#endif // TXENO
