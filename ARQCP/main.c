#include <stdio.h>
#include <stdint.h> 
#include <unistd.h>
#include "pcg32_random_r.h"
#include "sensores.h"

uint64_t state=0;  
uint64_t inc=0;

int j=0;


//Arrays para a US[102]
char temp[30];
char velcvento[30];
short dirvento[30];
char humdatm[30];
char humdsolo[30];
char pluvio[30];

//Inicializador de variaveis sem dependeencias
unsigned char ult_temp=15;
unsigned char ult_velc_vento=10;
unsigned short ult_dir_vento;




//US[104]
int erros;
int erroMaximo=5;


//US[103]
short matriz[6][3];
short *ptrMatriz=&matriz[0][0];
short valorMinimo=00;
short valorMaximo=0;
short contador=0;
short soma=0;
short media=0;






void sensTemp(int i){
	
	char temp[3600/freqTemp*24];
	char tempmin= 0;
	char tempmax= 55;

	Sensor sensTemp;
	sensTemp.id=1;
	sensTemp.sensor_type="T";
	sensTemp.max_limit=tempmax;
	sensTemp.min_limit=tempmin;
	sensTemp.freq=freqTemp;
	sensTemp.readings_size=sizeof(temp)/sizeof(unsigned long);
	sensTemp.readings=malloc(sensTemp.readings_size*sizeof(unsigned short));




	for (i; i < 30; i++){
		char comp_rand = pcg32_random_r() % 3;
		
		if(i!=0){
			ult_temp= temp[i-1];
		}
		
		

		temp[i]= sens_temp(ult_temp, comp_rand);
		
	}
	for (i = 0; i < 30; i++)
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

	for(int j=0;j<30;j++){
		soma+=temp[j];
		contador++;
		if(temp[j]<valorMinimo){
			valorMinimo=temp[j];
		}
		if(temp[j]>valorMaximo){
			valorMaximo=temp[j];
		}
	}
	media=soma/contador;
	
		*ptrMatriz=valorMaximo;
		*(ptrMatriz+1)=valorMinimo;
		*(ptrMatriz+2)=media;
		
		valorMinimo=500, valorMaximo=0, contador=0,media=0,soma=0, i=0,j=0;
		
		
		
		
		
		
}


	


	


void sensVelcVento(int i){

	unsigned char velcmin= 0;
	unsigned char velcmax= 150;

	Sensor sensVelcVento={2,"W",velcmax,velcmin,freqVelcVento,3600/freqVelcVento*24,velcvento};

		for (i; i < 30; i++){
			char comp_rand = pcg32_random_r() % 15;
		if(i!=0){
			ult_velc_vento=  velcvento[i-1];
		}
		velcvento[i]= sens_velc_vento(ult_velc_vento, comp_rand);
	}
	for (i = 0; i < 30; i++)
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
	for(int j=0;j<30;j++){
		soma+=velcvento[j];
		contador++;
		if(velcvento[j]<valorMinimo){
			valorMinimo=velcvento[j];
		}
		if(velcvento[j]>valorMaximo){
			valorMaximo=velcvento[j];
		}
	}

	media=soma/contador;
	
		*(ptrMatriz+3)=valorMaximo;
		*(ptrMatriz+4)=valorMinimo;
		*(ptrMatriz+5)=media;

		valorMinimo=500, valorMaximo=0, contador=0,media=0,soma=0, i=0,j=0;
		
		
		

}



