#include "io-commands.h"
#include <stdio.h>
#include <string.h>
#include <stdint.h>
#include <stdlib.h>
#include <avr/io.h>
#include <avr/pgmspace.h>
#include "hw-layout.h"

// Universal commands to control the external I/O lines.

#if ADC_CHANNELS > 0
   #include "driver/ADC.h"
#endif


// Flag wheter an io-command is currently executed.
// Can be used by interrupt driven functions to wait until system is not busy anymore.
uint8_t isBusy;

char ok[]                 PROGMEM="Ok\n";
char bad_command[]        PROGMEM="Bad command\n";
char invalid_port[]       PROGMEM="Invalid port\n";
char invalid_pin[]        PROGMEM="Invalid pin\n";
char invalid_hex[]        PROGMEM="Invalid hex number\n";
char invalid_boolean[]    PROGMEM="Invalid boolean value\n";
char invalid_channel[]    PROGMEM="Invalid channel number\n";
char invalid_reference[]  PROGMEM="Invalid reference name\n";

// Decode a hexadecimal digit to integer
// Invalid characters return -1.

int16_t decodeHex1(char c) {
    if (c>='0' && c<='9')
        return c-'0';
    else if (c>='A' && c<='F')
        return c-'A'+10;
    else if (c>='a' && c<='f')
        return c-'a'+10;
    else
        return -1;
}

// Decode a 2-digit hexadecimal string to integer
// Note that the string length is not checked.
// Invalid characters return -1.

int16_t decodeHex(char* characters) {
    int16_t highNibble=decodeHex1(characters[0]);
    int16_t lowNibble=decodeHex1(characters[1]);
    if ((highNibble<0) || (lowNibble<0))
        return -1;
    else
        return ((highNibble<<4) | (lowNibble));
}


// Decode a single boolean character to integer
// Invalid characters return -1.

int16_t decodeBin(char c) {
    switch (c) {
        case '1': return 1;
        case '0': return 0;
        case 'H': return 1;
        case 'L': return 0;
        case 'E': return 1;
        case 'D': return 0;
        case 'O': return 1;
        case 'I': return 0;
        case 'h': return 1;
        case 'l': return 0;
        case 'e': return 1;
        case 'd': return 0;
        case 'o': return 1;
        case 'i': return 0;
        default: return -1;
    }
}

// Decode a decimal number to integer.
// Invalid characters or empty string return -1.

int decodeDec(char* s) {
    if (s[0]<'0' || s[0]>'9')
        return -1;
    else
        return atoi(s);
}


//-----------------------------------------------------------------------------------------------

// Execute an p command
//     p=enable pull-up's on input pins

