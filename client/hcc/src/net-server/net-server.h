#ifndef NET_SERVER_H_
#define NET_SERVER_H_

#include "../hcc.h"

#define GET "GET "
#define PUT "PUT "

uint16_t pinGet(uint8_t *buf, uint16_t dat_p, uint16_t plen);
uint16_t rootGet(uint8_t *buf, uint16_t dat_p, uint16_t plen);
uint16_t pinGetDescription(uint8_t *buf, uint8_t pinId);


typedef struct s_resource {
  char *method;
  char *query;
  uint16_t (*func)(uint8_t *buf, uint16_t dat_p, uint16_t plen);
} t_resource;


uint16_t handleWebRequest(uint8_t *buf, uint16_t dataPointer);

#endif /* NET_SERVER_H_ */
