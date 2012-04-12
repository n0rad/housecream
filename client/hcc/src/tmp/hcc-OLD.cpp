
char globalErrorBuffer[100] = {0};




////////////////////////////////////////////////////////////////////////////////////////
// This is the configuration part
////////////////////////////////////////////////////////////////////////////////////////

// DEBUG on if defined
//#define DEBUG

static uint8_t   mac[6]         = {0x54, 0x55, 0x58, 0x10, 0x00, 0xF4};
static uint8_t   ip[4]          = {192, 168, 42, 245};
static uint16_t  port           = 80;
static char      description[]  = "control everything on window1";
static char      name[]  = "window1 controller";
static char      notifyUrl[]  = "";
static char      technicalDescription[]  = "indoor management of the window only, not powered from POE";

static uint16_t p_pinSize = 20;

/**
 * DIGITAL pin as input and value 1 will enable 20k pullup
 */
t_pin	p_pin[] = {
// DIGITAL
/*  0 */ {INPUT, HIGH, "TEST1"},
/*  1 */ {INPUT, LOW, "TEST2"},
/*  2 */ {OUTPUT, HIGH, "TEST3"},
/*  3 */ {PWM, 255, "TEST4"}, // PWM
/*  4 */ {NOTUSED, 0, 0},
/*  5 */ {OUTPUT, HIGH, "TEST5"}, // PWM
/*  6 */ {PWM, 255, "TEST6"}, // PWM
/*  7 */ {OUTPUT, HIGH, "SIMPLE LED"},
/*  8 */ {OUTPUT, HIGH, "TEST7"},
/*  9 */ {OUTPUT, LOW, "TEST9"}, // PWM
/* 10 */ {NOTUSED, 0, 0}, // Used by ethernet  // PWM
/* 11 */ {NOTUSED, 0, 0}, // Used by ethernet  // PWM
/* 12 */ {NOTUSED, 0, 0}, // Used by ethernet
/* 13 */ {NOTUSED, 0, 0}, // Used by ethernet
// ANALOG
/* 14 */ {ANALOG, 0, "temp receptor 1"}, // A0
/* 15 */ {DIGITAL, 0, "temp receptor 2"}, // A1
/* 16 */ {NOTUSED, 0, "temp receptor 3"}, // A2
/* 17 */ {ANALOG, 0, "temp receptor 4"}, // A3
/* 18 */ {DIGITAL, 0, "temp receptor 5"}, // A4
/* 19 */ {ANALOG, 0, "temp receptor 6"}  // A5
};

////////////////////////////////////////////////////////////////////////////////////////


#define HCC_VERSION "0.1"
#define HARDWARE "Arduino Duemilanove / Nuelectronics enc28j60 Ethernet Shield V1.1"


typedef struct s_resource {
  int id;
  char *method;
  char *query;
  uint16_t (*func)(uint8_t *buf, uint16_t dat_p, uint16_t plen);
} t_resource;




