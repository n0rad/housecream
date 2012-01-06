#include "httpd.h"
#include "httpd-fs.h"
#include <avr/pgmspace.h>
#include "../uip/uip-conf.h"
#include "httpd-fsdata.c"

// Filesystem in flash ROM

// access counters for all files
static u16_t count[HTTPD_FS_NUMFILES];


// compare two filenames
// str1 = RAM
// str2 = progmem

static u8_t httpd_fs_strcmp(const char *str1, const char *str2) {
    // parameter1 = RAM
    // parameter2 = progmem
    u8_t i;
    i = 0;
    while (1) {
        if (pgm_read_byte(&str2[i]) == 0 ||
                str1[i] == '\r' ||
                str1[i] == '\n' ||
                str1[i] == ' ') {
            return 0;
        }
        if (str1[i] != pgm_read_byte(&str2[i])) {
            return 1;
        }
        ++i;
    }
}

// compare two filenames
// str1 = progmem
// str2 = progmem

static u8_t httpd_fs_strcmp_P(const char *str1, const char *str2) {
    u8_t i;
    i = 0;
    while (1) {
        if (pgm_read_byte(&str2[i]) == 0 ||
                pgm_read_byte(&str1[i]) == '\r' ||
                pgm_read_byte(&str1[i]) == '\n' ||
                pgm_read_byte(&str1[i]) == ' '){
            return 0;
        }
        if (pgm_read_byte(&str1[i]) != pgm_read_byte(&str2[i])) {
            return 1;
        }
        ++i;
    }
}


// open a file
// name in RAM
// returns the index of the file or -1 if not found

int httpd_fs_open(const char *name, struct httpd_fs_file *file) {
    u16_t i = 0;
    for (struct httpd_fsdata_file* f=HTTPD_FS_ROOT; f != 0; f=f->next) {
        if (httpd_fs_strcmp(name, f->name) == 0) {
            file->data = f->data; 
            file->len = f->len;
            file->index=i;
            return i;
        }
        ++i;
    }
    file->index=-1;
    return -1;
}


// open a file
// name in program memory
// returns the index of the file or -1 if not found

int httpd_fs_open_P(const char *name, struct httpd_fs_file *file) {
    u16_t i = 0;
    for (struct httpd_fsdata_file* f=HTTPD_FS_ROOT; f != 0; f=f->next) {
        if (httpd_fs_strcmp_P(name, f->name) == 0) {
            file->data = f->data; 
            file->len = f->len;
            file->index=i;
            return i;
        }
        ++i;
    }
    file->index=-1;
    return -1;
}



// Return a pointer to the filename (in program memory) of the
// file identified by the index.

char* httpd_fs_getName(int index) {
    if (index>=HTTPD_FS_NUMFILES || index<0) {
        return 0;
    }
    int i=0;
    struct httpd_fsdata_file* f=HTTPD_FS_ROOT;
    while (i<index) {
        f=f->next;
        i++;
    }
    return f->name;
}


// Return the access counter of the file identified by the index

int httpd_fs_getCount(int index) {
    if (index>=HTTPD_FS_NUMFILES || index<0) {
        return -1;
    }
    return count[index];
}


// Count access to a file for statistics
void httpd_fs_countAccess(int index) {
    if (index<HTTPD_FS_NUMFILES && index>=0) {
        count[index]++;
    }
}
