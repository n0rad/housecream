#include "hcc.h"

int main(void) {
    hccInit();

    hccSetup();

    for (;;) {
        hccLoop();
    }

    return 0;
}
