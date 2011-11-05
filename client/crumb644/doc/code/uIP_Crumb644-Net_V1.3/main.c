/*
	Crumb644-Net uIP Webserver example 
	Adam Dunkels uIP adapted to Crumb644-Net by S.Perzborn v1.3 [11.01.2009]
	(stefan@perzborn.net)
 * ----------------------------------------------------------------------------
  Programm Fuse Bits (20MHz crystal, no Prescaler, no JTAG) : 
	  avrdude -p atmega644 -P lpt1 -c stk200 -F -u -U lfuse:w:0xF7:m
    avrdude -p atmega644 -P lpt1 -c stk200 -F -u -U hfuse:w:0xD9:m
  Programm Fuse Bits (20MHz crystal, no Prescaler, no JTAG, Clockout at PB1) : 
	  avrdude -p atmega644 -P lpt1 -c stk200 -F -u -U lfuse:w:0xB7:m
    avrdude -p atmega644 -P lpt1 -c stk200 -F -u -U hfuse:w:0xD9:m
	R5 = 220pF (capacitor)
 * ----------------------------------------------------------------------------
 IP-address (192.168.178.14), netmask (255.255.255.0) and gateway (192,168,178,1) 
 are predefined in module CP2200.c function network_device_init()
 Type HELP at serial (RS232) interface for information about how to display and 
 change IP address and netmask in running mode
 (Serial interface parameter: 19200 baud, 8 databits, no parity, 1 stopbit)
 
	IDE:
	AVR Studio 4.13 Service Pack 2

  Compiler: winavr-20080610 
  Compilersettings: 
  Device: atmega644
  Frequency: 200000000 Hz
  Optimisation: -Os

	(x) Unsigned Chars
  ( ) Unsigned Bitfields
	( ) Pack Structure Members
	( ) Short Enums
 */
 /*
	Changes:
	--> v1.2 [11.01.2009]: Fixed conflicting types "strchr_P"
					     					 used compiler version: WinAVR-20080610 (AVR-Studio 4)
	--> v1.3 [23.01.2009]: Add switchable PIO (PC0/green LED) through webbrowser 
 */


#ifndef F_CPU
#define F_CPU 20000000
#endif

#define UIP_CONF_IPV6 0

#include <avr/interrupt.h>
#include <inttypes.h>
#include <stdlib.h>   // malloc
#include <string.h>
#include <avr/io.h>
#include <avr/pgmspace.h>
#include <util/delay.h>
#include "CP2200.h"


#include "uip.h"
#include "uip_arp.h"
//#include "network-device.h"
#include "httpd.h"
#include "timer.h"
#define BUF ((struct uip_eth_hdr *)&uip_buf[0])


/*---------------------------------------------------------------------------*/
 
void InitUART(void);
void uart_puts (char *s);
char uart_getc(void);
void DisplayMem(char *start, char *ende);
void DisplayMem_P(int start, int ende);
int hextoi(char *hex);
void itohex(char *hexstr, int dez, char length);
int uart_putc_P(PGM_P c);
void uart_puts_P (PGM_P s);
void PrintTXBuffer(unsigned int start, unsigned char len);

extern void write_CP2200(int adr, char value);
extern char read_CP2200(int adr);
extern void network_device_init(void);
extern u16_t network_device_read(void);
extern void network_device_send(void);


// globale Variablen
static char toggle=0;
static char time;
//static char CP2200_status=0;
clock_time_t  sec_counter;

ISR(TIMER0_COMPA_vect)
{
	if (--time == 0)
	{
		// 1 Sekunden Timer
		time=75;
		sec_counter++;
		if (toggle==0)
		{
			PORTC &= ~(1<<PC1); // clear PC1 = LED ON
			toggle=1;
		}
		else
		{
			PORTC |= (1<<PC1); // set PC1 = LED OFF
			toggle=0;	
		}
	}
}



ISR(INT1_vect)
{
	PORTB &= ~(1<<PB0); // clear PB0 = LED ON
}


void InitUART(void)
{
	UBRR0H = 0;
	UBRR0L = 64;
	UCSR0B = (1<<RXEN0) | (1<<TXEN0); // transmit & receive enable 
	UCSR0C = 0x06; // init RS232: 8,N,1
	UCSR0A = 0x00; 
}
 
// putc fuer AVR mit einem UART (z.B. AT90S8515)
int uart_putc_P(PGM_P c)
{
    while(!(UCSR0A & (1 << UDRE0))); // warte, bis UDR bereit 
    UDR0 = pgm_read_byte(c);                     // sende Zeichen 
    return 0;
}

