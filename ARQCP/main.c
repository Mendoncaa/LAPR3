#include <stdio.h>
#include <stdint.h> 
#include "pcg32_random_r.h"

uint64_t state=0;  
uint64_t inc=0;

int main() { 
	uint32_t buffer [64]; 
	FILE *f;
	int result;
	f = fopen("/dev/urandom", "r"); 
	if (f == NULL) {
		printf("Error: open() failed to open /dev/random for reading\n"); 
		return 1;
    }
	result = fread(buffer , sizeof(uint32_t), 64,f);
	fread(&state , sizeof(uint64_t),1,f);
	fread(&inc , sizeof(uint64_t), 1,f);
    if (result < 1) {
		printf("Error , failed to read and words\n"); 
		return 1;
    }
     
	
	printf("%d\n",pcg32_random_r()); 
    return 0;




}
