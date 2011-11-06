/*
    CP2201 driver.
    Major parts have been written by S.Perzborn.
    Some extensions have been written by S.Frings.
 */

#include <inttypes.h>
#include <stdlib.h>
#include <avr/io.h>
#include <avr/pgmspace.h>
#include <util/delay.h>
#include <avr/interrupt.h>
#include "CP2200.h"
#include "../hw-layout.h"
#include "../uip/uip.h"
#include "../uip/uip_arp.h"

// timer used by this driver
struct timer cp2200_timer;

// the configuration from flash memory
struct flashconfig config;

#if F_CPU > 40000000
    #error The ethernet driver has been designed for max 20 Mhz. You will need to insert more wait states.
#elif F_CPU > 20000000
    #define wait50ns() { (PINA); (PINA); }  
#else
    #define wait50ns() { (PINA); }
#endif


// ---------------------------------------------
unsigned char CP2200_ReadTXBuffer(unsigned int addr)
{
        write_CP2200(RAMADDRL,addr & 0xFF);
        write_CP2200(RAMADDRH,addr >> 8);
        return (read_CP2200(RAMTXDATA));
}

// ---------------------------------------------
void CP2200_WriteTXBuffer(unsigned int addr, unsigned char value)
{
        write_CP2200(RAMADDRL,addr & 0xFF);
        write_CP2200(RAMADDRH,addr >> 8);
        write_CP2200(RAMTXDATA,value);
}

// ---------------------------------------------
unsigned char CP2200_ReadRXBuffer(unsigned int addr)
{
        write_CP2200(RAMADDRL, (char) (addr & 0xFF));
        write_CP2200(RAMADDRH,(char) (addr >> 8));
        return (read_CP2200(RAMRXDATA));
}

// ---------------------------------------------
void CP2200_WriteRXBuffer(unsigned int addr, unsigned char value)
{
        write_CP2200(RAMADDRL,addr & 0xFF);
        write_CP2200(RAMADDRH,addr >> 8);
        write_CP2200(RAMRXDATA,value);
}

// ---------------------------------------------
u16_t CP2200_ReadPacket(void)
{
        u16_t packetlen;
        u16_t i;
        packetlen = read_CP2200(CPLENH);
        packetlen = packetlen << 8;
        packetlen |= read_CP2200(CPLENL);
        for (i=0;i<packetlen;i++)
        {
                *(uip_buf+i) = read_CP2200(RXAUTORD);
        }
        return(packetlen);
}

// ---------------------------------------------
u16_t network_device_read(void)
{
        u16_t  packetlen=0;
        int cp_bufptr; // Zeiger auf Paket Puffer
        char status = read_CP2200(CP2200_INT0);
        if ((status & 0x02) == 0x02)
        {
                write_CP2200(RXCN,0x01);
                write_CP2200(RXCN,0x00);
        }
        // Paket im Empfangspuffer?
        if (((read_CP2200(CPINFOH) &0x80) != 0) && (read_CP2200(CPINFOL) &0x80) != 0)
        {
                // Zeiger auf Paket Puffer merken
                cp_bufptr = read_CP2200(RXFIFOHEADH);
                cp_bufptr = cp_bufptr << 8;
                cp_bufptr |= read_CP2200(RXFIFOHEADL);
                packetlen = CP2200_ReadPacket();
                write_CP2200(RXCN,0x06); // discharge package
        }
        return(packetlen);
}

// ---------------------------------------------
void CP2200_WritePacket(void)
{
        //unsigned int len;
        unsigned int addr;
        unsigned int t;
        // wait for previous packet complete
        while (read_CP2200(TXBUSY) != 0x00);
        // set transmit buffer pointer
        write_CP2200(TXSTARTH,0x00);
        write_CP2200(TXSTARTL,0x00);
        
        addr=0;
        for (t=0;t < UIP_LLH_LEN;t++)
        {
                CP2200_WriteTXBuffer(addr++, *(uip_buf + t));
        }
        
        if(uip_len <= UIP_LLH_LEN + UIP_TCPIP_HLEN)
        {
                for (t=UIP_LLH_LEN;t < uip_len;t++)
                {
                        CP2200_WriteTXBuffer(addr++, *(uip_buf + t));
                }
        }
        else
        {
                for (t=UIP_LLH_LEN;t < UIP_TCPIP_HLEN+UIP_LLH_LEN;t++)
                {
                        CP2200_WriteTXBuffer(addr++, *(uip_buf + t));
                }
                //hwsend(uip_appdata, uip_len - UIP_TCPIP_HLEN - UIP_LLH_LEN);
                for (t=0;t < uip_len - UIP_TCPIP_HLEN - UIP_LLH_LEN;t++)
                {
                        CP2200_WriteTXBuffer(addr++, *(((unsigned char *)uip_appdata) + t));
                }
        }

        // set packet end
        addr--;
        write_CP2200(TXENDH,(char) (addr >> 8));
        write_CP2200(TXENDL,(char) (addr & 0xFF));
        // set packet start
        write_CP2200(TXSTARTH,0x00);
        write_CP2200(TXSTARTL,0x00);
        // send packet
        write_CP2200(TXCN,0x01);
}