// putc fuer AVR mit einem UART (z.B. AT90S8515)
int uart_putc(unsigned char c)
{
	  while(!(UCSR0A & (1<< UDRE0))); // warte, bis UDR bereit 
    //while(!(USR & (1 << UDRE))); // warte, bis UDR bereit 
    UDR0 = c;                     // sende Zeichen 
    return 0;
}
 
void uart_puts (char *s)
{
    while (*s)
    {   
        uart_putc(*s);
        s++;
    }
} 

// konstante aus Flash
void uart_puts_P (PGM_P s)
{
    while (pgm_read_byte(s))
    {   
        uart_putc_P(s);
        s++;
    }
} 

char uart_getc(void)
{
  while (!(UCSR0A & (1<<RXC0)));  // warten bis Zeichen verfuegbar
    return UDR0;                   // Zeichen aus UDR an Aufrufer zurueckgeben
}





// itohex( hexstring, dezimalwert, anzahl der Stellen
void itohex(char *hexstr, int dez, char length)
{
	char i=0;
	char tmp=0;
	if (length > 3)
	{
		tmp=(dez & 0xF000) >> 12;
		if (tmp > 9)
			hexstr[(int)i] = tmp + 'A' - 10;
		else
			hexstr[(int)i] = tmp + '0';
		i++;
	}
	if (length > 2)
	{
		tmp=(dez & 0x0F00) >> 8;
		if (tmp > 9)
			hexstr[(int)i] = tmp + 'A' - 10;
		else
			hexstr[(int)i] = tmp + '0';
		i++;
	}
	if (length > 1)
	{
		tmp=(dez & 0x00F0) >> 4;
		if (tmp > 9)
			hexstr[(int)i] = tmp + 'A' - 10;		
		else
			hexstr[(int)i] = tmp + '0';
			
		i++;
	}
	if (length > 0)
	{
		tmp=dez & 0x000F;
		if (tmp > 9)
			hexstr[(int)i] = tmp + 'A' - 10;			
		else
			hexstr[(int)i] = tmp + '0';
		i++;
	} 
	hexstr[(int)i]=0;
}
/*
// itohex( hexstring, dezimalwert, anzahl der Stellen
void itohex(char *hexstr, int dez, char length)
{
	//signed char i=0;
	char tmp=0;
	hexstr[(int)length]=0;
	while (length > 0)
	{
		length--;
		tmp=(dez >> (length * 4)) & 0x0F;
		if (tmp > 9)
			hexstr[(int)length] = tmp + 'A' - 10;
		else
			hexstr[(int)length] = tmp + '0';
	}
}
*/

/*
int hextoi(char *hex)
{
  int dez=0;
  int i=0;
	strupr(hex);
  for (i=0;((i<4)&&(hex[i]!=0));i++)
  {
    dez = dez << 4;
    if (hex[i] > '9')
      dez = dez + (hex[i] - 'A' + 10);
    else
      dez = dez + hex[i] - '0';
  }
  return(dez);
}
*/

void clock_init(void)
{
	cli();
	// Timer0 init
	TCCR0A = 0x02;
	TCCR0B = 0x05;
	OCR0A = 0x80; // compare value
	TIMSK0 = 0x02;
	sei();
}
	
clock_time_t clock_time(void)
{
	return(sec_counter);
}

void uip_log(char *text)
{
	uart_puts(text);
}

// Groesse des freien RAM Speicher ermitteln
unsigned int AvailRAM(void)
{
	char tmpstr[10];
	unsigned int *p=0;
	size_t i;
	for (i=4000;i>1; i-=10)
	{
		p = (unsigned int *) malloc(i);
		if (p != 0) break;
	}
	if (p != 0) free(p);
	uart_puts_P(PSTR("available RAM memory:"));
	itoa(i,tmpstr,10);
	uart_puts(tmpstr);
	uart_puts_P(PSTR(" bytes \r\n"));
	
	return(i);
}


void cmd_ip(char *ipstr)
{
	char *ip = "....";
	char tmpstr[10];
	char *tmp=0;
	unsigned char i=0;
	uip_ipaddr_t ipaddr;
	//		struct uip_eth_addr eaddr;
	i=0;
	uip_gethostaddr(&ipaddr);
	ip = (char *) &ipaddr;

	if (*ipstr == 0)
	{
		// show ip address
		uart_puts(itoa(ip[i],tmpstr,10));
		i++;
		for (;i<4;i++)
		{
			uart_puts_P(PSTR("."));
			uart_puts(itoa((unsigned char) ip[i],tmpstr,10));
		}
		uart_puts_P(PSTR("\r\n"));
	}	
	else
	{
		// set ip address
		tmp=ipstr;		
		i=0;
		for (i=0;((*ipstr != 0) && (i<4));ipstr++)
		{
			if (*ipstr=='.') 
			{	
				*ipstr=0;
				ip[i++]=atoi(tmp);
				tmp = ipstr + 1;
			}
		}
		*ipstr=0;
		ip[i]=atoi(tmp);
		uip_ipaddr(ipaddr, ip[0],ip[1],ip[2],ip[3]);
		uip_sethostaddr(ipaddr);			
	}
} // cmd_ip

