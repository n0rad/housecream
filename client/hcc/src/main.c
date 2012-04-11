#include "hcc.h"

int main(void) {
    hccInit();

    setup();

    for (;;) {
        loop();
    }

    return 0;
}