void command_p(char* result, char* command) {
    size_t len=strlen(command);
    if (command[1]=='=' || command[1]==',') {
        if (len==18) { // write to 8 ports at once
            int16_t valueH=decodeHex(command+2);
            int16_t valueG=decodeHex(command+4);
            int16_t valueF=decodeHex(command+6);
            int16_t valueE=decodeHex(command+8);
            int16_t valueD=decodeHex(command+10);
            int16_t valueC=decodeHex(command+12);
            int16_t valueB=decodeHex(command+14);
            int16_t valueA=decodeHex(command+16);
            if (valueH<0 || valueG<0 || valueF<0 || valueE<0 ||valueD<0 || valueC<0 || valueB<0 || valueA<0)
                strcpy_P(result,invalid_hex);
            else {
                #ifdef PORTA
                    PORTA=(valueA & ~(DDRA | protect_PORTA)) | (PORTA & (DDRA | protect_PORTA));
                #endif
                #ifdef PORTB
                    PORTB=(valueB & ~(DDRB | protect_PORTB)) | (PORTB & (DDRB | protect_PORTB));
                #endif
                #ifdef PORTC
                    PORTC=(valueC & ~(DDRC | protect_PORTC)) | (PORTC & (DDRC | protect_PORTC));
                #endif
                #ifdef PORTD
                    PORTD=(valueD & ~(DDRD | protect_PORTD)) | (PORTD & (DDRD | protect_PORTD));
                #endif
                #ifdef PORTE
                    PORTE=(valueE & ~(DDRE | protect_PORTE)) | (PORTE & (DDRE | protect_PORTE));
                #endif
                #ifdef PORTF
                    PORTF=(valueF & ~(DDRF | protect_PORTF)) | (PORTF & (DDRF | protect_PORTF));
                #endif
                #ifdef PORTG
                    PORTG=(valueG & ~(DDRG | protect_PORTG)) | (PORTG & (DDRG | protect_PORTG));
                #endif
                #ifdef PORTH
                    PORTH=(valueH & ~(DDRH | protect_PORTH)) | (PORTH & (DDRH | protect_PORTH));
                #endif
            }
            strcpy_P(result,ok);
        }
        else if (len==10) { // write to 4 ports at once
            // all port pins at once
            int16_t valueD=decodeHex(command+2);
            int16_t valueC=decodeHex(command+4);
            int16_t valueB=decodeHex(command+6);
            int16_t valueA=decodeHex(command+8);
            if (valueD<0 || valueC<0 || valueB<0 || valueA<0)
                strcpy_P(result,invalid_hex);
            else {
                #ifdef PORTA
                    PORTA=(valueA & ~(DDRA | protect_PORTA)) | (PORTA & (DDRA | protect_PORTA));
                #endif
                #ifdef PORTB
                    PORTB=(valueB & ~(DDRB | protect_PORTB)) | (PORTB & (DDRB | protect_PORTB));
                #endif
                #ifdef PORTC
                    PORTC=(valueC & ~(DDRC | protect_PORTC)) | (PORTC & (DDRC | protect_PORTC));
                #endif
                #ifdef PORTD
                    PORTD=(valueD & ~(DDRD | protect_PORTD)) | (PORTD & (DDRD | protect_PORTD));
                #endif
            }
            strcpy_P(result,ok);
        }
        else {
            strcpy_P(result,invalid_hex);
        }
    }
    else if (command[3]=='=' || command[3]==',') {
         if (command[1]!='P')
             strcpy_P(result,invalid_port);
         char port=command[2];
         if (len==6) { // write to a single port
            int value=decodeHex(command+4);
            if (value<0)
                strcpy_P(result,invalid_hex);
            else {
                switch (port) {
                    #ifdef PORTA
                        case 'A': PORTA=(value & ~(DDRA | protect_PORTA)) | (PORTA & (DDRA | protect_PORTA)); break;
                    #endif
                    #ifdef PORTB
                        case 'B': PORTB=(value & ~(DDRB | protect_PORTB)) | (PORTB & (DDRB | protect_PORTB)); break;
                    #endif
                    #ifdef PORTC
                        case 'C': PORTC=(value & ~(DDRC | protect_PORTC)) | (PORTC & (DDRC | protect_PORTC)); break;
                    #endif
                    #ifdef PORTD
                        case 'D': PORTD=(value & ~(DDRD | protect_PORTD)) | (PORTD & (DDRD | protect_PORTD)); break;
                    #endif
                    #ifdef PORTE
                        case 'E': PORTE=(value & ~(DDRE | protect_PORTE)) | (PORTE & (DDRE | protect_PORTE)); break;
                    #endif
                    #ifdef PORTF
                        case 'F': PORTF=(value & ~(DDRF | protect_PORTF)) | (PORTF & (DDRF | protect_PORTF)); break;
                    #endif
                    #ifdef PORTG
                        case 'G': PORTG=(value & ~(DDRG | protect_PORTG)) | (PORTG & (DDRG | protect_PORTG)); break;
                    #endif
                    #ifdef PORTH
                        case 'H': PORTH=(value & ~(DDRH | protect_PORTH)) | (PORTH & (DDRH | protect_PORTH)); break;
                    #endif
                    #ifdef PORTJ
                        case 'J': PORTJ=(value & ~(DDRJ | protect_PORTJ)) | (PORTJ & (DDRJ | protect_PORTJ)); break;
                    #endif
                    #ifdef PORTK
                        case 'K': PORTK=(value & ~(DDRK | protect_PORTK)) | (PORTK & (DDRK | protect_PORTK)); break;
                    #endif
                    #ifdef PORTL
                        case 'L': PORTL=(value & ~(DDRL | protect_PORTL)) | (PORTL & (DDRL | protect_PORTL)); break;
                    #endif
                    default: strcpy_P(result,invalid_port); return;
                }
                strcpy_P(result,ok);
            }
        }
        else {
            strcpy_P(result,invalid_hex);
        }
    }
    else if (command[4]=='=' || command[4]==',') { // write to a single port pin
        if (command[1]!='P')
            strcpy_P(result,invalid_port);
        char port=command[2];
        int pin=decodeDec(command+3);
        int value=decodeBin(command[5]);
        if (pin<0 || pin>7)
            strcpy_P(result,invalid_pin);
        else if (value<0 || value>1)
            strcpy_P(result,invalid_boolean);
        else {
            switch (port) {
                #ifdef PORTA
                    case 'A': if ((protect_PORTA & (1<<pin))==0 && readBit(DDRA,pin)==0) writeBit(PORTA,pin,value); break;
                #endif
                #ifdef PORTB
                    case 'B': if ((protect_PORTB & (1<<pin))==0 && readBit(DDRB,pin)==0) writeBit(PORTB,pin,value); break;
                #endif
                #ifdef PORTC
                    case 'C': if ((protect_PORTC & (1<<pin))==0 && readBit(DDRC,pin)==0) writeBit(PORTC,pin,value); break;
                #endif
                #ifdef PORTD
                    case 'D': if ((protect_PORTD & (1<<pin))==0 && readBit(DDRD,pin)==0) writeBit(PORTD,pin,value); break;
                #endif
                #ifdef PORTE
                    case 'E': if ((protect_PORTE & (1<<pin))==0 && readBit(DDRE,pin)==0) writeBit(PORTE,pin,value); break;
                #endif
                #ifdef PORTF
                    case 'F': if ((protect_PORTF & (1<<pin))==0 && readBit(DDRF,pin)==0) writeBit(PORTF,pin,value); break;
                #endif
                #ifdef PORTG
                    case 'G': if ((protect_PORTG & (1<<pin))==0 && readBit(DDRG,pin)==0) writeBit(PORTG,pin,value); break;
                #endif
                #ifdef PORTH
                    case 'H': if ((protect_PORTH & (1<<pin))==0 && readBit(DDRH,pin)==0) writeBit(PORTH,pin,value); break;
                #endif
                #ifdef PORTJ
                    case 'J': if ((protect_PORTJ & (1<<pin))==0 && readBit(DDRJ,pin)==0) writeBit(PORTJ,pin,value); break;
                #endif
                #ifdef PORTK
                    case 'K': if ((protect_PORTK & (1<<pin))==0 && readBit(DDRK,pin)==0) writeBit(PORTK,pin,value); break;
                #endif
                #ifdef PORTL
                    case 'L': if ((protect_PORTL & (1<<pin))==0 && readBit(DDRL,pin)==0) writeBit(PORTL,pin,value); break;
                #endif
                default: strcpy_P(result,invalid_port); return;
            }
            strcpy_P(result,ok);
        }
    }
    else
        strcpy_P(result,bad_command);
}