void cmd_netmask(char *ipstr)
{
	char *ip = "....";
	char tmpstr[10];
	char *tmp=0;
	unsigned char i=0;
	uip_ipaddr_t ipaddr;
	//struct uip_eth_addr eaddr;
	i=0;
	uip_getnetmask(&ipaddr);
	ip = (char *) &ipaddr;
	
	if (*ipstr == 0)
	{
		// show netmask
		uart_puts(itoa(ip[i],tmpstr,10));
		i++;
		for (;i<4;i++)
		{
			uart_puts_P(PSTR("."));
			uart_puts(itoa(ip[i],tmpstr,10));
		}
		uart_puts_P(PSTR("\r\n"));
	}	
	else
	{
		// set netmask
		tmp=ipstr;		
		i=0;
		for (i=0;((*ipstr != 0) && (i<4));ipstr++)
		{
			if (*ipstr=='.') 
			{	
				*ipstr=0;
				ip[i++]=atoi(tmp);
				tmp = ipstr + 1;
			}
		}
		*ipstr=0;
		ip[i]=atoi(tmp);
		uip_ipaddr(ipaddr, ip[0],ip[1],ip[2],ip[3]);
		uip_setnetmask(ipaddr);			
	}
} // cmd_netmask

void cmd_help(void)
{
	uart_puts_P(PSTR("\r\nAdam Dunkels uIP webserver example for Crumb644-NET\r\n"));
	uart_puts_P(PSTR(" *** version 1. adapted by S.Perzborn [23.01.2009] *** \r\n"));
	uart_puts_P(PSTR(" *** web: www.perzborn.net \r\n"));
	uart_puts_P(PSTR(" *** email: stefan@perzborn.net \r\n"));
	uart_puts_P(PSTR(" serial: 19200 baud 8N1 \r\n"));
	uart_puts_P(PSTR(" Commands: \r\n"));
	uart_puts_P(PSTR("  CPRESET                     // reinitialize ethernet controller \r\n"));
	uart_puts_P(PSTR("  IP <xxx.xxx.xxx.xxx>        // display / set IP address\r\n"));
	uart_puts_P(PSTR("  NETMASK <xxx.xxx.xxx.xxx>   // display / set netmask\r\n"));
	uart_puts_P(PSTR("  \r\n"));
	_delay_ms(50);
}

void parse(char *zeile)
{
	char *pcmd=0;
	char *argv[80];
	int argc=0;
	char i=0;
		//char tmpstr[10];
  char unknown=1;
	
	// Eingabestrings in Teilstrings wandeln
	// Trennzeichen = Leerzeichen
	// argv[0..9] = Teilstrings (Nullterminiert) 
	// argc = Anzahl der Teilstrings (1..10) 
	// Bei keiner Eingabe (nur Enter) ist argc = 1
	for(;argc<10;)
	{
		// führende Leerzeichen überlesen (oder Ende)
		for(;((*(zeile+i) <=' ')&&(*(zeile+i) !=0));i++);
		argv[argc++]=zeile+i;  // pointer auf Teilstring
		if (*(zeile+i) ==0) break;
		// bis nächsten Leerzeichen lesen (oder Ende)
		for(;((*(zeile+i) >' ')&&(*zeile+i !=0));i++);
		if (*(zeile+i)==0) break;
		*(zeile+i) =0; // Teilsstring terminieren
		i++;
  }
		
	pcmd=argv[0];
	strupr(pcmd);
	if ((strcmp_P(pcmd,PSTR("IP"))==0) && (strlen(pcmd) == 2))
	{
		if (argc>1)
			cmd_ip(argv[1]);
		else
			cmd_ip("");
		unknown=0;
	}
	if ((strcmp_P(pcmd,PSTR("NETMASK"))==0) && (strlen(pcmd) == 7))
	{
		if (argc>1)
			cmd_netmask(argv[1]);
		else
			cmd_netmask("");
		unknown=0;
	}
	if ((strcmp_P(pcmd,PSTR("CPRESET"))==0) && (strlen(pcmd) == 7))
	{
		unknown=0;
		uart_puts_P(PSTR("CP2200 reset ..."));
		network_device_init();
		uart_puts_P(PSTR("done \n\r"));
	}
	if ((strcmp_P(pcmd,PSTR("HELP"))==0) && (strlen(pcmd) == 4))
	{
		unknown=0;
		cmd_help();
	}


} // parse


