#include <stdio.h>
#include <string.h>

int my_strpos(const char *s, int ch) {
    for (int i = 0; s[i]; i++) {
        if (s[i] == ch) {
            return i;
        }
    }
    return -1;
}
