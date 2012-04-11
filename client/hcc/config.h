
#define DEBUG

#define NUMBER_OF_PINS 2

struct s_boardDescription p_boardDescription = {
    {0x54, 0x55, 0x58, 0x10, 0x00, 0xF4},       // mac
    "indoor management of the window only, not powered from POE", // technical description
};

struct s_pinDescription	p_pinDescriptions[NUMBER_OF_PINS] = {
    {PIN_INPUT, PIN_ANALOG, "temperature captor for window1"},
 //   {PIN_INPUT, PIN_ANALOG, "temperature captor for window1"},
};

////////////////////////////////////////////////////
// EEPROM stored in configuration
////////////////////////////////////////////////////
struct s_config {
   struct s_boardInfo boardInfo;
   struct s_pinInfo pinInfos[NUMBER_OF_PINS];
   char version[5];
} config = {
    {
        {192, 168, 42, 245},                        // ip
        80,                                         // port
        "window1 controller",                       // name
        "192.168.42.86:8080/hcs/ws/event",          // notify url
    },
    {
        0, "Windowtemp1", {PIN_NOTIFY_OVER_EQ, 1},
        42, "Windowtemp2", {PIN_NOTIFY_OVER_EQ, 1},
    },
    CONFIG_VERSION
};
