#ifndef MYLIBC_H
#define MYLIBC_H


float RelDif(float a, float b);
int my_strpos(const char *s, int ch);
int strstrpos_P(const char *s, const prog_char *wanted);

#define Abs(x)    ((x) < 0 ? -(x) : (x))
#define Max(a, b) ((a) > (b) ? (a) : (b))


#endif
