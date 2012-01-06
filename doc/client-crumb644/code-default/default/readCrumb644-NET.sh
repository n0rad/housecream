avrdude -n -c stk500v2 -p m644p -P /dev/serial/by-id/usb-FTDI_FT232R_USB_UART_A7005thp-if00-port0 -U flash:r:defaultflash.hex:r
avrdude -n -c stk500v2 -p m644p -P /dev/serial/by-id/usb-FTDI_FT232R_USB_UART_A7005thp-if00-port0 -U eeprom:r:defaulteeprom.hex:r
