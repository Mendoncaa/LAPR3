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

for(int = 0; i<(sizeof(array__)/sizeof(array__[0]));i++){   //pluviosidade
	sum += array__[i];
	count++;
	if (array__[i] < minValue) {
	minValue = array__[i];
	}
	if (arraypluvio[i] > maxValue) {
	maxValue = array__[i];
	}
}
	avg = sum/count;
	*(ptrMtrx+15) = maxValue;
	*(ptrMtrx+16) = minValue;
	*(ptrMtrx+17)= avg;
	minValue =400;
	maxValue=0;
	sum=0; 
	count=0; 
	avg=0;
