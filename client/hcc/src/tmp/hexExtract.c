int x = SOME_INTEGER;
char res[5]; /* two bytes of hex = 4 characters, plus NULL terminator */

if (x <= 0xFFFF)
{
    sprintf(&res[0], "%04x", x);
}
Your integer may contain more than four hex digits worth of data, hence the check first.

If you're not allowed to use library functions, divide it down into nybbles manually:

#define TO_HEX(i) (i <= 9 : '0' + i ? 'A' - 10 + i)

int x = SOME_INTEGER;
char res[5];

if (x <= 0xFFFF)
{
    char nybble;
    res[0] = TO_HEX(((x & 0xF000) >> 12));
    res[1] = TO_HEX(((x & 0x0F00) >> 8));
    res[2] = TO_HEX(((x & 0x00F0) >> 4));
    res[3] = TO_HEX(((x & 0x000F));
    res[4] = '\0';
}