//-----------------------------------------------------------------------------------------------

// Execute an d command
//     d=set i/o direction
//     value 1=output
//     value 0=input

void command_d(char* result, char* command) {
    size_t len=strlen(command);
    if (command[1]=='=' || command[1]==',') {
        if (len==18) {  // write to 8 ports at once
            int valueH=decodeHex(command+2);
            int valueG=decodeHex(command+4);
            int valueF=decodeHex(command+6);
            int valueE=decodeHex(command+8);
            int valueD=decodeHex(command+10);
            int valueC=decodeHex(command+12);
            int valueB=decodeHex(command+14);
            int valueA=decodeHex(command+16);
            if (valueH<0 || valueG<0 || valueF<0 || valueE<0 ||valueD<0 || valueC<0 || valueB<0 || valueA<0)
                strcpy_P(result,invalid_hex);
            else {
                #ifdef PORTA
                DDRA=(valueA & ~protect_PORTA) | (DDRA & protect_PORTA);
                #endif
                #ifdef PORTB
                DDRB=(valueB & ~protect_PORTB) | (DDRB & protect_PORTB);
                #endif
                #ifdef PORTC
                DDRC=(valueC & ~protect_PORTC) | (DDRC & protect_PORTC);
                #endif
                #ifdef PORTD
                DDRD=(valueD & ~protect_PORTD) | (DDRD & protect_PORTD);
                #endif
                #ifdef PORTE
                DDRE=(valueE & ~protect_PORTE) | (DDRE & protect_PORTE);
                #endif
                #ifdef PORTF
                DDRF=(valueF & ~protect_PORTF) | (DDRF & protect_PORTF);
                #endif
                #ifdef PORTG
                DDRG=(valueG & ~protect_PORTG) | (DDRG & protect_PORTG);
                #endif
                #ifdef PORTH
                DDRH=(valueH & ~protect_PORTH) | (DDRH & protect_PORTH);
                #endif
            }
            strcpy_P(result,ok);
        }
        else if (len==10) { // write to 4 ports at once
            // all port pins at once
            int valueD=decodeHex(command+2);
            int valueC=decodeHex(command+4);
            int valueB=decodeHex(command+6);
            int valueA=decodeHex(command+8);
            if (valueD<0 || valueC<0 || valueB<0 || valueA<0)
                strcpy_P(result,invalid_hex);
            else {
                #ifdef PORTA
                DDRA=(valueA & ~protect_PORTA) | (DDRA & protect_PORTA);
                #endif
                #ifdef PORTB
                DDRB=(valueB & ~protect_PORTB) | (DDRB & protect_PORTB);
                #endif
                #ifdef PORTC
                DDRC=(valueC & ~protect_PORTC) | (DDRC & protect_PORTC);
                #endif
                #ifdef PORTD
                DDRD=(valueD & ~protect_PORTD) | (DDRD & protect_PORTD);
                #endif
            }
            strcpy_P(result,ok);
        }
        else {
            strcpy_P(result,invalid_hex);
        }
    }
    else if (command[3]=='=' || command[3]==',') { // write to a single port
        if (command[1]!='P')
            strcpy_P(result,invalid_port);
        char port=command[2];
        if (len==6) {
            int value=decodeHex(command+4);
            if (value<0)
                strcpy_P(result,invalid_hex);
            else {

                switch (port) {
                    #ifdef PORTA
                        case 'A': DDRA=(value & ~protect_PORTA) | (DDRA & protect_PORTA); break;
                    #endif
                    #ifdef PORTB
                        case 'B': DDRB=(value & ~protect_PORTB) | (DDRB & protect_PORTB); break;
                    #endif
                    #ifdef PORTC
                        case 'C': DDRC=(value & ~protect_PORTC) | (DDRC & protect_PORTC); break;
                    #endif
                    #ifdef PORTD
                        case 'D': DDRD=(value & ~protect_PORTD) | (DDRD & protect_PORTD); break;
                    #endif
                    #ifdef PORTE
                        case 'E': DDRE=(value & ~protect_PORTE) | (DDRE & protect_PORTE); break;
                    #endif
                    #ifdef PORTF
                        case 'F': DDRF=(value & ~protect_PORTF) | (DDRF & protect_PORTF); break;
                    #endif
                    #ifdef PORTG
                        case 'G': DDRG=(value & ~protect_PORTG) | (DDRG & protect_PORTG); break;
                    #endif
                    #ifdef PORTH
                        case 'H': DDRH=(value & ~protect_PORTH) | (DDRH & protect_PORTH); break;
                    #endif
                    #ifdef PORTJ
                        case 'J': DDRJ=(value & ~protect_PORTJ) | (DDRJ & protect_PORTJ); break;
                    #endif
                    #ifdef PORTK
                        case 'K': DDRK=(value & ~protect_PORTK) | (DDRK & protect_PORTK); break;
                    #endif
                    #ifdef PORTL
                        case 'L': DDRL=(value & ~protect_PORTL) | (DDRL & protect_PORTL); break;
                    #endif
                    default: strcpy_P(result,invalid_port); return;
                }
                strcpy_P(result,ok);
            }
        }
        else {
            strcpy_P(result,invalid_hex);
        }
    }
    else if (command[4]=='=' || command[4]==',') { // write to a single port pin
        if (command[1]!='P')
            strcpy_P(result,invalid_port);
        char port=command[2];
        int pin=decodeDec(command+3);
        int value=decodeBin(command[5]);
        if (pin<0 || pin>7)
            strcpy_P(result,invalid_pin);
        else if (value<0 || value>1)
            strcpy_P(result,invalid_boolean);
        else {
            switch (port) {
                #ifdef PORTA
                    case 'A': if ((protect_PORTA & (1<<pin))==0) writeBit(DDRA,pin,value); break;
                #endif
                #ifdef PORTB
                    case 'B': if ((protect_PORTB & (1<<pin))==0) writeBit(DDRB,pin,value); break;
                #endif
                #ifdef PORTC
                    case 'C': if ((protect_PORTC & (1<<pin))==0) writeBit(DDRC,pin,value); break;
                #endif
                #ifdef PORTD
                    case 'D': if ((protect_PORTD & (1<<pin))==0) writeBit(DDRD,pin,value); break;
                #endif
                #ifdef PORTE
                    case 'E': if ((protect_PORTE & (1<<pin))==0) writeBit(DDRE,pin,value); break;
                #endif
                #ifdef PORTF
                    case 'F': if ((protect_PORTF & (1<<pin))==0) writeBit(DDRF,pin,value); break;
                #endif
                #ifdef PORTG
                    case 'G': if ((protect_PORTG & (1<<pin))==0) writeBit(DDRG,pin,value); break;
                #endif
                #ifdef PORTH
                    case 'H': if ((protect_PORTH & (1<<pin))==0) writeBit(DDRH,pin,value); break;
                #endif
                #ifdef PORTJ
                    case 'J': if ((protect_PORTJ & (1<<pin))==0) writeBit(DDRJ,pin,value); break;
                #endif
                #ifdef PORTK
                    case 'K': if ((protect_PORTK & (1<<pin))==0) writeBit(DDRK,pin,value); break;
                #endif
                #ifdef PORTL
                    case 'L': if ((protect_PORTL & (1<<pin))==0) writeBit(DDRL,pin,value); break;
                #endif
                default: strcpy_P(result,invalid_port); return;
            }
            strcpy_P(result,ok);
        }
    }
    else
        strcpy_P(result,bad_command);
}

