Butterfly NET I/O Firmware
Stefan Frings
www.stefanfrings.de


---------
License:
---------

This firmware comes without any warranty. It's free for commercial and
non-comercial use. Resellers have to include a note about the original
author and his homepage in the documentation.


-------------
Description:
-------------

This firmware turns any AVR microcontroller and a Silabs CP2201 ethernet 
controller into a general purpose I/O interface that may be used instead
of a classic parallel port. 

The free I/O pins can be controlled through simple plain-text commands
on serial interface, IP-socket or HTTP URL.

All AVR's with at least 64KB program memory and 4KB RAM are supported. A
clock frequency of 14.7456 Mhz is recommended.

With a 16 or 20 Mhz chrystal, the maximum serial bitrate is 115200.
A 14.7456 Mhz chrystal allows higher bitrates of 230400, 460800 and
921600 baud.


Compatible modules are sold by www.chip45.com, for example:

The Crumb644-NET v1.0 provides 15 digital I/O pins and 4 analog input pins,
or 18 digital I/O pins if dont use the analog inputs.

The Crumb644-NET v2.1 (which is coming soon) provides 16 digital I/O pins, 
and an USB interface or 18 digital I/O pins if you don't use USB.


-----------
Compiling:
-----------

To compile and install the firmware, you need an ISP programmer hardware 
and some software:

Unix users install: avr-gcc, avr-libc, gnu make, avrdude and perl.

Windows users install :
- WinAVR or Avr Studio 5
- and Strawberry Perl or Active Perl

Note: The AVR Toolchain version 3.3.0 is buggy, therefore you must either use a newer version or WinAVR.

Make that the PATH environment varibale contains the directory of the
"make" command:

C:\Program Files\Atmel\AVR Studio 5.0\AVR Toolchain\bin
or C:\Program Files\WinAvr\bin

Also the "perl" command must be in the PATH, which should have been
set-up automatically during installation of Perl.

Select the correct hardware layout in src/Makefile. If you create your own
custom harware layout, you also need to edit the file src/hw-layout.h.

Then run "make clean code" to compile the source.
Then run "make fuses" to burn the fuse bits of the AVR chip.
Then run "make program" to burn the program into the AVR chip.

Instead of "make program", which uses avrdude, you can use any graphical
programmer, for example PonyProg. Dont forget to burn the fuses as defined
in src/Makefile.


------------------
IP configuration:
------------------

After flashing the firmware, the default configuration is:

DHCP:       enabled
IP-Address: 192.168.0.123
Netmask:    255.255.255.0
Gateway:    192.168.0.1

If DHCP fails, the device falls back to static ip-address after 1-2 minutes.
So if there is no DHCP server, you need to wait 2 minutes before you
can access the device and configure a static ip-address.

An alternative way to configure the ip-address is the following:

- Power on the module and wait until the DHCP client succeedes or times out.
- On a computer, which is physically located in the same network as the 
  module, enter the command "arp -s 192.168.1.13 00:0b:3c:06:3c:45",
  where you need to know and use the MAC address of your own module and the
  ip-address that you want to assign.
- Run the command "ping 192.168.1.13", the modules answers immediately and 
  changes his own IP address temporarily to the new one.

If you think that this feature introduces a security whole, you can disable
the setting UIP_PING_ADDR_CHANGE in uip/uipconf.h and recompile the firmware.

To store the new IP-address permanently, you need to open the configuration
website, and then click on the "submit" button.

You can reset the IP configuration to the defaults by the following procedure:

- Power off the device
- Put a jumper on the middle of the ISP connector (pin 3+4)
- Power on the device
- Remove the jumper


-----------------------------------------
Hardware notes for Crumb644-NET modules:
-----------------------------------------

Cut the interrupt line as shown in the photo "cut_this_wire.jpg". 
The photo was taken from harware version 1.0, but it is the same also in 
version 2.0 and 2.1.

The ethernet controller can be clocked from the AVR pin XTAL2 through a 100nF
capaciator if the AVR is clocked by a 20Mhz chrystal. Please note that the
schematic of Crumb644-NET v1.0 has an error here, but the shipped hardware
is correct.


--------------
Known issues:
--------------

If you use telnet, the first command always fails with error
message "Bad command" because telnet sends some invisible characters to the 
module during startup, that the module does not understand. As a better
alternative, you might use the netcat program (nc command). 

You might notice sporadically delays of about 1 second when loading websites.
The delay occurs when your computer uses the "nagle" algorithm, which is
not supported by the module. You would need to hack the operating system
or web browswer to disable nagle, so better don't try it.

Internet Explorer 9 reads the response much slower than all other browsers.
I did not yet investigate that issue.

DHCP fails on some few Internet Routers, for example on the Vodafone EasyBox
803. Until somebody finds a solution use static IP settings.

This effect does not occur when you execute i/o commands via
HTTP or socket connection. Only the websites are affected.


-----------------
Troubleshooting:
-----------------

If you encounter unstable or non working physical ethernet connection,
disable auto-negotiation and set the bitrate to 10Mbit/s full-duplex
on the ethernet switch (or computer in case of a direct connection).

If the link LED does not go on, try a different switch.

The on-board status LED flashes two times immediately after power-on to
indicate that the AVR controller is living.

If your ethernet link LED goes off after a while unexpectedly on a
Crumb644-NET v1.0, then you might have a broken hardware. I received
several modules of version 1.0 that fail immediately when exposed to
the hot airflow of a hair dryer. Modules of version 2.0 are obviously
not affected.