void network_device_send(void)
{
        CP2200_WritePacket();
}



void cpWriteMac(char adr,int value)
{
        write_CP2200(MACADDR,adr);
        write_CP2200(MACDATAH,value>>8);
        write_CP2200(MACDATAL,value);
        write_CP2200(MACRW,1);
}

void write_CP2200(int adr, char value)
{
	nCSETH(0); // clear nCS
        ALE(1); // set ALE
        ADBUS_output(); // AD0..7 output
	ADBUS_write(adr); // output address
        wait50ns();
        ALE(0); // clear ALE
        wait50ns();
        nWR(0); // clear nWR
        ADBUS_write(value); // output data
	wait50ns();
	wait50ns();
	wait50ns();
	wait50ns();
	nWR(0); // clear nWR
	ADBUS_write(value); // wait state
        nWR(1); // set nWR
        wait50ns();
        nCSETH(1); // set nCS
        ADBUS_input(); // AD0..7 Tri-state
}


u8_t read_CP2200(int adr)
{
        char value;
        nCSETH(0); // clear nCS
        ALE(1); // set ALE
        ADBUS_output(); // AD0..7 output
        ADBUS_write(adr); // output address
        wait50ns();
        ALE(0); // clear ALE
        wait50ns();
        ADBUS_input(); // AD0..7 input
        nRD(0); // clear nRD
	wait50ns();
	wait50ns();
	wait50ns();
	wait50ns();
        value = ADBUS_read(); // input D0..7
        nRD(1); // set nRD
        nCSETH(1); // set nCS
        return(value);
}


//----------------------------------------------------------------------------
// The following functions have been added by Stefan Frings

// Initialize CP2200, establish physical network connection
// Returns 1 on success, 0 in case of a timeout

int init_CP2200(void)
{
        unsigned char tmp;
        unsigned int tmp16;

	// timeout 5 seconds
	timer_set(&cp2200_timer, CLOCK_SECOND*5); // 5s  
        
        // SW reset. Commented out because the chip performarns a hardware 
	// reset during power-on automatically and the software reset would
	// pull down the /LRST line which is possibly not wanted.
        //write_CP2200(SWRST,4);
        //_delay_ms(100);

        // Enable link LED and pull-ups
        write_CP2200(IOPWR,0x0C);

        //PHY INIT
        write_CP2200(PHYCN,0x00);
        write_CP2200(PHYCN,DPLXMD); // full duplex
        write_CP2200(PHYCF,SMSQ|LINKINTG|JABBER|AUTOPOL);
         _delay_ms(10);
        write_CP2200(PHYCN,DPLXMD|PHYEN);
        _delay_ms(10);
        write_CP2200(PHYCN,DPLXMD|PHYEN|TXEN|RXEN);
	_delay_ms(100);

        // wait for the physical link to come up
        while (!(read_CP2200(PHYCN) & LINKSTA)) {
	    if (timer_expired(&cp2200_timer)) {
	        return 0;
            }
            _delay_ms(100);
	}

        // read configuration from flash
        readFlashConfig();
	
        //MAC INIT
        cpWriteMac(MACCF,0x40B3); // full duplex
        cpWriteMac(IPGT,0x0015);  // full duplex
        cpWriteMac(MACCF,0x40B3);
        cpWriteMac(MAXLEN,0x05EE);
        write_CP2200(FLASHADDRL,0xFA);
        write_CP2200(FLASHADDRH,0x1F);
        tmp16 = 0x0000;
        tmp16 = read_CP2200(FLASHAUTORD);
        tmp16 |=read_CP2200(FLASHAUTORD)<<8;
        cpWriteMac(MACAD2,tmp16);
        tmp16 = 0x0000;
        tmp16 = read_CP2200(FLASHAUTORD);
        tmp16 |= read_CP2200(FLASHAUTORD)<<8;
        cpWriteMac(MACAD1,tmp16);
        tmp = 0x0000;
        tmp16 = read_CP2200(FLASHAUTORD);
        tmp16 |= read_CP2200(FLASHAUTORD)<<8;
        cpWriteMac(MACAD0,tmp16);
        cpWriteMac(MACCN,0x01);

        // Enable Packet Receive and Receive FIFO full interrupts
        write_CP2200(INT0EN,0x03);
        write_CP2200(INT1EN,0x00);
        
        // clear interrupt status
        read_CP2200(CP2200_INT0);
        read_CP2200(CP2200_INT1);
        struct uip_eth_addr eaddr;

        // configure uIP with own mac address (get from CP2200 flash memory)
        // added by Stefan Frings
        write_CP2200(FLASHADDRL,0xFA);
        write_CP2200(FLASHADDRH,0x1F);
        for (int i=0;i<6;i++)
        {
            eaddr.addr[i]=read_CP2200(FLASHAUTORD); // mac address from CP2200
        }
        // set mac address of this modules
        uip_setethaddr(eaddr);

	return 1;
}