//-----------------------------------------------------------------------------------------------

// Execute an o command
//     o=output data

void command_o(char* result, char* command) {
    size_t len=strlen(command);
    if (command[1]=='=' || command[1]==',') {
        if (len==18) { // write to 8 ports at once
            int valueH=decodeHex(command+2);
            int valueG=decodeHex(command+4);
            int valueF=decodeHex(command+6);
            int valueE=decodeHex(command+8);
            int valueD=decodeHex(command+10);
            int valueC=decodeHex(command+12);
            int valueB=decodeHex(command+14);
            int valueA=decodeHex(command+16);
            if (valueH<0 || valueG<0 || valueF<0 || valueE<0 ||valueD<0 || valueC<0 || valueB<0 || valueA<0)
                strcpy_P(result,invalid_hex);
            else {
                #ifdef PORTA
                PORTA=(valueA & (DDRA & ~protect_PORTA)) | (PORTA & (~DDRA | protect_PORTA));
                #endif
                #ifdef PORTB
                PORTB=(valueB & (DDRB & ~protect_PORTB)) | (PORTB & (~DDRB | protect_PORTB));
                #endif
                #ifdef PORTC
                PORTC=(valueC & (DDRC & ~protect_PORTC)) | (PORTC & (~DDRC | protect_PORTC));
                #endif
                #ifdef PORTD
                PORTD=(valueD & (DDRD & ~protect_PORTD)) | (PORTD & (~DDRD | protect_PORTD));
                #endif
                #ifdef PORTE
                PORTE=(valueE & (DDRE & ~protect_PORTE)) | (PORTE & (~DDRE | protect_PORTE));
                #endif
                #ifdef PORTF
                PORTF=(valueF & (DDRF & ~protect_PORTF)) | (PORTF & (~DDRF | protect_PORTF));
                #endif
                #ifdef PORTG
                PORTG=(valueG & (DDRG & ~protect_PORTG)) | (PORTG & (~DDRG | protect_PORTG));
                #endif
                #ifdef PORTH
                PORTH=(valueH & (DDRH & ~protect_PORTH)) | (PORTH & (~DDRH | protect_PORTH));
                #endif
            }
            strcpy_P(result,ok);
        }
        else if (len==10) { // write to 4 ports at once
            // all port pins at once
            int valueD=decodeHex(command+2);
            int valueC=decodeHex(command+4);
            int valueB=decodeHex(command+6);
            int valueA=decodeHex(command+8);
            if (valueD<0 || valueC<0 || valueB<0 || valueA<0)
                strcpy_P(result,invalid_hex);
            else {
                #ifdef PORTA
                PORTA=(valueA & (DDRA & ~protect_PORTA)) | (PORTA & (~DDRA | protect_PORTA));
                #endif
                #ifdef PORTB
                PORTB=(valueB & (DDRB & ~protect_PORTB)) | (PORTB & (~DDRB | protect_PORTB));
                #endif
                #ifdef PORTC
                PORTC=(valueC & (DDRC & ~protect_PORTC)) | (PORTC & (~DDRC | protect_PORTC));
                #endif
                #ifdef PORTD
                PORTD=(valueD & (DDRD & ~protect_PORTD)) | (PORTD & (~DDRD | protect_PORTD));
                #endif
            }
            strcpy_P(result,ok);
        }
        else {
            strcpy_P(result,invalid_hex);
        }
    }
    else if (command[3]=='=' || command[3]==',') { // write to a single port
        if (command[1]!='P')
            strcpy_P(result,invalid_port);
        char port=command[2];
        if (len==6) {
            int value=decodeHex(command+4);
            if (value<0)
                strcpy_P(result,invalid_hex);
            else {

                switch (port) {
                    #ifdef PORTA
                        case 'A': PORTA=(value & (DDRA & ~protect_PORTA)) | (PORTA & (~DDRA | protect_PORTA)); break;
                    #endif
                    #ifdef PORTB
                        case 'B': PORTB=(value & (DDRB & ~protect_PORTB)) | (PORTB & (~DDRB | protect_PORTB)); break;
                    #endif
                    #ifdef PORTC
                        case 'C': PORTC=(value & (DDRC & ~protect_PORTC)) | (PORTC & (~DDRC | protect_PORTC)); break;
                    #endif
                    #ifdef PORTD
                        case 'D': PORTD=(value & (DDRD & ~protect_PORTD)) | (PORTD & (~DDRD | protect_PORTD)); break;
                    #endif
                    #ifdef PORTE
                        case 'E': PORTE=(value & (DDRE & ~protect_PORTE)) | (PORTE & (~DDRE | protect_PORTE)); break;
                    #endif
                    #ifdef PORTF
                        case 'F': PORTF=(value & (DDRF & ~protect_PORTF)) | (PORTF & (~DDRF | protect_PORTF)); break;
                    #endif
                    #ifdef PORTG
                        case 'G': PORTG=(value & (DDRG & ~protect_PORTG)) | (PORTG & (~DDRG | protect_PORTG)); break;
                    #endif
                    #ifdef PORTH
                        case 'H': PORTH=(value & (DDRH & ~protect_PORTH)) | (PORTH & (~DDRH | protect_PORTH)); break;
                    #endif
                    #ifdef PORTJ
                        case 'J': PORTJ=(value & (DDRJ & ~protect_PORTJ)) | (PORTJ & (~DDRJ | protect_PORTJ)); break;
                    #endif
                    #ifdef PORTK
                        case 'K': PORTK=(value & (DDRK & ~protect_PORTK)) | (PORTK & (~DDRK | protect_PORTK)); break;
                    #endif
                    #ifdef PORTL
                        case 'L': PORTL=(value & (DDRL & ~protect_PORTL)) | (PORTL & (~DDRL | protect_PORTL)); break;
                    #endif
                    default: strcpy_P(result,invalid_port); return;
                }
                strcpy_P(result,ok);
            }
        }
        else {
            strcpy_P(result,invalid_hex);
        }
    }
    else if (command[4]=='=' || command[4]==',') { // write to a single port pin
        if (command[1]!='P')
            strcpy_P(result,invalid_port);
        char port=command[2];
        int pin=decodeDec(command+3);
        int value=decodeBin(command[5]);
        if (pin<0 || pin>7)
            strcpy_P(result,invalid_pin);
        else if (value<0 || value>1 || strlen(command)!=6)
            strcpy_P(result,invalid_boolean);
        else {
            switch (port) {
                #ifdef PORTA
                    case 'A': if ((readBit(DDRA,pin)==1) && ((protect_PORTA & (1<<pin))==0)) writeBit(PORTA,pin,value); break;
                #endif
                #ifdef PORTB
                    case 'B': if ((readBit(DDRB,pin)==1) && ((protect_PORTB & (1<<pin))==0)) writeBit(PORTB,pin,value); break;
                #endif
                #ifdef PORTC
                    case 'C': if ((readBit(DDRC,pin)==1) && ((protect_PORTC & (1<<pin))==0)) writeBit(PORTC,pin,value); break;
                #endif
                #ifdef PORTD
                    case 'D': if ((readBit(DDRD,pin)==1) && ((protect_PORTD & (1<<pin))==0)) writeBit(PORTD,pin,value); break;
                #endif
                #ifdef PORTE
                    case 'E': if ((readBit(DDRE,pin)==1) && ((protect_PORTE & (1<<pin))==0)) writeBit(PORTE,pin,value); break;
                #endif
                #ifdef PORTF
                    case 'F': if ((readBit(DDRF,pin)==1) && ((protect_PORTF & (1<<pin))==0)) writeBit(PORTF,pin,value); break;
                #endif
                #ifdef PORTG
                    case 'G': if ((readBit(DDRG,pin)==1) && ((protect_PORTG & (1<<pin))==0)) writeBit(PORTG,pin,value); break;
                #endif
                #ifdef PORTH
                    case 'H': if ((readBit(DDRH,pin)==1) && ((protect_PORTH & (1<<pin))==0)) writeBit(PORTH,pin,value); break;
                #endif
                #ifdef PORTJ
                    case 'J': if ((readBit(DDRJ,pin)==1) && ((protect_PORTJ & (1<<pin))==0)) writeBit(PORTJ,pin,value); break;
                #endif
                #ifdef PORTK
                    case 'K': if ((readBit(DDRK,pin)==1) && ((protect_PORTK & (1<<pin))==0)) writeBit(PORTK,pin,value); break;
                #endif
                #ifdef PORTL
                    case 'L': if ((readBit(DDRL,pin)==1) && ((protect_PORTL & (1<<pin))==0)) writeBit(PORTL,pin,value); break;
                #endif
                default: strcpy_P(result,invalid_port); return;
            }
            strcpy_P(result,ok);
        }
    }
    else
        strcpy_P(result,bad_command);
}


