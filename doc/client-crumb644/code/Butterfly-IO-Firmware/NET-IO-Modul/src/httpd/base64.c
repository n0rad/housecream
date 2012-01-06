#include <stdio.h>
#include <stdlib.h>
#include "base64.h"

// Author: Bob Trower

// Translation Table as described in RFC1113
static const char cb64[]="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

// Translation Table to decode
static const char cd64[]="|$$$}rstuvwxyz{$$$$$$$>?@ABCDEFGHIJKLMNOPQRSTUVW$$$$$$XYZ[\\]^_`abcdefghijklmnopq";

// encode 3 8-bit binary bytes as 4 '6-bit' characters
void encodeblock64( unsigned char in[3], unsigned char out[4], int len )
{
    out[0] = cb64[ in[0] >> 2 ];
    out[1] = cb64[ ((in[0] & 0x03) << 4) | ((in[1] & 0xf0) >> 4) ];
    out[2] = (unsigned char) (len > 1 ? cb64[ ((in[1] & 0x0f) << 2) | ((in[2] & 0xc0) >> 6) ] : '=');
    out[3] = (unsigned char) (len > 2 ? cb64[ in[2] & 0x3f ] : '=');
}

// base64 encode a string
void encode64(char *outstr, char *instr)
{
    unsigned char in[3], out[4];
    int i, len;
    while(*instr) {
        len = 0;
        for( i = 0; i < 3; i++ ) {
            in[i] = *instr;
            if(*instr) {
               instr++;
               len++;
            }
        }
        if( len ) {
            encodeblock64( in, out, len );
            for( i = 0; i < 4; i++ ) {
               *outstr++ = out[i];
            }
        }
    }
    *outstr = '\0';
}
