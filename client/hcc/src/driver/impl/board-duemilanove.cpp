#include "../../hcc.h"

extern __FlashStringHelper *criticalProblem;

byte checkBoardRequirement() {
    if (sizeof(defaultConfig) > 1000) {
        criticalProblem = F("Not enough eeprom to store configuration on this board");
        return 1;
    }
    return 0;
}
