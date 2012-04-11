#include "network.h"

void networkSetup() {
    /*initialize enc28j60*/
    es.ES_enc28j60Init(mac);
    es.ES_enc28j60clkout(2); // change clkout from 6.25MHz to 12.5MHz
    delay(10);
    /* Magjack leds configuration, see enc28j60 datasheet, page 11 */
    // LEDA=greed LEDB=yellow
    //
    // 0x880 is PHLCON LEDB=on, LEDA=on
    // enc28j60PhyWrite(PHLCON,0b0000 1000 1000 00 00);
    es.ES_enc28j60PhyWrite(PHLCON, 0x880);
    delay(500);
    // 0x990 is PHLCON LEDB=off, LEDA=off
    // enc28j60PhyWrite(PHLCON,0b0000 1001 1001 00 00);
    es.ES_enc28j60PhyWrite(PHLCON, 0x990);
    delay(500);
    // 0x880 is PHLCON LEDB=on, LEDA=on
    // enc28j60PhyWrite(PHLCON,0b0000 1000 1000 00 00);
    es.ES_enc28j60PhyWrite(PHLCON, 0x880);
    delay(500);
    // 0x990 is PHLCON LEDB=off, LEDA=off
    // enc28j60PhyWrite(PHLCON,0b0000 1001 1001 00 00);
    es.ES_enc28j60PhyWrite(PHLCON, 0x990);
    delay(500);
    // 0x476 is PHLCON LEDA=links status, LEDB=receive/transmit
    // enc28j60PhyWrite(PHLCON,0b0000 0100 0111 01 10);
    es.ES_enc28j60PhyWrite(PHLCON, 0x476);
    delay(100);
    //init the ethernet/ip layer:
    es.ES_init_ip_arp_udp_tcp(mac, ip, port);
}
