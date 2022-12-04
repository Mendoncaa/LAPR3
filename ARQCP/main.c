#include <stdio.h>
#include <stdint.h> 
#include <unistd.h>
#include "pcg32_random_r.h"
#include "sensores.h"

uint64_t state=0;  
uint64_t inc=0;




//Arrays para a US[102]
char temp[30];
char velcvento[30];
short dirvento[30];
char humdatm[30];
char humdsolo[30];
char pluvio[30];

//Inicializador de variaveis sem dependeencias
char ult_temp=15;
char ult_velc_vento=10;
char ult_dir_vento;


//US[104]
int erros;
int erroMaximo=5;


//US[103]
short matriz[6][3];
short *ptrMatriz=&matriz[0][0];
short valorMinimo=400;
short valorMaximo=0;
short contador=0;
short soma=0;
short media=0;






void sensTemp(int i){
	
	char tempmin= -20;
	char tempmax= 55;


	for (i; i < 30; i++){
		char comp_rand = pcg32_random_r() % 3;
		
		if(i!=0){
			ult_temp= temp[i-1];
		}
		
		

		temp[i]= sens_temp(ult_temp, comp_rand);
		
	}
	for (int i = 0; i < 30; i++)
	{
		if (temp[i]> tempmax || temp[i]< tempmin){
			erros++;
		}
		else{
			erros=0;
		}
		if(erros==erroMaximo){
			int init = i-4;
			sensTemp(init);
		}
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
		i=0;

}
	


void sensVelcVento(int i){

	unsigned char velcmin= 0;
	unsigned char velcmax= 150;

		for (i; i < 30; i++){
			char comp_rand = pcg32_random_r() % 15;
		if(i!=0){
			ult_velc_vento=  velcvento[i-1];
		}
		velcvento[i]= sens_velc_vento(ult_velc_vento, comp_rand);
	}
	for (int i = 0; i < 30; i++)
	{
		if (velcvento[i]> velcmax || velcvento[i]< velcmin){
			erros++;
		}
		else{
			erros=0;
		}
		if(erros==erroMaximo){
			int init = i-4;
			sensVelcVento(init);
		}
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
	
		*(ptrMatriz+4)=valorMaximo;
		*(ptrMatriz+5)=valorMinimo;
		*(ptrMatriz+6)=media;
		
		valorMinimo=400;
		valorMaximo=0;
		contador=0;
		media=0;
		soma=0;
		i=0;

}



void sensDirVento(int i){
		
		unsigned short dirmin= 0;
		unsigned short dirmax= 359;
		
		

		for (i; i < 30; i++){
			char comp_rand = pcg32_random_r() % 50;		
		if(i!=0){
			ult_dir_vento = dirvento[i-1];
		}
		dirvento[i]= sens_dir_vento(ult_dir_vento, comp_rand);
		
		}
		for (int i = 0; i < 30; i++)
	{
		if (dirvento[i]> dirmax || dirvento[i]< dirmin){
			erros++;
		}
		else{
			erros=0;
		}
		if(erros==erroMaximo){
			int init = i-4;
			sensDirVento(init);
		}
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
	
		*(ptrMatriz+7)=valorMaximo;
		*(ptrMatriz+8)=valorMinimo;
		*(ptrMatriz+9)=media;
		
		valorMinimo=400;
		valorMaximo=0;
		contador=0;
		media=0;
		soma=0;
		i=0;
}
	
	
void  sensHumAtm(int i){

		unsigned char humatmmin= 0;
		unsigned char humatmmax= 5;
		char comp_rand;





		unsigned char ult_pluvio;
		
		unsigned char ult_hmd_atm;
		
	for (i; i < 30; i++){
			ult_pluvio= pluvio[i];

			if( ult_pluvio==0){
				
				comp_rand = pcg32_random_r() % 5;
			}
			
			if(i!=0){
				ult_hmd_atm = humdatm[i-1];
			}
			
			humdatm[i]= sens_humd_atm(ult_hmd_atm, ult_pluvio, comp_rand);
		
		}
		for (int i = 0; i < 30; i++)
	{
		if (humdatm[i]> humatmmax || humdatm[i]< humatmmin){
			erros++;
		}
		else{
			erros=0;
		}
		if(erros==erroMaximo){
			int init = i-4;
			sensHumAtm(init);
		}
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
	
		*(ptrMatriz+10)=valorMaximo;
		*(ptrMatriz+11)=valorMinimo;
		*(ptrMatriz+12)=media;
		
		valorMinimo=400;
		valorMaximo=0;
		contador=0;
		media=0;
		soma=0;
		i=0;
		
		
}
	
	
	
void sensHumSolo(int i){

			unsigned char solomin= 0;
			unsigned char solomax= 5;

			
			unsigned char ult_humd_solo;
			unsigned char ult_pluvio=0;

			
			for (i; i < 30; i++){
				char comp_rand= pcg32_random_r() % 5;
			
			
			if(i!=0){
				
				ult_humd_solo= humdsolo[i-1];

				
			
			}
			humdsolo[i]= sens_humd_solo(ult_humd_solo, ult_pluvio, comp_rand);
			}
	for (int i = 0; i < 30; i++)
	{
		if (humdsolo[i]> solomax || humdsolo[i]< solomin){
			erros++;
		}
		else{
			erros=0;
		}
		if(erros==erroMaximo){
			int init = i-4;
			sensHumSolo(init);
		}
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
	
		*(ptrMatriz+13)=valorMaximo;
		*(ptrMatriz+14)=valorMinimo;
		*(ptrMatriz+15)=media;
		
		valorMinimo=400;
		valorMaximo=0;
		contador=0;
		media=0;
		soma=0;
		i=0;
		
		

}


	//sensor pluvio


void sensPluvio(int i){

		char minPluvio=0;
		char maxPluvio=5;
		
	
		
		unsigned char ult_pluvio;
		char ult_temp;
    	

    	for (i; i < 30; i++){
        	char ult_temp = temp[i];
        	//sleep(k); //criar função para dar output de x em x segundos
        	char comp_rand = pcg32_random_r() % 5; //mudar a alteração de temp
			//componente aleatoria que gera um numero random, alteração
        	int comp_relative = pcg32_random_r() % ult_temp ;
			//
        if (comp_relative !=0){ 	
            comp_rand = 0;

        }
		// O valor só altera se a componente gerar o valor 0
        if (i !=0){		
			ult_pluvio = pluvio[i-1];
        }
		// o ultimo valor da pluviosidade é a posição anterior para onde o apontador está a apontar
        pluvio[i]= sens_pluvio(ult_pluvio,ult_temp, comp_rand);
			

		}
		for (i = 0; i < 30; i++){
		if (pluvio[i]> maxPluvio || pluvio[i]< minPluvio){
			erros++;
		}
		else{
			erros=0;
		}
		if(erros==erroMaximo){
			int init = i-4;
			sensPluvio(init);
		}
	}
	for(int j=0;j<30;j++){
		soma+=dirvento[i];
		contador++;
	if(pluvio[j]<valorMinimo){
		valorMinimo=pluvio[j];
	}
	if(pluvio[j]>valorMaximo){
		valorMaximo=pluvio[j];
		}
	}
	media=soma/contador;
	
		*(ptrMatriz+16)=valorMaximo;
		*(ptrMatriz+17)=valorMinimo;
		*(ptrMatriz+18)=media;
		
		valorMinimo=400;
		valorMaximo=0;
		contador=0;
		media=0;
		soma=0;
		i=0;
	


}
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
    sensTemp(0);
    sensVelcVento(0);
    sensDirVento(0);
    sensHumAtm(0);
    sensHumSolo(0);
	sensPluvio(0);
	
	for(int i=0; i<6;i++){
		for(int j=0; j<3;j++){
			printf("%10d\n",matriz[i][j]);
		}
	}
			//printf("%d\n",pcg32_random_r()); 
	return 0;




}
			


