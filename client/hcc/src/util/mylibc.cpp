#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <avr/pgmspace.h>

#include "mylibc.h"


uint8_t readIP(char *buf, uint16_t len, uint8_t newIp[4]) {
    char num = 0;
    char point = 0;
    for (uint8_t i = 0; i < len; i++) {
        if ((buf[i] < '0' || buf[i] > '9') && buf[i] != '.') {
            return 1;
        }
        num++;
        if (buf[i] == '.') {
            num = 0;
            point++;
        }
        if (num > 3) {
            return 1;
        }
    }
    if (point != 3) {
        return 1;
    }
    newIp[0] = atoi(buf);
    newIp[1] = atoi(buf = &buf[my_strpos(buf, '.') + 1]);
    newIp[2] = atoi(buf = &buf[my_strpos(buf, '.') + 1]);
    newIp[3] = atoi(buf = &buf[my_strpos(buf, '.') + 1]);
    if (newIp[0] == 255 || newIp[1] == 255 || newIp[2] == 255 || newIp[3] == 255 // cannot be 255
            || newIp[0] == 0 || newIp[3] == 0) { // cannot start or finish with 0
        return 1;
    }

    return 0;
}

float floatRelativeDiff(float a, float b) {
    float c = Abs(a);
    float d = Abs(b);
    d = Max(c, d);
    return d == 0.0 ? 0.0 : Abs(a - b) / d;
}


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
