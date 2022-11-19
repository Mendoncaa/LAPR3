#include <stdio.h>
#include <stdint.h>
#include "pcg32_random_r.h"

uint64_t state;
uint64_t inc;
int main(){
     FILE *f;
	int i;
	f = fopen("/dev/urandom", "r");
	if (f == NULL) {
		printf("Error: open() failed to open /dev/random for reading\n");
    return 1;
	}
	state = fread(&state, sizeof(uint64_t), 1, f);
	inc = fread(&inc, sizeof(uint64_t), 1, f);
	for(i=0;i<32;i++) 
    printf("%8x\n",pcg32_random_r()); 
	return 0;
	
}
