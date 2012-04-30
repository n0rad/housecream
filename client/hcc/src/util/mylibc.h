#ifndef MYLIBC_H
#define MYLIBC_H

uint8_t readIP(char *buf, uint16_t len, uint8_t newIp[4]);

float floatRelativeDiff(float a, float b);
int my_strpos(const char *s, int ch);
int strstrpos_P(const char *s, const prog_char *wanted);

#define Abs(x)    ((x) < 0 ? -(x) : (x))
#define Max(a, b) ((a) > (b) ? (a) : (b))


#endif
