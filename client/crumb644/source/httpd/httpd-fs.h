#ifndef __HTTPD_FS_H__
#define __HTTPD_FS_H__

// Filesystem in flash ROM


// Pointer to the file data and length of the file
struct httpd_fs_file {
  char *data; // content of the file
  int len;    // length of the file
  int index;  // index of the file in filesystem
};


// A file in the filesystem. Files are stored as a linked list.
struct httpd_fsdata_file {
   struct httpd_fsdata_file *next; // next file in the linked list
   char *name;  // file name
   char *data;  // pointer to file data
   int len;     // length of the file data
};


// Open a file
// name must be in propgram memory
int httpd_fs_open_P(const char *name, struct httpd_fs_file *file);

// Open a file
// name must be in RAM
int httpd_fs_open(const char *name, struct httpd_fs_file *file);

// Return a pointer to the filename (in program memory) of the
// file identified by the index.
char* httpd_fs_getName(int index);

// Return the access counter of the file identified by the index
int httpd_fs_getCount(int index) ;

// Count access to a file, identified by the index.
void httpd_fs_countAccess(int index);

#endif /* __HTTPD_FS_H__ */
