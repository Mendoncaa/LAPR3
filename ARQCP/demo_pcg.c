/*
 * =====================================================================================
 *  PCG Random generator
 *
 *  See:  https://www.pcg-random.org 
 *
 *  "Uglyfied" for an easier  assembly translation  
 *
 *  Note: the values of state and inc should be initialized with values from /dev/random  
 *  
 * =====================================================================================
 */

#include <stdio.h> 
#include <stdint.h> 
#include <stdlib.h>


uint32_t pcg32_random_r()
{	
	uint64_t state=rand()%64; 
	uint64_t inc=rand()%64;
    uint64_t oldstate = state;
    // Advance internal state
    state = oldstate * 6364136223846793005ULL + (inc|1);
    // Calculate output function (XSH RR), uses old state for max ILP
    uint32_t xorshifted = ((oldstate >> 18u) ^ oldstate) >> 27u;
    uint32_t rot = oldstate >> 59u;
    return (xorshifted >> rot) | (xorshifted << ((-rot) & 31));
}

int main()
    { 
	
      int i;   
	  srand(time(NULL));
      for(i=0;i<32;i++) 
		printf("%d", rand() % 64); 
      return 0; 
    } 
