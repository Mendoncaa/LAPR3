/*
 * =====================================================================================
 *  Reads random numbers from /dev/random 
 *  Works on Linux/OSX and other "Unixes"  
 *
 *  Taken from: 
 *
 *  David Johnston 
 *  Random Number Generators—Principles and Practices
 *  2018 Walter de Gruyter GmbH
 *  ISBN 978-1-5015-1513-2 
 *
 * =====================================================================================
 */

#include <stdio.h>
#include <stdint.h>
int main() {
    uint32_t buffer [64]; 
    FILE *f;
    int result;
    int i;
    f = fopen("/dev/urandom", "r"); 
    if (f == NULL) {
        printf("Error: open() failed to open /dev/random for reading\n"); 
        return 1;
        }
    result = fread(buffer , sizeof(uint32_t), 64,f);
    if (result < 1) {
        printf("error , failed to read and words\n"); 
        return 1;
        }
    printf("Read %d words from /dev/urandom\n",result); 
    for(i=0;i<result;i++) 
        printf("%08x\n",buffer[i]); 
    return 0;
}