//---------------------------------------------------------------------------------------------


// Execute an i command
//     i=read input

void command_i(char* result, char* command) {
    size_t len=strlen(command);
    if (len==1) { // read all ports
        #ifdef PINA
            uint8_t pina=PINA;
        #else
            uint8_t pina=0;
        #endif
        #ifdef PINB
            uint8_t pinb=PINB;
        #else
            uint8_t pinb=0;
        #endif
        #ifdef PINC
            uint8_t pinc=PINC;
        #else
            uint8_t pinc=0;
        #endif
        #ifdef PIND
            uint8_t pind=PIND;
        #else
           uint8_t pind=0;
        #endif
        // If the AVR has more than 4 ports, the read also ports E-H
        #ifdef PINE
            #ifdef PINE
                uint8_t pine=PINE;
            #else
                uint8_t pine=0;
            #endif
            #ifdef PINF
                uint8_t pinf=PINF;
            #else
                uint8_t pinf=0;
            #endif
            #ifdef PING
                uint8_t ping=PING;
            #else
                uint8_t ping=0;
            #endif
            #ifdef PINH
                uint8_t pinh=PINH;
            #else
                uint8_t pinh=0;
            #endif
        #endif
        // If the controler has more than 4 port, then output a 64bit number, else output a 32bit number
        #ifdef PINE
            sprintf_P(result,PSTR("i=%02X%02X%02X%02X%02X%02X%02X%02X\n"),pinh,ping,pinf,pine,pind,pinc,pinb,pina);
        #else
           sprintf_P(result,PSTR("i=%02X%02X%02X%02X\n"),pind,pinc,pinb,pina);
        #endif
    }
    else if (len==3) {  // read a single port
        if (command[1]!='P')
            strcpy_P(result,invalid_port);
        char port=command[2];
        uint8_t value;
        switch (port) {
            #ifdef PINA
                case 'A': value=PINA; break;
            #endif
            #ifdef PINB
                case 'B': value=PINB; break;
            #endif
            #ifdef PINC
                case 'C': value=PINC; break;
            #endif
            #ifdef PIND
                case 'D': value=PIND; break;
            #endif
            #ifdef PINE
                case 'E': value=PINE; break;
            #endif
            #ifdef PINF
                case 'F': value=PINF; break;
            #endif
            #ifdef PING
                case 'G': value=PING; break;
            #endif
            #ifdef PINH
                case 'H': value=PINH; break;
            #endif
            #ifdef PINJ
                case 'J': value=PINJ; break;
            #endif
            #ifdef PINK
                case 'K': value=PINK; break;
            #endif
            #ifdef PINL
                case 'L': value=PINL; break;
            #endif
            default: strcpy_P(result,invalid_port); return;
        }
        sprintf_P(result,PSTR("P%c=%02X\n"),port,value);
    }
    else if (len==4) {  // read a single port pin
        // a single port pin
        if (command[1]!='P')
            strcpy_P(result,invalid_port);
        char port=command[2];
        int pin=decodeDec(command+3);
        if (pin<0 || pin>7)
            strcpy_P(result,invalid_pin);
        else {
            uint8_t value;
            switch (port) {
              #ifdef PINA
                  case 'A': value=readBit(PINA,pin); break;
              #endif
              #ifdef PINB
                  case 'B': value=readBit(PINB,pin); break;
              #endif
              #ifdef PINC
                  case 'C': value=readBit(PINC,pin); break;
              #endif
              #ifdef PIND
                  case 'D': value=readBit(PIND,pin); break;
              #endif
              #ifdef PINE
                  case 'E': value=readBit(PINE,pin); break;
              #endif
              #ifdef PINF
                  case 'F': value=readBit(PINF,pin); break;
              #endif
              #ifdef PING
                  case 'G': value=readBit(PING,pin); break;
              #endif
              #ifdef PINH
                  case 'H': value=readBit(PINH,pin); break;
              #endif
              #ifdef PINJ
                  case 'J': value=readBit(PINJ,pin); break;
              #endif
              #ifdef PINK
                  case 'K': value=readBit(PINK,pin); break;
              #endif
              #ifdef PINL
                  case 'L': value=readBit(PINL,pin); break;
              #endif
              default: strcpy_P(result,invalid_port); return;
            }
            sprintf_P(result,PSTR("P%c%i=%i\n"),port,pin,value);
        }
    }
    else
        strcpy_P(result,invalid_port);
}

