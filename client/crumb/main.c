#include <avr/interrupt.h>
#include <inttypes.h>
#include <stdlib.h>
#include <avr/io.h>
#include <avr/pgmspace.h>
#include <util/delay.h>
#include "driver/CP2200.h"
#include "uip/uip.h"
#include "uip/uip_arp.h"
#include "httpd/httpd.h"
#include "socketd/socketd.h"
#include "uip/timer.h"
#include "io-commands.h"
#include "driver/ADC.h"
#include "hw-layout.h"
#include "main.h"

#ifdef EMAIL_APP
#include "email_app/email_app.h"
#endif //EMAIL_APP

#ifdef SERIAL
#include "seriald/seriald.h"
#endif //SERIAL

#define BUF ((struct uip_eth_hdr *)&uip_buf[0])

//
//typedef struct s_device {
//  char		   *software;
//  char
//
//  char         mode; // NOTUSED (-1), INPUT (0) , OUTPUT(1), PWM (2), ANALOG (3), DIGITAL (4)
//  int          startValue; // 0-1 for digital, 0-255 for PWM
//  char         *description;
//} t_device;






// Set hardcoded default IP settings
void set_defaults(void) {
    config.dhcpenable=1;
    uip_ipaddr(config.ipaddr, 192,168,42,42);
    uip_ipaddr(config.netmask, 255,255,255,0);
    uip_ipaddr(config.gateway, 192,168,42,2);
    // clear password
    config.authentication[0]=0; 
    writeFlashConfig();
};



void IP_config(void) {
#if UIP_UDP
    // if DHCP is enabled, then initialize via DHCP, else use static settings
    if (config.dhcpenable) {
        dhcpc_init();
    }
    else {
        uip_sethostaddr(config.ipaddr);
        uip_setnetmask(config.netmask);
        uip_setdraddr(config.gateway);
    }
#else // UIP_UDP
    // configure the interface with static settings
    uip_sethostaddr(config.ipaddr);
    uip_setnetmask(config.netmask);
    uip_setdraddr(config.gateway);
#endif // UIP_UDP
}


// Check if reset jumper is connected
// and if yes, then reset the configuration to default settings.
void check_for_reset_jumper(void) {
    // Save the original pin status
    uint8_t old_ddr=JUMPER_DDR; 
    uint8_t old_port=JUMPER_PORT;
    // Configure pin i/o direction
    init_jumper_pins();
    // toggle the output pin several times and check if the input pin follows
    JUMPER_write(1);    
    _delay_ms(1);
    if (JUMPER_read()!=0) {  
        JUMPER_write(0);    
        _delay_ms(1);
        if (JUMPER_read()==0) {         
            JUMPER_write(1);    
            _delay_ms(1);
            if (JUMPER_read()!=0) {        
                JUMPER_write(0);    
                _delay_ms(1);
                if (JUMPER_read()==0) {  
                     // the jumper is connected
		     
		     // Wait a little bit for the case that the ethernet 
		     // controller has slower startup than the AVR.
                     _delay_ms(100);
		     
		     // reset configuration to default values
		     set_defaults();
		     
                     // wait until the jumper gets removed
                     while (JUMPER_read()==0) {};
                }
            }
        }
    }
    // restore the original status of these pins
    JUMPER_PORT=old_port;
    JUMPER_DDR=old_ddr;
}


int main(void) {
    struct timer periodic_timer, arp_timer;
          
    // Flash the status LED two times to indicate that the AVR controller is living
    STATUS_LED_on();
    _delay_ms(20);
    STATUS_LED_off();
    _delay_ms(100);
    STATUS_LED_on();
    _delay_ms(20);
    STATUS_LED_off();

    // Initialize direction and level of I/O pins
    init_io_pins(); 

    #if ADC_CHANNELS > 0
        // initialize ADC converter
        initADC();
    #endif
    
    // enable io functions
    isBusy=0;
  
    // Initialize system clock timers.
    clock_init();
    timer_set(&periodic_timer, CLOCK_SECOND); 
    timer_set(&arp_timer, CLOCK_SECOND*10);

    // check if the config reset jumper is connected.
    check_for_reset_jumper();    
    
#ifdef SERIAL
    // start the serial port server
    seriald_init();
#endif // SERIAL
    
    // initialize ethernet controller
    // loop until link comes up
    while (init_CP2200()==0) {
       _delay_ms(200);
    }
    
    // replace config by hardcoded defaults if the flash is unprogrammed (all bits 1)
    if (uip_ipaddr_cmp(config.ipaddr, all_ones_addr) &&
        uip_ipaddr_cmp(config.netmask, all_ones_addr) &&
        uip_ipaddr_cmp(config.gateway, all_ones_addr)) {
        set_defaults();
    }

    // initialize uIP protocol
    uip_arp_init();
    uip_init();
    
    // Configure the IP-Adrdess
    IP_config();
    
 #ifdef EMAIL_APP
    // Initialize the email application
    email_app_init();
#endif // EMAIL_APP

    // Initialize the inetd
    inetd_init();

    // main loop, dispatches network and timer events
    while (1) {
        uip_len = network_device_read();
        if (uip_len > 0) {
            if (BUF->type == htons(UIP_ETHTYPE_IP)) {
                uip_arp_ipin();
                uip_input();
                // If the above function invocation resulted in data that
                // should be sent out on the network, the global variable
                // uip_len is set to a value > 0. 
                if (uip_len > 0) {
                    uip_arp_out();
                    network_device_send();
                }
            } else if (BUF->type == htons(UIP_ETHTYPE_ARP)) {
                uip_arp_arpin();
                // If the above function invocation resulted in data that
                // should be sent out on the network, the global variable
                // uip_len is set to a value > 0. 
                if (uip_len > 0) {
                    network_device_send();
                }
            }
        }
        else if (timer_expired(&periodic_timer)) {
            timer_reset(&periodic_timer);

            for (int i = 0; i < UIP_CONNS; i++) {
                uip_periodic(i);
                // If the above function invocation resulted in data that
                // should be sent out on the network, the global variable
                // uip_len is set to a value > 0. 
                if (uip_len > 0) {
                    uip_arp_out();
                    network_device_send();
                }
            }

#if UIP_UDP
            for (int i = 0; i < UIP_UDP_CONNS; i++) {
                uip_udp_periodic(i);
                // If the above function invocation resulted in data that
                // should be sent out on the network, the global variable
                // uip_len is set to a value > 0.
                if (uip_len > 0) {
                    uip_arp_out();
                    network_device_send();
                }
            }
#endif // UIP_UDP

            // Call the ARP timer function every 10 seconds. 
            if (timer_expired(&arp_timer)) {
                timer_reset(&arp_timer);
                uip_arp_timer();
            }
        }
        
#ifdef EMAIL_APP
        email_app_main_loop();
#endif //EMAIL_APP
		
    }
    return 0;
}

