#ifndef NET_SERVER_H_
#define NET_SERVER_H_

#include "../hcc.h"

#define GET "GET "
#define PUT "PUT "

uint16_t handleWebRequest(uint8_t *buf, uint16_t dataPointer);

#endif /* NET_SERVER_H_ */
