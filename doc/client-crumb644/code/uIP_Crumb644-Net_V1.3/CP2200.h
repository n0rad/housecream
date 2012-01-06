#define ALE 3
#define nWR 4
#define nRD 5
#define nCS 6
#define nLRST 7

#define CPADDRH 0x21 // Current RX Packet Address High Byte page 65
#define CPADDRL 0x22 // Current RX Packet Address Low Byte page 65
#define CPINFOH 0x1D // Current RX Packet Information High Byte page 63
#define CPINFOL 0x1E // Current RX Packet Information Low Byte page 64
#define CPLENH 0x1F // Current RX Packet Length High Byte page 64
#define CPLENL 0x20 // Current RX Packet Length Low Byte page 64
#define CPTLB 0x1A // Current RX Packet TLB Number page 67
#define FLASHADDRH 0x69 // Flash Address Pointer High Byte page 76
#define FLASHADDRL 0x68 // Flash Address Pointer Low Byte page 76
#define FLASHAUTORD 0x05 // Flash AutoRead w/ increment page 77
#define FLASHDATA 0x06 // Flash Read/Write Data Register page 77
#define FLASHERASE 0x6A // Flash Erase page 77
#define FLASHKEY 0x67 // Flash Lock and Key page 76
#define FLASHSTA 0x7B // Flash Status page 75
#define CP2200_INT0 0x63 // Interrupt Status Register 0 (Self-Clearing) page 31
#define INT0EN 0x64 // Interrupt Enable Register 0 page 33
#define INT0RD 0x76 // Interrupt Status Register 0 (Read-Only) page 32
#define CP2200_INT1 0x7F // Interrupt Status Register 1 (Self-Clearing) page 34
#define INT1EN 0x7D // Interrupt Enable Register 1 page 36
#define INT1RD 0x7E // Interrupt Status Register 1 (Read-Only) page 35
#define IOPWR 0x70 // Port Input/Output Power page 45
#define MACADDR 0x0A // MAC Address Pointer page 79
#define MACDATAH 0x0B // MAC Data Register High Byte page 79
#define MACDATAL 0x0C // MAC Data Register Low Byte page 79
#define MACRW 0x0D // MAC Read/Write Initiate page 79
#define OSCPWR 0x7C // Oscillator Power page 46
#define PHYCF 0x79 // Physical Layer Configuration page 92
#define PHYCN 0x78 // Physical Layer Control page 91
#define PHYSTA 0x80 // Physical Layer Status page 93
#define RAMADDRH 0x08 // RAM Address Pointer High Byte page 24
#define RAMADDRL 0x09 // RAM Address Pointer Low Byte page 24
#define RAMRXDATA 0x02 // RXFIFO RAM Data Register page 24
#define RAMTXDATA 0x04 // TXBUFF RAM Data Register page 24
#define RSTEN 0x72 // Reset Enable Register page 42
#define RSTSTA 0x73 // Reset Source Status Register page 41
#define RXAUTORD 0x01 // RXFIFO AutoRead w/ increment page 62
#define RXCN 0x11 // Receive Control page 61
#define RXFIFOHEADH 0x17 // Receive Buffer Head Pointer High Byte page 71
#define RXFIFOHEADL 0x18 // Receive Buffer Head Pointer Low Byte page 71
#define RXFIFOSTA  0x5B // Receive Buffer Status page 72
#define RXFIFOTAILH 0x15 // Receive Buffer Tail Pointer High Byte page 71
#define RXFIFOTAILL 0x16 // Receive Buffer Tail Pointer Low Byte page 71
#define RXFILT 0x10 // Receive Filter Configuration
#define RXHASHH 0x0E // Receive Hash Table High Byte page 62
#define RXHASHL 0x0F // Receive Hash Table Low Byte page 63
#define RXSTA 0x12 // Receive Status page 61
#define SWRST 0x75 // Software Reset Register page 40
#define TLB0ADDRH 0x27 // TLB0 Address High Byte page 70
#define TLB0ADDRL 0x28 // TLB0 Address Low Byte page 70
#define TLB0INFOH 0x23 // TLB0 Information High Byte page 68
#define TLB0INFOL 0x24 // TLB0 Information Low Byte page 69
#define TLB0LENH 0x25 // TLB0 Length High Byte page 69
#define TLB0LENL 0x26 // TLB0 Length Low Byte page 70
#define TLB1ADDRH 0x2D // TLB1 Address High Byte page 70
#define TLB1ADDRL 0x2E // TLB1 Address Low Byte page 70
#define TLB1INFOH 0x29 // TLB1 Information High Byte page 68
#define TLB1INFOL 0x2A // TLB1 Information Low Byte page 69
#define TLB1LENH 0x2b // TLB1 Length High Byte page 69
#define TLB1LENL 0x2C // TLB1 Length Low Byte page 70
#define TLB2ADDRH 0x33 // TLB2 Address High Byte page 70
#define TLB2ADDRL 0x34 // TLB2 Address Low Byte page 70
#define TLB2INFOH 0x2F // TLB2 Information High Byte page 68
#define TLB2INFOL 0x30 // TLB2 Information Low Byte page 69
#define TLB2LENH 0x31 // TLB2 Length High Byte page 69
#define TLB2LENL 0x32 // TLB2 Length Low Byte page 70
#define TLB3ADDRH 0x39 // TLB3 Address High Byte page 70
#define TLB3ADDRL 0x3A // TLB3 Address Low Byte page 70
#define TLB3INFOH 0x35 // TLB3 Information High Byte page 68
#define TLB3INFOL 0x36 // TLB3 Information Low Byte page 69
#define TLB3LENH 0x37 // TLB3 Length High Byte page 69
#define TLB3LENL 0x38 // TLB3 Length Low Byte page 70
#define TLB4ADDRH 0x3F // TLB4 Address High Byte
#define TLB4ADDRL 0x40 // TLB4 Address Low Byte page 70
#define TLB4INFOH 0x3B // TLB4 Information High Byte page 68
#define TLB4INFOL 0x3C // TLB4 Information Low Byte page 69
#define TLB4LENH 0x3D // TLB4 Length High Byte page 69
#define TLB4LENL 0x3E // TLB4 Length Low Byte page 70
#define TLB5ADDRH 0x45 // TLB5 Address High Byte page 70
#define TLB5ADDRL 0x46 // TLB5 Address Low Byte page 70
#define TLB5INFOH 0x41 // TLB5 Information High Byte page 68
#define TLB5INFOL 0x42 // TLB5 Information Low Byte page 69
#define TLB5LENH 0x43 // TLB5 Length High Byte page 69
#define TLB5LENL 0x44 // TLB5 Length Low Byte page 70
#define TLB6ADDRH 0x4B // TLB6 Address High Byte page 70
#define TLB6ADDRL 0x4C // TLB6 Address Low Byte page 70
#define TLB6INFOH 0x47 // TLB6 Information High Byte page 68
#define TLB6INFOL 0x48 // TLB6 Information Low Byte page 69
#define TLB6LENH 0x49 // TLB6 Length High Byte page 69
#define TLB6LENL 0x4A // TLB6 Length Low Byte page 70
#define TLB7ADDRH 0x51 // TLB7 Address High Byte page 70
#define TLB7ADDRL 0x52 // TLB7 Address Low Byte page 70
#define TLB7INFOH 0x4D // TLB7 Information High Byte page 68
#define TLB7INFOL 0x4E // TLB7 Information Low Byte page 69
#define TLB7LENH 0x4F // TLB7 Length High Byte page 69
#define TLB7LENL 0x50 // TLB7 Length Low Byte page 70
#define TLBVALID 0x1C // TLB Valid Indicators page 68
#define TXAUTOWR 0x03 // Transmit Data AutoWrite page 53
#define TXBUSY 0x54 // Transmit Busy Indicator page 51
#define TXCN 0x53 // Transmit Control page 51
#define TXENDH 0x57 // Transmit Data Ending Address High Byte page 53
#define TXENDL 0x58 // Transmit Data Ending Address Low Byte
#define TXPAUSEH 0x55 // Transmit Pause High Byte page 52
#define TXPAUSEL 0x56 // Transmit Pause Low Byte page 52
#define TXPWR 0x7A // Transmitter Power page 46
#define TXSTA0 0x62 // Transmit Status Vector 0 page 57
#define TXSTA1 0x61 // Transmit Status Vector 1 page 56
#define TXSTA2 0x60 // Transmit Status Vector 2 page 56
#define TXSTA3 0x5F // Transmit Status Vector 3 page 55
#define TXSTA4 0x5E // Transmit Status Vector 4 page 55
#define TXSTA5 0x5D // Transmit Status Vector 5 page 54
#define TXSTA6 0x5C // Transmit Status Vector 6 page 54
#define TXSTARTH 0x59 // Transmit Data Starting Address High Byte page 52
#define TXSTARTL 0x5A // Transmit Data Starting Address Low Byte page 52
#define VDMCN 0x13 // VDD Monitor Control Register page 39

// Indirect MAC registers
#define MACCN  0x00 // MAC Control. Used to enable reception and other options.
#define MACCF 0x01 // MAC Configuration. Used to configure padding options and other settings.
#define IPGT 0x02 // Back-to-Back Interpacket Delay. Sets the Back-to-Back Interpacket Delay.
#define IPGR 0x03 // Non-Back-to-Back Interpacket Delay. Sets the Non-Back-to-Back Interpacket Delay.
#define CWMAXR 0x04 // Collision Window and Maximum Retransmit. Sets the collision window size and the maximum number of retransmits allowed.
#define MAXLEN 0x05 // Maximum Frame Length. Sets the maximum receive frame length.
#define MACAD0 0x10 // MAC Address 
#define MACAD1 0x11 // MAC Address 
#define MACAD2 0x12 // MAC Address 


