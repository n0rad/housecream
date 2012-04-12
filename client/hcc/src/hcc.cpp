#include "hcc.h"

void hccInit(void) {
    init(); // load init of arduino

#ifdef DEBUG
    delay(3000);
    Serial.begin(9600);
#endif
    delay(500);
//    DEBUG_PRINT_FULL(F("Starting init"));
    configLoad();

    networkSetup();

    pinMode(9, OUTPUT);
}

void hccSetup(void) {
//    DEBUG_PRINT_FULL(PSTR("Starting setup"));

//    if (checkDef()) {
//        DEBUG_PRINTLN(globalErrorBuffer);
//        return;
//    }
//    // init pins
//    initPins();


}

int count = 0;

void hccLoop(void) {
//    Serial.print("temp: ");
//    Serial.println(count);
//    count++;
//    digitalWrite(9, HIGH);   // set the LED on
//    delay(1000);              // wait for a second
//    digitalWrite(9, LOW);    // set the LED off
//    delay(1000);              // wait for a second

    networkManage();
}
