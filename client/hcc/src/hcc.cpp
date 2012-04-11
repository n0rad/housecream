#include "hcc.h"

void hccInit(void) {
    init(); // load init of arduino

#ifdef DEBUG
    delay(3000);
    Serial.begin(9600);
#endif
    delay(500);
    DEBUG_PRINT_FULL("Starting init");
    configLoad();

    netSetup();

    pinMode(13, OUTPUT);
}

void hccSetup(void) {
    DEBUG_PRINT_FULL("Starting setup");
}

int count = 0;

void hccLoop(void) {
    Serial.print("temp: ");
    Serial.println(count);
    count++;
    digitalWrite(13, HIGH);   // set the LED on
    delay(1000);              // wait for a second
    digitalWrite(13, LOW);    // set the LED off
    delay(1000);              // wait for a second
}
