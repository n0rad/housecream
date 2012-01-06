# Makefile for this AVR project

# make          Compiles the source code into hex files.
# make fuses    Program fuses
# make program  Program flash and eeprom

# make list     Create generated code listing
# make clean    Delete all generated files


#---------------------
# Configuration
#---------------------

# Paramneters for ISP programmer avrdude
# Linux:   /dev/ttyUSB0 is the first virtual serial port 
# Windows: //./COM20    is the virtual port COM20
AVRDUDE_HW = -c stk500v2 -P /dev/ttyUSB1 -b 115200 


# Select one of the predefined hardware layouts.
# The hardware layouts are defined below.
#LAYOUT = Crumb644_NET_v1
LAYOUT = Crumb644_NET_v2


# Enable access through serial (or USB) port
# Needs about 110 bytes RAM.
# Bitrates above 115200 require a 14,745600 Mhz chrystal.
SERIAL = yes
BAUD = 115200

# Enable eMail application, which sends an eMail if
# a PIN on Port PD0 - PD7 changes its status.
# Needs about 30 bytes RAM.
EMAIL_APP = yes

# Enable username/password authentication in HTTP and socket server
# Needs about 410 bytes RAM.
AUTH = yes


######################################################################
# If you add your own code or hardware layout, then you should take
# a look at the settins below this marker.
# If you just want to use the firmware as it is, you dont need to 
# change any settings below.
#######################################################################


# Source files, separated by space.
SRC = driver/CP2200.c driver/clock.c uip/uip.c uip/uip_arp.c uip/psock.c uip/timer.c socketd/socketd.c httpd/httpd-fs.c httpd/httpd-functions.c httpd/httpd.c inetd/inetd.c dhcpc/dhcpc.c io-commands.c main.c


#--------------------
# Hardware layouts
#--------------------

# SRC += driver/MCP3204.c   adds the driver for an external serial ADC chip of type MCP3204 or MCP3208
# SRC += driver/ADC.c       adds the driver for the AVR internal ADC.

ifeq ($(LAYOUT),Crumb644_NET_v1)
  MCU = atmega644
  F_CPU = 20000000
  LFUSE = 0xF7
  HFUSE = 0xD9
  SRC += driver/MCP3204.c
  # See additional settings in hw-layout.h !
endif

ifeq ($(LAYOUT),Crumb644_NET_v2)
  MCU = atmega644p
  F_CPU = 20000000
  LFUSE = 0xF7
  HFUSE = 0xD9
  # See additional settings in hw-layout.h !
endif


#--------------------
# Optional features
#--------------------

CFLAGS = -D $(LAYOUT) -D DEBUG

ifeq ($(SERIAL),yes)
  CFLAGS += -D SERIAL -D BAUD=$(BAUD)
  SRC += seriald/seriald.c
endif

ifeq ($(EMAIL_APP),yes)
  CFLAGS += -D EMAIL_APP
  SRC += smtpc/smtpc.c email_app/email_app.c httpd/base64.c
endif

ifeq ($(AUTH),yes)
  CFLAGS += -D AUTH
  SRC += httpd/base64.c
endif


###################################################################
# You possibly do not need to change settings below this marker
###################################################################

# Binaries to be used
# You may add the path to them if they are not in the PATH variable.
CC      = avr-gcc
OBJCOPY = avr-objcopy
OBJDUMP = avr-objdump
AVRDUDE = avrdude
PERL    = perl


# Name of the program without extension
PRG = hcrumb

# Do we need to write Eeprom? (yes/no)
EEPROM = no

# Libraries
#LIBS = -L path/to/libraries -llibrary1 -llibrary2

# Includes
#INCLUDES = -Ipath/to/include/files


# Compiler options for all c source files
CFLAGS += -Wall -Os -mmcu=$(MCU) -DF_CPU=$(F_CPU) $(INCLUDES) -std=gnu99
CFLAGS += -funsigned-char
CFLAGS += -funsigned-bitfields
CFLAGS += -fpack-struct
CFLAGS += -fshort-enums
CFLAGS += -Wstrict-prototypes
CFLAGS += -Wundef



# Linker options 
LDFLAGS = -Wl,-Map,$(PRG).map

# Enable floating-point support in printf
LDFLAGS += -Wl,-u,vfprintf -lprintf_flt -lm



# Collect fuse operations for avrdude
ifdef FUSE
  FUSES += -U fuse:w:$(FUSE):m
endif
ifdef LFUSE
  FUSES += -U lfuse:w:$(LFUSE):m
endif
ifdef HFUSE
  FUSES += -U hfuse:w:$(HFUSE):m
endif
ifdef EFUSE
  FUSES += -U efuse:w:$(EFUSE):m
endif

# Default sections
ifeq ($(EEPROM),yes)
all: code eeprom
else
all: code
endif

# Program code
code: $(PRG).hex

# Alias for AVR Studio
NET-IO-Modul: $(PRG).hex

# Eeprom content
eeprom: $(PRG)_eeprom.hex

# Generated code listing
list: $(PRG).lst

# Remove all generated files
clean:
	rm -f *.o uip/*.o httpd/*.o driver/*.o dhcpc/*.o inetd/*.o socketd/*.o seriald/*.o $(PRG).hex $(PRG).elf $(PRG).lst $(PRG).map $(PRG)_eeprom.hex httpd/httpd-fsdata.c smtpc/*.o email_app/*.o

# Program flash memory with or without eeprom
ifeq ($(EEPROM),yes)
program: code eeprom
	$(AVRDUDE) -p $(MCU) $(AVRDUDE_HW) -U flash:w:$(PRG).hex:i -U eeprom:w:$(PRG)_eeprom.hex:i 
else
program: code 
	$(AVRDUDE) -p $(MCU) $(AVRDUDE_HW) -U flash:w:$(PRG).hex:i 
endif

# Program fuses
fuses:
	$(AVRDUDE) -p $(MCU) $(AVRDUDE_HW) $(FUSES)

$(PRG).elf: $(SRC:.c=.o)
	$(CC) $(CFLAGS) $(LDFLAGS) -o $@ $^ $(LIBS)

%.lst: %.elf
	$(OBJDUMP) -h -S $< > $@

%.hex: %.elf
	$(OBJCOPY) -j .text -j .data -O ihex $< $@

%_eeprom.hex: %.elf
	$(OBJCOPY) -j .eeprom --change-section-lma .eeprom=0 -O ihex $< $@ 

httpd/httpd-fs.o: httpd-files/* httpd/httpd-fs.c httpd/httpd-fs.h
	$(PERL) makefsdata
	$(CC) $(CFLAGS) $(LDFLAGS) -c -o $@ httpd/httpd-fs.c $(LIBS)

