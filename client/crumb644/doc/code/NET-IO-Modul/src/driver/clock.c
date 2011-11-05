#include <avr/interrupt.h>
#include <avr/io.h>
#include "../uip/clock.h"
#include "../uip/timer.h"

// The system clock counts up every 1ms
// It is used by uIP to perform time related stuff.

static int timercounter;

ISR(TIMER0_COMPA_vect)
{
    timercounter++;
}

void clock_init(void)
{
    cli();
    // Timer0 init
    TCCR0A = 0x02;
    TCCR0B = 0x05;
    OCR0A = 20; // compare value, causes timer event every 1ms
    TIMSK0 = 0x02;
    sei();
}

// This function is called by uIP to query the system time in seconds
int clock_time(void)
{
    return(timercounter);
}
