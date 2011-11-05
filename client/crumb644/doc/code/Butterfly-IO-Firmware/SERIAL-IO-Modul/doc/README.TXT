Butterfly USB I/O Firmware
Stefan Frings
www.stefanfrings.de

--------
License:
--------

This firmware comes without any warranty. It's free for commercial and
non-comercial use. Resellers have to include a note about the original
author and his homepage in the documentation.

------------
Description:
------------

This firmware turns AVR microcontroller with serial interface into
a general purpose I/O interface that may be used instead of a classic 
parallel port.

The microcontroller is connected to the computer via serial interface 
(or USB adapter). This firmware provides access to the I/O pins of the
AVR microcontroller through plain text commands.

Every AVR controller with an UART (serial port) and at least 8kB program
memory can be used with this firmware.

For regular serial bitrate up to 2400-115200 baud, a clock frequency of
1.8432, 3.6864, 7.3728, 11.0592 or 14.7456 Mhz is recommended.

For serial bitrates of 230400, 460800 and 921600 baud, a clock
frequency of 14.7456 Mhz is recommended.

Compatible modules are sold by www.chip45.com, for example:

Crumb8-USB
Crumb128
Crumb168-USB
Crumb644
Crumb2560


------------
Compiling:
------------

To compile and install the firmware, you need an ISP programmer hardware 
and some software:

Unix users install: avr-gcc, avr-libc, gnu make and avrdude.

Windows users install WinAVR or Avr Studio 5. Make that the PATH 
environment varibale contains the directory of the "make" command:

C:\Program Files\Atmel\AVR Studio 5.0\AVR Toolchain\bin
or C:\Program Files\WinAvr\bin

Modify src/Makefile so that it matches your hardware.

Then run "make clean code" to compile the source.
Then run "make fuses" to burn the fuse bits of the AVR chip.
Then run "make program" to burn the program into the AVR chip.

Instead of "make program", which uses avrdude, you can use any graphical
program, for example PonyProg. Dont forget to burn the fuses correctly
for the chrystal oscillator.


-----------------------
How to use the device:
-----------------------

Connect the module to the serial or USB port and run a terminal program 
with 115200 baud, 8 bits, no parity, 1 stop bit and
no handshake. Commands must be terminated by line-feed characters.

You can change the bitrate in src/Makefile but that is not recommended. 
Lower bitrates provide less performance. Higher bitrates will only work
fine when you use a 14,7456 Mhz chrystal.

Enter "help" to get the list of commands.


----------------------
List of I/O commands:
----------------------

Enter "help" to see the list of commands:

Commands that affect all port pins:
d,DDCCBBAA  Set direction of all port pins, high=output, low=input
p,DDCCBBAA  Configure pull-up resistors, high=on, low=off
o,DDCCBBAA  Output data on all port pins
i           Read from all port pins

Commands that affect one port:
dPB,xx      Set direction of port B
pPB,xx      Configure pull-up resistors of port B
oPB,xx      Output data on port B
iPB         Read from port B

Commands that affect a single port pin:
dPB0,1      Set direction of port B pin 0 to input (0 or i=input, 1 or o=output)
pPB0,1      Enable pull-up resistors of port B pin 0 (0 or d=disabled, 1 or e=enabled)
oPB0,1      Output high on port B pin 0 (0 or l=low, 1 or h=high)
iPB0        Read from port B pin 0

Analog inputs:
rVCC        Use VCC as reference (5V)
rINT        Use internal 1,1V reference
rEXT        Use external reference
a3          Read from analog pin 3

xx and DDCCBBAA are hexadecimal numbers.
On modules with more than 4 ports, you can also use 64 bit numbers
in format HHGGFFEEDDCCBBAA.

Binary and hexadecimal digits can be given in upper and lower case.
Instead of "," you can also enter "=".;

