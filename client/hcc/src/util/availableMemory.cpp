#include <stddef.h>
#include <stdint.h>
#include <stdlib.h>

int availableMemory() {
  int size = 2048;
  uint8_t *buf;

  while ((buf = (uint8_t *) malloc(--size)) == NULL)
    ;

  free(buf);

  return size;
}
