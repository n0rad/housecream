###############################################################################
# Makefile for the project crumb644-net
###############################################################################

## General Flags
PROJECT = crumb644-net
MCU = atmega644
TARGET = crumb644-net.elf
CC = avr-gcc.exe

## Options common to compile, link and assembly rules
COMMON = -mmcu=$(MCU)

## Compile options common for all C compilation units.
CFLAGS = $(COMMON)
CFLAGS += -Wall -gdwarf-2 -std=gnu99                    -DF_CPU=200000000UL -Os -funsigned-char
CFLAGS += -MD -MP -MT $(*F).o -MF dep/$(@F).d 

## Assembly specific flags
ASMFLAGS = $(COMMON)
ASMFLAGS += $(CFLAGS)
ASMFLAGS += -x assembler-with-cpp -Wa,-gdwarf2

## Linker flags
LDFLAGS = $(COMMON)
LDFLAGS +=  -Wl,-Map=crumb644-net.map


## Intel Hex file production flags
HEX_FLASH_FLAGS = -R .eeprom

HEX_EEPROM_FLAGS = -j .eeprom
HEX_EEPROM_FLAGS += --set-section-flags=.eeprom="alloc,load"
HEX_EEPROM_FLAGS += --change-section-lma .eeprom=0 --no-change-warnings


## Objects that must be built in order to link
OBJECTS = main.o CP2200.o httpd.o httpd-cgi.o httpd-fs.o http-strings.o psock.o timer.o uip.o uip_arp.o uiplib.o uip-neighbor.o 

## Objects explicitly added by the user
LINKONLYOBJECTS = 

## Build
all: $(TARGET) crumb644-net.hex crumb644-net.eep crumb644-net.lss size

## Compile
main.o: ../main.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

CP2200.o: ../CP2200.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

httpd.o: ../httpd.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

httpd-cgi.o: ../httpd-cgi.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

httpd-fs.o: ../httpd-fs.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

http-strings.o: ../http-strings.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

psock.o: ../psock.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

timer.o: ../timer.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

uip.o: ../uip.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

uip_arp.o: ../uip_arp.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

uiplib.o: ../uiplib.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

uip-neighbor.o: ../uip-neighbor.c
	$(CC) $(INCLUDES) $(CFLAGS) -c  $<

##Link
$(TARGET): $(OBJECTS)
	 $(CC) $(LDFLAGS) $(OBJECTS) $(LINKONLYOBJECTS) $(LIBDIRS) $(LIBS) -o $(TARGET)

%.hex: $(TARGET)
	avr-objcopy -O ihex $(HEX_FLASH_FLAGS)  $< $@

%.eep: $(TARGET)
	-avr-objcopy $(HEX_EEPROM_FLAGS) -O ihex $< $@ || exit 0

%.lss: $(TARGET)
	avr-objdump -h -S $< > $@

size: ${TARGET}
	@echo
	@avr-size -C --mcu=${MCU} ${TARGET}

## Clean target
.PHONY: clean
clean:
	-rm -rf $(OBJECTS) crumb644-net.elf dep/* crumb644-net.hex crumb644-net.eep crumb644-net.lss crumb644-net.map


## Other dependencies
-include $(shell mkdir dep 2>/dev/null) $(wildcard dep/*)