// Read IP configuration from flash.
// The first page of flash memory is used for that.

void readFlashConfig() {
    u16_t adr=FLASHCONFIG;
    write_CP2200(FLASHADDRL,adr & 0xFF);
    write_CP2200(FLASHADDRH,adr >> 8);
    u8_t* ptr=(u8_t*)&config;
    for (int i=0; i<sizeof(config); i++) {
        ptr[i]=read_CP2200(FLASHAUTORD);
    }
};


// write a single byte into the flash.
// the flash must be erased before that.

void writeflash(u16_t adr, u8_t data) {
    // unlock the flash write protection
    cli();
    write_CP2200(FLASHKEY,0xA5);
    write_CP2200(FLASHKEY,0xF1);
    // select the write address
    write_CP2200(FLASHADDRL,adr & 0xFF);
    write_CP2200(FLASHADDRH,adr >> 8);
    write_CP2200(FLASHDATA,data);
    sei();
    // wait until writing finishes
    u8_t flash_status;
    do {
        flash_status=read_CP2200(FLASHSTA);
    } while ((flash_status & 8)>0);
}


// Write IP configuration into flash.
// The first page of flash memory is used for that.

void writeFlashConfig() {
    u16_t adr=FLASHCONFIG;
    // unlock the flash write protection
    cli();
    write_CP2200(FLASHKEY,0xA5);
    write_CP2200(FLASHKEY,0xF1);
    // erase the page with config settings
    write_CP2200(FLASHADDRL,adr & 0xFF);
    write_CP2200(FLASHADDRH,adr >> 8);
    write_CP2200(FLASHERASE,0x01);
    sei();
    // wait until erasing finishes
    u8_t flash_status;
    do {
        flash_status=read_CP2200(FLASHSTA);
    } while ((flash_status & 8)>0);
    u8_t* ptr= (u8_t*) &config;
    for (int i=0; i<sizeof(config); i++) {
        writeflash(adr++,ptr[i]);
    }
};


// Read a single character from the flash memory at the given adress.
// code 255 is converted to code 0.

u8_t readFlashChar(u16_t adr) {
    char c;
    write_CP2200(FLASHADDRL,adr & 0xFF);
    write_CP2200(FLASHADDRH,adr >> 8);
    c=read_CP2200(FLASHAUTORD);
    if (c==255)
        c=0;
    return c;
}


// Read a String from the flash memory at the given adress.

void readFlashString(char* buffer, u16_t adr) {
    char c;
    write_CP2200(FLASHADDRL,adr & 0xFF);
    write_CP2200(FLASHADDRH,adr >> 8);
    do {
        c=read_CP2200(FLASHAUTORD);
        // Replace ascii 255 by ascii 0.
        // Epty flash cells have that value
        if (c==255)
          c=0;
        buffer[0]=c;
        buffer++;
    }
    while (c!=0);
}



// Write String to the flash memory at the given adress.
// The string must be 0-512 characters long and it must start at a 512-byte boundary.

void writeFlashString(char* string, u16_t adr) {
    u8_t c;
    // protect the first and last page, they are reserved for IP config and MAC address
    if (adr<512 || adr>7679)
        return;    
    // unlock the flash write protection
    cli();
    write_CP2200(FLASHKEY,0xA5);
    write_CP2200(FLASHKEY,0xF1);
    // erase the page
    write_CP2200(FLASHADDRL,adr & 0xFF);
    write_CP2200(FLASHADDRH,adr >> 8);
    write_CP2200(FLASHERASE,0x01);
    sei();
    // wait until erasing finishes
    u8_t flash_status;
    do {
        flash_status=read_CP2200(FLASHSTA);
    } while ((flash_status & 8)>0);
    do {
      c=string[0];
      writeflash(adr++,c);
      string++;
      // protect the last page, they are reserved for IP config and MAC address
      if (adr>7679)
        return;    
    }
    while (c!=0);
};