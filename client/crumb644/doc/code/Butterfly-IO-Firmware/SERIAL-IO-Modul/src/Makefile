# Makefile for this AVR project

# make          Compiles the source code into hex files.
# make fuses    Program fuses
# make program  Program flash and eeprom

# make list     Create generated code listing
# make clean    Delete all generated files


# Programmer hardware settings for avrdude
# Linux:   /dev/ttyUSB0 is the first virtual serial port 
# Windows: //./COM20    is the virtual port COM20
AVRDUDE_HW = -c stk500v2 -P /dev/ttyUSB1 -b 115200

# Select one of the predefined hardware layouts.
# The hardware layouts are defined below.
#LAYOUT = Crumb8_USB
#LAYOUT = Crumb128
#LAYOUT = Crumb168_USB
LAYOUT = Crumb644
#LAYOUT = Crumb2560

# Clock frequency in Hz
F_CPU = 20000000
#F_CPU = 14745600

# Serial bitrate
# Bitrates above 115200 require a 14745600 Mhz chrystal.
BAUD = 115200

######################################################################
# If you add your own code or hardware layout, then you should take
# a look at the settings below this marker.
# If you just want to use the firmware as it is, you dont need to
# change any settings below.
#######################################################################

# Source files, separated by space.
SRC = driver/serialconsole.c driver/ADC.c io-commands.c main.c

ifeq ($(LAYOUT),Crumb8_USB)
  MCU = atmega8
  LFUSE = 0xF7
  HFUSE = 0x9
  # See additional settings in hw-layout.h !
endif

ifeq ($(LAYOUT),Crumb128)
  MCU = atmega128
  LFUSE = 0xFF
  HFUSE = 0xC9
  # See additional settings in hw-layout.h !
endif

ifeq ($(LAYOUT),Crumb168_USB)
  MCU = atmega168
  LFUSE = 0xF7
  HFUSE = 0xD9
  # See additional settings in hw-layout.h !
endif

ifeq ($(LAYOUT),Crumb644)
  MCU = atmega644p
  LFUSE = 0xF7
  HFUSE = 0xD9
  # See additional settings in hw-layout.h !
endif

ifeq ($(LAYOUT),Crumb2560)
  MCU = atmega2560
  LFUSE = 0xF7
  HFUSE = 0xD9
  # See additional settings in hw-layout.h !
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

# Name of the program without extension
PRG = SERIAL-IO-Modul

# Do we need to write Eeprom? (yes/no)
EEPROM = no

# Libraries
#LIBS = -L path/to/libraries -llibrary1 -llibrary2

# Includes
#INCLUDES = -Ipath/to/include/files

# Compiler options for all c source files
CFLAGS = -std=c99 -Wall -Os -mmcu=$(MCU) -DF_CPU=$(F_CPU) $(INCLUDES) -DBAUD=$(BAUD) -D$(LAYOUT)

# Linker options 
LDFLAGS = -Wl,-Map,$(PRG).map


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
USB-IO-Modul: $(PRG).hex

# Eeprom content
eeprom: $(PRG)_eeprom.hex

# Generated code listing
list: $(PRG).lst

# Remove all generated files
clean:
	rm -rf *.o driver/*.o $(PRG).hex $(PRG).elf $(PRG).lst $(PRG).map $(PRG)_eeprom.hex

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
