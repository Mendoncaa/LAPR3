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
void SensTemp(){

		char ult_temp=15;

		for (int i = 0; i < 24; i++){
		
			char comp_rand = pcg32_random_r() % 3;
		
		
		if(i!=0){
			ult_temp=  *(ptrtemp-1)
		}
		
		sens_temp(ult_temp, comp_rand);

		*ptrtemp++;
		
	}

	
	
	void SensVelcVento(){

		char ult_velc_vento=10;

		for (int i = 0; i < 24; i++){
		
			char comp_rand = pcg32_random_r() % 15;
		
		if(i!=0){
			ult_velc_vento=  *(ptrvento-1);
		}
		
		sens_velc_ventp(ult_velc_vento, comp_rand);

		*ptrvento++;
		
	}
	
	void SensDirVento(){
		//so pode variar entre 0 e 359

		char ult_dir_vento=250;

		for (int i = 0; i < 24; i++){
		
			char comp_rand = pcg32_random_r() % 15;

		

		if(i!=0){
			ult_dir_vento=  *(ptrdir-1);
		}
		
		sens_velc_vento(ult_dir_vento, comp_rand);

		*ptrdir++;
		
	}
	
	void SensHumdAtm(){

		char ult_dir_vento=250;

		for (int i = 0; i < 24; i++){
		
			char comp_rand = pcg32_random_r() % 15;
			
		

		if(i!=0){
			ult_dir_vento=  *(ptrdir-1);
		}
		
		sens_velc_vento(ult_dir_vento, comp_rand);

		*ptrvento++;
		
	}

	


	//sensor pluvio


	void SensPluvio(){

		char ult_temp;

    	*ptrtemp = *(ptrtemp-24);

    	unsigned char ult_pluvio;
		char pluvio[24];

		char *ptrpluvio= pluvio

    	


    	for (int i = 0; i < 24; i++){

        	char ult_temp = arraytemp[i];

        	//sleep(k); //criar função para dar output de x em x segundos

        	char comp_rand = pcg32_random_r() % 3; //mudar a alteração de temp
			//componente aleatoria que gera um numero random, alteração

        	int random_number = pcg32_random_r() % ult_temp ;
			//


        if (random_number !=0){ 	

            comp_rand = 0;

        }
		// O valor só altera se a componente gerar o valor 0


        if (i !=0){		

            ult_pluvio = *(ptrpluvio-1);

        }
		// o ultimo valor da pluviosidade é a posição anterior para onde o apontador está a apontar



        sens_pluvio(ult_pluvio,ult_temp, comp_rand);

        *ptrpluvio++;

    }
	
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

