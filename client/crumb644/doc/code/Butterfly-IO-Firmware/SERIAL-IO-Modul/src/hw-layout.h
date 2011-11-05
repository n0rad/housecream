#ifndef _HW_LAYOUT_H__
#define _HW_LAYOUT_H__


// This file is used to adapt the firmware to different hardware layouts


// Macro used to write to a single I/O pin
#define writeBit(port,bit,value) { if ((value)>0) (port) |= (1<<bit); else (port) &= ~(1<<bit); } 

// Macro used to read from a single I/O pin
#define readBit(port,bit) (((port) >> (bit)) & 1)



// Crumb8-USB from www.chip45.com
#ifdef Crumb8_USB
    // The status LED is accessed in open-drain mode
    #define STATUS_LED_on()      { writeBit(DDRB,2,1); writeBit(PORTB,2,0); }
    #define STATUS_LED_off()	 writeBit(DDRB,2,0)
    
    // Initialisation of all I/O pins.
    // by default, all pins are input without pull-up.
    #define init_io_pins()       { }
				 
    // Number of ADC channels
    #define ADC_CHANNELS         8
                                   
    // Definition of i/o ports that are write protected.
    // These pins cannot be accessed via io-commands d, p and o.
    // 0x00 means that all 8 bits are not protected.
    #define protect_PORTB        0x00
    #define protect_PORTC        0x00
    #define protect_PORTD        0x00
#endif


// Crumb128 v3 from www.chip45.com
#ifdef Crumb128
    // The status LED is accessed in open-drain mode
    #define STATUS_LED_on()      { writeBit(DDRB,7,1); writeBit(PORTB,7,0); }
    #define STATUS_LED_off()     writeBit(DDRB,7,0)

    // Initialisation of all I/O pins.
    // by default, all pins are input without pull-up.
    #define init_io_pins()       { }

    // Number of ADC channels
    #define ADC_CHANNELS         8

    // Use second serial port
    #define USE_SERIAL2

    // Definition of i/o ports that are write protected.
    // These pins cannot be accessed via io-commands d, p and o.
    // 0x00 means that all 8 bits are not protected.
    #define protect_PORTA        0x00
    #define protect_PORTB        0x00
    #define protect_PORTC        0x00
    #define protect_PORTD        0x00
    #define protect_PORTE        0x00
    #define protect_PORTF        0x00
    #define protect_PORTG        0x00
#endif


// Crumb168-USB from www.chip45.com
#ifdef Crumb168_USB
    // The status LED is accessed in open-drain mode
    #define STATUS_LED_on()      { writeBit(DDRB,2,1); writeBit(PORTB,2,0); }
    #define STATUS_LED_off()     writeBit(DDRB,2,0)

    // Initialisation of all I/O pins.
    // by default, all pins are input without pull-up.
    #define init_io_pins()       { }

    // ADC hardware settings
    #define ADC_CHANNELS         8
    #define EXT_AREF             5.00

    // Definition of i/o ports that are write protected.
    // These pins cannot be accessed via io-commands d, p and o.
    // 0x00 means that all 8 bits are not protected.
    #define protect_PORTB        0x00
    #define protect_PORTC        0x00
    #define protect_PORTD        0x00
#endif


// Crumb644 from www.chip45.com
#ifdef Crumb644
    // The status LED is accessed in open-drain mode
    #define STATUS_LED_on()      { writeBit(DDRD,7,1); writeBit(PORTD,7,0); }
    #define STATUS_LED_off()     writeBit(DDRD,7,0)

    // Initialisation of all I/O pins.
    // by default, all pins are input without pull-up.
    #define init_io_pins()       { }

    // Number of ADC channels
    #define ADC_CHANNELS         8

    // Definition of i/o ports that are write protected.
    // These pins cannot be accessed via io-commands d, p and o.
    // 0x00 means that all 8 bits are not protected.
    #define protect_PORTA        0x00
    #define protect_PORTB        0x00
    #define protect_PORTC        0x00
    #define protect_PORTD        0x00
#endif

// Crumb2560 from www.chip45.com
#ifdef Crumb2560
    // This modules does not have LED's
    #define STATUS_LED_on()      { }
    #define STATUS_LED_off()     { }

    // Initialisation of all I/O pins.
    // by default, all pins are input without pull-up.
    #define init_io_pins()       { }

    // Number of ADC channels
    #define ADC_CHANNELS         8

    // Definition of i/o ports that are write protected.
    // These pins cannot be accessed via io-commands d, p and o.
    // 0x00 means that all 8 bits are not protected.
    #define protect_PORTA        0x00
    #define protect_PORTB        0x00
    #define protect_PORTC        0x00
    #define protect_PORTD        0x00
    #define protect_PORTE        0x00
    #define protect_PORTF        0x00
    #define protect_PORTG        0x00
    #define protect_PORTH        0x00
    #define protect_PORTJ        0x00
    #define protect_PORTK        0x00
    #define protect_PORTL        0x00
#endif


#endif // _HW_LAYOUT_H__