--------------------------
How to use the webserver:
--------------------------

The webserver displays files that are stored in the 64K program memory. 
It's main purpose is to configure the device and to provide some status 
information for debugging.

Just open any web browser and enter the URL "http://ip-address/".

You can execute I/O commands (from io-commands.c) by appending them
to the URL "http://ip-address/io?", for example:

Send:   http://ip-address/io?iPB3 
Answer: PB3=FF

I/O commands are described in a chapter below. Please note that the web 
server is slower, than the serial or socket connection, because of the
HTTP protocol overhead.


--------------------------
How to use the IP socket:
--------------------------

Connect to the device using it's IP-Address and port 23, for example
using the telnet program. This is a simple plain-text socket.
The firmware supports up to 4 connections simultaneously.

You can execute I/O commands (from io-commands.c) by simply sending 
them on the socket connection. Example:

Send:   iPB3   (followed by a line-feed character)
Answer: PB3=FF

I/O commands are descibed in a chapter below.


------------------------------------
How to use serial (USB) connection:
------------------------------------

If you enable the SERIAL feature in src/Makefile, you can also connect
via serial port, which might be connected to an USB to serial adapter.

Connect the module to the serial port and run a terminal program 
with 115200 baud, 8 bits, no parity, 1 stop bit and no handshake. 
Commands must be terminated by line-feed characters.

Different to the socket connection, the serial connection sends an echo 
of each command back to the computer before executing it. This has been
done for convenience and it has only minor impact to the performance. 
You can disable the echo in src/seriald/seriald.h.

You can execute I/O commands (from io-commands.c) by simply sending 
them on the serial connection. Example:

Send:   iPB3   (followed by a line-feed character)
Answer: PB3=FF

I/O commands are descibed in a chapter below.


----------------------------------
How to use the email application:
----------------------------------

Open the eMail Application website and enter the data for the SMTP server 
that shall be used to send emails. If this server does not need authentication, 
then the username and password fields are ignored.

For each port D pin that you want to monitor, enter a message text.

The application will send this text in the subject field of the email, when
the related pin changes from low to high.


-------------------------------
How to use the authentication:
-------------------------------

In the IP configuration form, enter a username and a password in the form:

username:password

When you next try to access the module vie HTTP, you will be prompted for
authentication using HTTP BASIC authentication method.

In addition, the socket connection will also be protected. If you enter any
command, you will receive the answer "auth required" until you authenticate
using the command:

auth username:password

If you forgot your username or password, you can reset the configuration
with a jumper as describe above.


----------------------
List of I/O commands:
----------------------

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



-------------------
Programmers notes:
-------------------

This firmware is based on the uIP stack of Adam Dunkels, which was specially
written for microcontrollers with low RAM memory. 

The current firmware leaves about 10 kB free program and 470 bytes free RAM
memory if all features are enabled. Be aware that some RAM must be kept free 
for dynamic allocation (I assume that about 50 bytes are required).

My firmware stores all settings in the internal flash memory of the ethernet
controller. The EEPROM of the AVR chip is not used.

You can add new custom i/o commands to the file io-commands.c. Be aware
that these functions always must return a string of 0-100 characters length.

If you change anything in the Makefile, dont forget to run "make clean". 

If you plan to write your own web applications, then I highly recommend to
read the manual of uIP and study the source code of socketd.c until you 
understand how to use protothreads, protosockets and what limitations 
they introduce to local (stack-, and register-) variables.

You can reduce the RAM usage a little:

- Reduce the application buffer size UIP_BUFSIZE, but not less than 500 bytes.
  If you make it too small, the statistics page will not work anymore. If you
  make it even smaller, other functions will also fail.

- Disable UIP_CONF_STATISTICS will save the space needed for the statistics 
  counters.

- Disable udp protocol via UIP_CONF_UDP will also disable the DHCP client.

- Removing all floating point arithmetic (in the source files and also a
  setting in the Makefile) will safe a lot memory: 4KB ROM and 300 bytes RAM!
 
You need to keep at least 50 bytes RAM free for proper operation.


-------------------------
How the webserver works:
-------------------------

The io.shtml website demonstrates several features of the webserver:

- The include tag at the beginning shows how to include another file.

- The Port status shows how to call functions to create dynamic content.

- The form demonstrates how to call functions and display their result 
  via JavaScript.

If a function name matches a file name, then the function will be executed
first and then the file content will be returned. This feature is used in
the configuration page, that stores settings from the HTML form and then
displays the same page again.

You can add your own web pages to provide custom features, such as
switching outputs via a web form. The "I/O Status" website demonstrates the 
basics.

The web files are stored in the directory src/httpd-files. During compilation,
a perl script generates a C source file with byte arrays from the content of
these files.

Files that contain dynamic content must be called *.shtml. These files may 
include the follwing scripting tags:

%!: /header.html  Include the content of another file here. The included file
                  cannot contain scripting tags.

%! portstatus?B   Call a function and place its output string here. In this 
                  case, the function name is "portstatus" and the parameter
                  to this function is "B".

The source code of these functions is in src/httpd/httpd_functions.c. One
specia function is the "io" function, that executes i/o commands from
io-commands.c.

Functions can also be called via URL, for examples:

http://ip-address/portstatus?B
http://ip-address/io?oPB3,0

