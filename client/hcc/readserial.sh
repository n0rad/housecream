#!/usr/bin/python
# -*- coding: utf-8 -*-

import serial
import time
import sys

if len(sys.argv) == 3:
	ser = serial.Serial(sys.argv[1], sys.argv[2])
else:
	print "# Please specify a port and a baudrate, e.g. %s /dev/ttyUSB0 115200" % sys.argv[0]
	print "# using defaults /dev/ttyUSB0 9600"
	ser = serial.Serial("/dev/ttyUSB0", 9600)

while 1:
	sys.stdout.write(ser.readline())

