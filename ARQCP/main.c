#include <stdio.h>
#include <stdint.h> 
#include <unistd.h>
#include "pcg32_random_r.h"

uint64_t state=0;  
uint64_t inc=0;


char temp[30];
//char *ptrtemp = &temp;

char velcvento[30];
//char *ptrvento= &velcvento;

char dirvento[30];
//char *ptrdirvento= &dirvento;

char humdatm[30];
//char *ptrhumdatm= &humdatm;

char humdsolo[30];
//char *ptrhumdsolo= &humdsolo;

char pluvio[30];

int numElementos=30;

int k=0;
int j=0;

short matriz[6][3];
short *ptrMatriz=&matriz[0][0];
short valorMinimo=400;
short valorMaximo=0;
short contador=0;
short soma=0;
short media=0;

int erros=0;
int erroMaximo=3;



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
	for(int i=0;i<30;i++){
		soma+=temp[i];
		contador++;
		if(temp[i]<valorMinimo){
			valorMinimo=temp[i];
		}
		if(temp[i]>valorMaximo){
			valorMaximo=temp[i];
		}
	}
	media=soma/contador;
	
		*ptrMatriz=valorMaximo;
		*(ptrMatriz+1)=valorMinimo;
		*(ptrMatriz+2)=media;
		
		valorMinimo=400;
		valorMaximo=0;
		contador=0;
		media=0;
		soma=0;
		j=0;

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
	for(int i=0;i<30;i++){
		soma+=velcvento[i];
		contador++;
		if(velcvento[i]<valorMinimo){
			valorMinimo=velcvento[i];
		}
		if(velcvento[i]>valorMaximo){
			valorMaximo=velcvento[i];
		}
	}
	media=soma/contador;
	
		*ptrMatriz=valorMaximo;
		*(ptrMatriz+1)=valorMinimo;
		*(ptrMatriz+2)=media;
		
		valorMinimo=400;
		valorMaximo=0;
		contador=0;
		media=0;
		soma=0;
		j=0;

}



void SensDirVento(){
		//so pode variar entre 0 e 359
		char ult_dir_vento=250;
		for (int i = 0; i < 24; i++){
			char comp_rand = pcg32_random_r() % 15;		
		if(i!=0){
			ult_dir_vento = *(ptrdir-1);
		}
		sens_velc_vento(ult_dir_vento, comp_rand);
		*ptrdirvento++;
		}
		for(int i=0;i<30;i++){
		soma+=dirvento[i];
		contador++;
		if(dirvento[i]<valorMinimo){
			valorMinimo=dirvento[i];
		}
		if(dirvento[i]>valorMaximo){
			valorMaximo=dirvento[i];
		}
	}
	media=soma/contador;
	
		*ptrMatriz=valorMaximo;
		*(ptrMatriz+1)=valorMinimo;
		*(ptrMatriz+2)=media;
		
		valorMinimo=400;
		valorMaximo=0;
		contador=0;
		media=0;
		soma=0;
		j=0;
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
	
	for(int i=0;i<30;i++){
		soma+=dirvento[i];
		contador++;
		if(humdatm[i]<valorMinimo){
			valorMinimo=humdatm[i];
		}
		if(humdatm[i]>valorMaximo){
			valorMaximo=humdatm[i];
		}
	}
	media=soma/contador;
	
		*ptrMatriz=valorMaximo;
		*(ptrMatriz+1)=valorMinimo;
		*(ptrMatriz+2)=media;
		
		valorMinimo=400;
		valorMaximo=0;
		contador=0;
		media=0;
		soma=0;
		j=0;
		
		
}
	
	
	
void SensHumdSolo(){
			char ult_humd_solo;
			for (int i = 0; i < 24; i++){
				char comp_rand= pcg32_random_r() % 15;
			
			}
			if(i!=0){
			
			}
	for(int i=0;i<30;i++){
		soma+=dirvento[i];
		contador++;
	if(humdsolo[i]<valorMinimo){
		valorMinimo=humdsolo[i];
	}
	if(humdsolo[i]>valorMaximo){
		valorMaximo=humdsolo[i];
		}
	}
	media=soma/contador;
	
		*ptrMatriz=valorMaximo;
		*(ptrMatriz+1)=valorMinimo;
		*(ptrMatriz+2)=media;
		
		valorMinimo=400;
		valorMaximo=0;
		contador=0;
		media=0;
		soma=0;
		j=0;
		
		

}


	//sensor pluvio


void SensPluvio(){
		char ult_temp;
    	*ptrtemp = *(ptrtemp-24);
    	unsigned char ult_pluvio;
		char *ptrpluvio= pluvio;
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
	for(int i=0;i<30;i++){
		soma+=dirvento[i];
		contador++;
	if(pluvio[i]<valorMinimo){
		valorMinimo=pluvio[i];
	}
	if(pluvio[i]>valorMaximo){
		valorMaximo=pluvio[i];
		}
	}
	media=soma/contador;
	
		*ptrMatriz=valorMaximo;
		*(ptrMatriz+1)=valorMinimo;
		*(ptrMatriz+2)=media;
		
		valorMinimo=400;
		valorMaximo=0;
		contador=0;
		media=0;
		soma=0;
		j=0;


}
			


