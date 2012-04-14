#include "../../hcc.h"

byte checkBoardRequirement() {
    if (sizeof(defaultConfig) > 1000) {
        criticalProblem_p = PSTR("Not enough eeprom to store configuration on this board");
        return 1;
    }
    return 0;
}
