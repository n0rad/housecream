#ifndef EEPROM_CONFIG_H
#define EEPROM_CONFIG_H

#include <avr/pgmspace.h>
#include "../config.h"
#include <stdint.h>

void configLoad(void);
void getConfigIP(uint8_t ip[4]);
void getConfigMac(uint8_t ip[6]);
uint16_t getConfigPort();

#define CONFIG_VERSION "hcc1"
#define CONFIG_EEPROM_START 0

/** description stay in program memory and cannot change */
typedef struct s_boardDescription {
    uint8_t mac[6];
    uint8_t ip[4];
    uint16_t port;
    char name[21];
    char description[101];
    char technicalDescription[101];
    char notifyurl[51];
} t_boardDescription;

///////////////////////////////////////////////////////////////
// PIN
///////////////////////////////////////////////////////////////

#define PIN_INPUT 0
#define PIN_OUTPUT 1
#define PIN_NOTUSED 2
#define PIN_RESERVED 3
extern const char *pinDirection[];


#define PIN_ANALOG 1
#define PIN_DIGITAL 2
extern const char *pinType[];

#define PIN_NOTIFY_OVER_EQ 1
#define PIN_NOTIFY_UNDER_EQ 2

#define NOT_SET 0




typedef struct s_notify { // 3 Bytes
    char condition;
    uint16_t value;
} t_notify;

/** description stay in program memory and cannot change */
typedef struct s_pinDescription {
    char direction;             // PIN_INPUT, PIN_OUTPUT, PIN_NOTUSED, PIN_RESERVED
    char type;                  // PIN_ANALOG, PIN_DIGITAL
//  float (*convertPinValue)(uint16_t pinValue); //
//  uint16_t (*accessPin)(char type, int pinId);
    char technicalDesc[101];
} t_pinDescription;

/** infos are stored in eeprom and can change  */
typedef struct s_pinInfo { // 35 Bytes
    char name[21];
} t_pinInfo;

/** data are stored in eeprom and keep in ram for fast access */
typedef struct s_pinData {
    uint16_t lastValue; // output only
    t_notify notifies[4];
} t_pinData;

///////////////////////////////////////////////////////////////
// STRUCTURE OF EEPROM
///////////////////////////////////////////////////////////////

typedef struct s_config {
   t_pinInfo pinInfos[NUMBER_OF_PINS];
   t_pinData pinData[NUMBER_OF_PINS];
   char version[5];
} t_config;

extern const t_config defaultConfig;
extern const t_boardDescription boardDescription;
extern const t_pinDescription pinDescriptions[NUMBER_OF_PINS];

#endif