//--------------------------------------------------------------------------------------------

#if ADC_CHANNELS > 0

// Execute an a command
//     a=read analog input

void command_a(char* result, char* command) {
      int channel=decodeDec(command+1);
      if (channel<0 || channel>=ADC_CHANNELS)
          strcpy_P(result,invalid_channel);
      else {
          sprintf_P(result,PSTR("ADC%i=%04X\n"),channel,getADC(channel));
      }
}

// Execute an r command.
//     r=set ADC reference input
void command_r(char* result, char* command) {
    if (setAREF(command+1)==1)
        strcpy_P(result,ok);
    else
       strcpy_P(result,invalid_reference);
}

#endif //ADC_CHANNELS


//-------------------------------------------------------------------------------------


// Execute an I/O command
// Return the result string in result buffer

void io_command(char* result, char* command) {
    // wait until no other thread executes this function
    while (isBusy);
    isBusy=1;

    // call command function
    switch (command[0]) {
        // if the command is empty, return nothing
        case 0  : result[0]=0; break;
        // call the command function
        case 'd': command_d(result,command); break;
        case 'p': command_p(result,command); break;
        case 'o': command_o(result,command); break;
        case 'i': command_i(result,command); break;
        #if ADC_CHANNELS > 0
            case 'r': command_r(result,command); break;
            case 'a': command_a(result,command); break;
        #endif
        // in case of unknown command, return error message
        default: strcpy_P(result,bad_command);
    }

    isBusy=0;
}
