#include <stdio.h>
#include <string.h>
#include <avr/pgmspace.h>

int my_strpos(const char *s, int ch) {
    for (int i = 0; s[i]; i++) {
        if (s[i] == ch) {
            return i;
        }
    }
    return -1;
}

int strstrpos_P(const char *str, const prog_char *wanted) {
    const size_t len = strlen_P(wanted);
    size_t pos = 0;
    if (len == 0)
        return pos;
    while (str[pos] != pgm_read_byte(wanted) || strncmp_P(&str[pos], wanted, len)) {
        if (str[pos++] == '\0') {
            return -1;
        }
    }
    return pos;
}
