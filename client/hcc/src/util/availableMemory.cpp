#include <stddef.h>
#include <stdint.h>
#include <stdlib.h>

int availableMemory() {
  int size = 512;
  uint8_t *buf;

  while ((buf = (uint8_t *) malloc(--size)) == NULL)
    ;

  free(buf);

  return size;
}

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