void sensDirVento(int i){
		
		unsigned short dirmin= 0;
		unsigned short dirmax= 359;
		
		
		Sensor sensDirVento={3,"D",dirmax,dirmin,freqDirVento,3600/freqDirVento*24,dirvento};
	

		for (i; i < 30; i++){
			char comp_rand = pcg32_random_r() % 50;		
		if(i!=0){
			ult_dir_vento = dirvento[i-1];
		}
		dirvento[i]= sens_dir_vento(ult_dir_vento, comp_rand);
		
		}
		for (i = 0; i < 30; i++)
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

		for(int j=0;j<30;j++){
			soma+=dirvento[j];
			contador++;
		if(dirvento[j]<valorMinimo){
			valorMinimo=dirvento[j];
		}
		if(dirvento[j]>valorMaximo){
			valorMaximo=dirvento[j];
		}
	}
	media=soma/contador;
	
		*(ptrMatriz+6)=valorMaximo;
		*(ptrMatriz+7)=valorMinimo;
		*(ptrMatriz+8)=media;
		
		valorMinimo=500, valorMaximo=0, contador=0,media=0,soma=0, i=0,j=0;
		
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
			//se nao calha 0 quer dizer que nao choveu
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
		soma+=dirvento[j];
		contador++;
	if(pluvio[j]<valorMinimo){
		valorMinimo=pluvio[j];
	}
	if(pluvio[j]>valorMaximo){
		valorMaximo=pluvio[j];
		}
	}
	media=soma/contador;
	
		*(ptrMatriz+15)=valorMaximo;
		*(ptrMatriz+16)=valorMinimo;
		*(ptrMatriz+17)=media;
		
		valorMinimo=500, valorMaximo=0, contador=0,media=0,soma=0, i=0,j=0;
		
	


}
void  sensHumAtm(int i){

		unsigned char humatmmin= 0;
		unsigned char humatmmax= 5;
		char comp_rand;
<<<<<<< HEAD


=======
		
>>>>>>> 0880e50c69dbf5ac6be048c9997cf6d5e8688382
		unsigned char ult_pluvio;
		
		unsigned char ult_hmd_atm;

		Sensor sensHumAtm={4,"A",humatmmax,humatmmin,freqHumAtm,3600/freqHumAtm*24,humdatm};
		

	for (i; i < 30; i++){
			ult_pluvio= pluvio[i];

			if( ult_pluvio==0){
					comp_rand= pcg32_random_r() % 10;

				}
			else{
					comp_rand= pcg32_random_r() % 2;
				}
			
			if(i!=0){
				ult_hmd_atm = humdatm[i-1];
			}
			
			humdatm[i]= sens_humd_atm(ult_hmd_atm, ult_pluvio, comp_rand);
		
		}
		for (i = 0; i < 30; i++)
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
	
	for(int j=0;j<30;j++){
		soma+=dirvento[j];
		contador++;
		if(humdatm[j]<valorMinimo){
			valorMinimo=humdatm[j];
		}
		if(humdatm[j]>valorMaximo){
			valorMaximo=humdatm[j];
		}
		}
	media=soma/contador;
	
		*(ptrMatriz+9)=valorMaximo;
		*(ptrMatriz+10)=valorMinimo;
		*(ptrMatriz+11)=media;
		
		valorMinimo=500, valorMaximo=0, contador=0,media=0,soma=0, i=0,j=0;
		
		
		
}
void sensHumSolo(int i){

			unsigned char solomin= 0;
			unsigned char solomax= 5;
			char comp_rand;
			
			unsigned char ult_humd_solo;
			unsigned char ult_pluvio=0;
			
			Sensor sensHumSolo={5,"S",solomax,solomin,freqHumSolo,3600/freqHumSolo*24,humdsolo};

		
			
			for (i; i < 30; i++){
				
				ult_pluvio= pluvio[i];
				
				if( ult_pluvio==0){
					 
					comp_rand= pcg32_random_r() % 10;

				}
				else{
					comp_rand= pcg32_random_r() % 2;
				}
				
			
			
			if(i!=0){
				
				ult_humd_solo= humdsolo[i-1];

				
			
			}
			humdsolo[i]= sens_humd_solo(ult_humd_solo, ult_pluvio, comp_rand);
			}
	for (i = 0; i < 30; i++)
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
	
	
	for(int j=0;j<30;j++){
		soma+=dirvento[j];
		contador++;
	if(humdsolo[j]<valorMinimo){
		valorMinimo=humdsolo[j];
	}
	if(humdsolo[j]>valorMaximo){
		valorMaximo=humdsolo[j];
		}
	}
	media=soma/contador;
	
		*(ptrMatriz+12)=valorMaximo;
		*(ptrMatriz+13)=valorMinimo;
		*(ptrMatriz+14)=media;
		
		valorMinimo=500, valorMaximo=0, contador=0,media=0,soma=0, i=0,j=0;
		
		
		

}
<<<<<<< HEAD
=======


	//sensor pluvio


void sensPluvio(int i){

		char minPluvio=0;
		char maxPluvio=5;
		
	
		
		unsigned char ult_pluvio;
		char ult_temp;
    	
		Sensor sensPluvio={6,"R",maxPluvio,minPluvio,freqPluvio,3600/freqPluvio*24,pluvio};

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
		soma+=dirvento[j];
		contador++;
	if(pluvio[j]<valorMinimo){
		valorMinimo=pluvio[j];
	}
	if(pluvio[j]>valorMaximo){
		valorMaximo=pluvio[j];
		}
	}
	media=soma/contador;
	
		*(ptrMatriz+15)=valorMaximo;
		*(ptrMatriz+16)=valorMinimo;
		*(ptrMatriz+17)=media;
		
		valorMinimo=500, valorMaximo=0, contador=0,media=0,soma=0, i=0,j=0;
		
	


}
>>>>>>> 0880e50c69dbf5ac6be048c9997cf6d5e8688382
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
	printf("Qual a frequencia de leitura dos sensor de temperatura? (em segundos)\n");
	int freqTemp;
	scanf("%d",&freqTemp);
	printf("Qual a frequencia de leitura dos sensor de velocidade do vento? (em segundos)\n");
	int freqVelcVento;
	scanf("%d",&freqVelcVento);
	printf("Qual a frequencia de leitura dos sensor de direção do vento? (em segundos)\n");
	int freqDirVento;
	scanf("%d",&freqDirVento);
	printf("Qual a frequencia de leitura dos sensor de humidade atmosférica? (em segundos)\n");
	int freqHumAtm;
	scanf("%d",&freqHumAtm);
	printf("Qual a frequencia de leitura dos sensor de humidade do solo? (em segundos)\n");
	int freqHumSolo;
	scanf("%d",&freqHumSolo);
	printf("Qual a frequencia de leitura dos sensor de pluviosidade? (em segundos)\n");
	int freqPluvio;
	scanf("%d",&freqPluvio);
	printf("Qual o número máximo de erros que pode ocorrer antes de reiniciar o sensor?\n");
	

	sensTemp(0);
    sensVelcVento(0);
    sensDirVento(0);
    sensHumAtm(0);
    sensHumSolo(0);
	sensPluvio(0);
	printf("\t\t\tValor Mínimo\tValor Máximo\tMédia dos valores");
	printf("\n");
	for(int i=0; i<6;i++){
		switch (i){
			case 0:
				printf("Temperatura:\t\t");
				break;
			
			
			case 1:
				printf("Velocidade do vento:\t");
				break;

			
			case 2:
				printf("Direção do vento:\t");
				break;
			

			case 3:
				printf("Humidade atmosférica:\t");
				break;

			
			case 4: 
				printf("Humidade do solo:\t");
				break;


			case 5:
				printf("Puviosidade:\t\t");
				break;
		}
		for(int j=0; j<3;j++){
			printf("%10d\t",matriz[i][j]);
		}
		printf("\n");
	}
			
	return 0;




}
			


