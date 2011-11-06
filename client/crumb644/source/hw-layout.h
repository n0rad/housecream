#ifndef _HW_LAYOUT_H__
#define _HW_LAYOUT_H__


// This file is used to adapt the firmware to different hardware layouts

// ADBUS     parallel A/D bus
// ALE       address latch enable of parallel A/D bus
// nWr       write data on parallel A/D bus
// nRd       read data on parallel A/D bus

// nCSADC    chip select of ADC chip
// nCSETH    chip select of ethernet controller

// DIN       data input of serial ADC chip
// DOUT      data output of serial ADC chip
// CLK       clock line of serial ADC chip


// Macro used to write to a single I/O pin
#define writeBit(port,bit,value) { if ((value)>0) (port) |= (1<<bit); else (port) &= ~(1<<bit); } 

// Macro used to read from a single I/O pin
#define readBit(port,bit) (((port) >> (bit)) & 1)



// Crumb644-NET version 1.0 from www.chip45.com
#ifdef Crumb644_NET_v1
    // Macros for access to Adress/data bus
    #define ADBUS_output()       DDRA = 255
    #define ADBUS_input()        DDRA = 0 
    #define ADBUS_write(value)   PORTA = (value)
    #define ADBUS_read()         PINA
    
    // Macros for the control lines of the adress/data bus
    #define ALE(value)           writeBit(PORTC,3,value)
    #define nWR(value)           writeBit(PORTC,4,value)
    #define nRD(value)           writeBit(PORTC,5,value)
    
    // Macros for chip-select signals
    #define nCSADC(value)        writeBit(PORTC,2,value)
    #define nCSETH(value)        writeBit(PORTC,6,value)
       
    // The status LED is accessed in open-drain mode
    #define STATUS_LED_on()      { writeBit(DDRC,0,1); writeBit(PORTC,0,0); }
    #define STATUS_LED_off()	 writeBit(DDRC,0,0)   
    
    // Initialisation of all I/O pins.
    // PC2 - PC6 are output and high.
    #define init_io_pins()       { \
                                   DDRA=0; \
                                   DDRC|=(1<<2)+(1<<3)+(1<<4)+(1<<5)+(1<<6); \
				   PORTC|=(1<<2)+(1<<3)+(1<<4)+(1<<5)+(1<<6); \
				 }   
				 
    // Config reset jumper on ISP connector.
    // PB5 is used as input with pull-ups and PB7 is used as output
    #define JUMPER_PORT          PORTB
    #define JUMPER_DDR           DDRB
    #define JUMPER_write(value)  writeBit(PORTB,7,value)
    #define JUMPER_read()        readBit(PINB,5)
    #define init_jumper_pins()   { \
                                   writeBit(DDRB,7,1); \
				   writeBit(DDRB,5,0); \
				   writeBit(PORTB,5,1); \
				 }

    // ADC hardware settings.
    // The EXT_AREF setting enables displaying voltage values.
    #define ADC_CHANNELS         4
    #define EXT_AREF             5.00
    
    // Serial ADC communication to MCP3204 chip.
    // PB5 is the data line from AVR to ADC
    // PB6 is the data line from ADC to AVR
    // PB7 is the clock line
    #define ADC_PORT             PORTB
    #define ADC_DDR              DDRB
    #define ADC_CLK(value)       writeBit(PORTB,7,value)
    #define ADC_write(value)     writeBit(PORTB,5,value)
    #define ADC_read()           readBit(PINB,6)
    #define init_adc_pins()      { \
                                   writeBit(DDRB,5,1); \
				   writeBit(DDRB,6,0); \
				   writeBit(DDRB,7,1); \
				 }
                                 
    // Definition of i/o port bits that are write-protected.
    // These pins cannot be accessed via io-commands d, p and o.
    // 0x00 means that the whole port is not protected.
    #define protect_PORTA        0xFF
    #define protect_PORTB        0x00
    #define protect_PORTC        0xFC
    #define protect_PORTD        0x00
#endif


// Crumb644-NET version 2.0 or 2.1 from www.chip45.com
#ifdef Crumb644_NET_v2
    // Macros for access to Adress/data bus
    #define ADBUS_output()       DDRA = 255
    #define ADBUS_input()        DDRA = 0 
    #define ADBUS_write(value)   PORTA = (value)
    #define ADBUS_read()         PINA
    
    // Macros for the control lines of the adress/data bus
    #define ALE(value)           writeBit(PORTC,3,value)
    #define nWR(value)           writeBit(PORTC,4,value)
    #define nRD(value)           writeBit(PORTC,5,value)
    
    // Macros for chip-select signals
    #define nCSETH(value)        writeBit(PORTC,6,value)
       
    // The status LED is accessed in open-drain mode
    #define STATUS_LED_on()      { writeBit(DDRC,0,1); writeBit(PORTC,0,0); }
    #define STATUS_LED_off()	 writeBit(DDRC,0,0)   
    
    // Initialisation of all I/O pins.
    // PC3 - PC7 are output and high.
    #define init_io_pins()       { \
                                   DDRA=0; \
                                   DDRC|=(1<<3)+(1<<4)+(1<<5)+(1<<6); \
				   PORTC|=(1<<3)+(1<<4)+(1<<5)+(1<<6); \
				 }   
				 
    // Config reset jumper on ISP connector.
    // PB5 is used as input with pull-ups and PB7 is used as output
    #define JUMPER_PORT          PORTB
    #define JUMPER_DDR           DDRB
    #define JUMPER_write(value)  writeBit(PORTB,7,value)
    #define JUMPER_read()        readBit(PINB,5)
    #define init_jumper_pins()   { \
                                   writeBit(DDRB,7,1); \
				   writeBit(DDRB,5,0); \
				   writeBit(PORTB,5,1); \
				 }

    // The ADC inputs are not accessible on this harware.
    #define ADC_CHANNELS         0

    // Definition of i/o port bits that are protected.
    // These pins cannot be accessed via io-commands d, p and o.
    // 0x00 means that the whole port is not protected.
    #define protect_PORTA        0xFF
    #define protect_PORTB        0x00
    #define protect_PORTC        0xFC
    #define protect_PORTD        0x00
				    
#endif


#endif // _HW_LAYOUT_H__
