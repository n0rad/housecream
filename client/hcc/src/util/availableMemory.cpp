#include <stddef.h>
#include <stdint.h>
#include <stdlib.h>

extern int __bss_end;
extern void *__brkval;
int getFreeMemory() {
  int free_memory;
  if((int)__brkval == 0)
    free_memory = ((int)&free_memory) - ((int)&__bss_end);
  else
    free_memory = ((int)&free_memory) - ((int)__brkval);


  return free_memory;
}