int main(void)
{
	char taste=0;
	char zeile[80];
	int i=0;
	struct timer periodic_timer, arp_timer;
	DDRA = 0x00;    
  DDRB = 0xFF;    
	DDRC = 0xFF;
	PORTA = 0x00;
	PORTB = 0x00;
	PORTC = 0xF7;
	PORTC |= (1 << nRD); // set RD#
	PORTC |= (1 << nWR); // set WR#
	PORTC &= ~(1 << ALE); // clear ALE
	PORTC &= ~(1 << nCS); // clear CS
	PORTC |= (1 << nLRST); // reset aus, CP2200 wieder laufen lassen
	
	InitUART();
	cmd_help();
	AvailRAM();
	_delay_ms(50);
	//Init_CP2000();
	cli(); // disable inerrupts
	// MCUCR |= (1<<ISC11);  // 1 = INT1 fallede Flanke
	// MCUCR &= ~(1<<ISC10); // 0	= INT1 fallede Flanke
	EICRA = 0x08; // INT1 fallede Flanke
	// GIMSK = 0x80; // enable external int1
	EIMSK = 0x02; // enable external int1
	// GIFR = 0x80;
	sei();  // enable interrupts 
	uart_puts_P(PSTR("init CP2200 ..."));
	_delay_ms(10);
	network_device_init();
		cmd_ip("");
	clock_init();
	timer_set(&periodic_timer, CLOCK_SECOND / 2);
	timer_set(&arp_timer, CLOCK_SECOND * 10);
	uart_puts_P(PSTR("\r\nready. \r\n"));
	_delay_ms(10);

	uip_arp_init();
	uip_init();
	httpd_init();



	while(1) 
	{
		uip_len = network_device_read();
		if(uip_len > 0) 
		{
			if(BUF->type == htons(UIP_ETHTYPE_IP)) 
			{
				uip_arp_ipin();
				uip_input();
				/* If the above function invocation resulted in data that
				should be sent out on the network, the global variable
				uip_len is set to a value > 0. */
				if(uip_len > 0) 
				{
					uip_arp_out();
					network_device_send();
				}
			}
			else if(BUF->type == htons(UIP_ETHTYPE_ARP)) 
			{
			
				uip_arp_arpin();
				/* If the above function invocation resulted in data that
				should be sent out on the network, the global variable
				uip_len is set to a value > 0. */
				if(uip_len > 0) 
				{
					network_device_send();
				}
			}
		} 
		else if(timer_expired(&periodic_timer)) 
		{
			timer_reset(&periodic_timer);
			for(i = 0; i < UIP_CONNS; i++) 
			{
				uip_periodic(i);
				/* If the above function invocation resulted in data that
				should be sent out on the network, the global variable
				uip_len is set to a value > 0. */
				if(uip_len > 0) 
				{
					uip_arp_out();
					network_device_send();
				}	
			}

			/*#if UIP_UDP
			for(i = 0; i < UIP_UDP_CONNS; i++) 
			{
				uip_udp_periodic(i);
				//If the above function invocation resulted in data that
				//should be sent out on the network, the global variable
				//uip_len is set to a value > 0. 
				if(uip_len > 0) 
				{
					uip_arp_out();
					network_device_send();
				}
			}
			#endif // UIP_UDP */
			/* Call the ARP timer function every 10 seconds. */
			if(timer_expired(&arp_timer)) 
			{
				timer_reset(&arp_timer);
				uip_arp_timer();
			}
		}

		
		if (UCSR0A & (1<<RXC0))  // Zeichen verfuegbar?
		{
			taste = UDR0; // Zeichen abholen
			uart_putc(taste); // Zeichen ausgeben
			if (taste == 13)
			{
				uart_putc('\n');
				zeile[i]=0;
				parse(zeile);	
				uart_puts_P(PSTR("#"));
				i=0;
				
			}
			
			if (taste == 8)
			{
				// Backspace key
				if (i>0) 
				{
					i--;
					uart_puts_P(PSTR("\x1B[P")); // VT100-Code Backspace
				}
			}	
			else
			{
				if (i<78)
					zeile[i++]=taste; // Zeichen merken
			
			}
		}		
	}
	return 0;
}	// main
	
