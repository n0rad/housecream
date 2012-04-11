#ifndef EEPROM_CONFIG_H
#define EEPROM_CONFIG_H

void configSave(void);
void configLoad(void);



#include <stdint.h>

#define CONFIG_VERSION "hcc1"
#define CONFIG_EEPROM_START 0

/** description stay in program memory and cannot change */
struct s_boardDescription {
    uint8_t mac[6];
	char technicaldesc[101];
} t_boardDescription;

/** infos are stored in eeprom and can change  */
struct s_boardInfo {
	uint8_t ip[4];
	uint16_t port;
	char name[21];
	char notifyurl[51];
} t_boardInfo;

///////////////////////////////////////////////////////////////
// PIN
///////////////////////////////////////////////////////////////

#define PIN_INPUT 1
#define PIN_OUTPUT 2
#define PIN_NOTUSED 3
#define PIN_RESERVED 4

#define PIN_ANALOG 1
#define PIN_DIGITAL 2

#define PIN_NOTIFY_OVER_EQ 1
#define PIN_NOTIFY_UNDER_EQ 2

#define NOT_SET 0

typedef struct s_notify { // 3 Bytes
	char condition;
	uint16_t value;
} t_notify;

/** description stay in program memory and cannot change */
typedef struct s_pinDescription {
	char direction;				// PIN_INPUT, PIN_OUTPUT, PIN_NOTUSED, PIN_RESERVED
	char type;					// PIN_ANALOG, PIN_DIGITAL
//	float (*convertPinValue)(uint16_t pinValue); //
//	uint16_t (*accessPin)(char type, int pinId);
	char technicalDesc[101];
} t_pinDescription;

/** infos are stored in eeprom and can change  */
struct s_pinInfo { // 35 Bytes
	uint16_t lastValue; // output only
	char name[21];
	t_notify notifies[4];
} t_pinInfo;

#include "../config.h"

#endif
