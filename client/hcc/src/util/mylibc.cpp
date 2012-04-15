#include <stdio.h>
#include <string.h>
#include "../hcc.h"

int my_strpos(const char *s, int ch) {
    for (int i = 0; s[i]; i++) {
        DEBUG_PRINT(s[i]);
        if (s[i] == ch) {
            DEBUG_PRINTLN(" ");
            return i;
        }
    }
    return -1;
}


char *my_strstr(char *s, char *find) {
    char c, sc;
    int len;

    if ((c = *find++) != 0) {
        len = strlen(find);
        do {
            do {
                if ((sc = *s++) == 0)
                    return 0;
            } while (sc != c);
        } while (strncmp(s, find, len) != 0);
        s--;
    }
    return (char *) s;
}
