#include <stdio.h>
#include <stdint.h> 
#include <unistd.h>
#include <stdlib.h>
#include "struct.h"
#include "pcg32_random_r.h"
#include "sensores.h"

uint64_t state=0;  
uint64_t inc=0;

int j=0;


//Arrays para a US[102]
/*char temp[30];
char velcvento[30];
short dirvento[30];
char humdatm[30];
char humdsolo[30];
char pluvio[30];*/


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






/* char velcvento[3600/freqVelcVento*24];
short dirvento[3600/freqDirVento*24];
char humdatm[3600/freqHumDatm*24];
char humdsolo[3600/freqHumDsolo*24];
char pluvio[3600/freqPluvio*24];*/



void sensTemp(int i, int freqTemp) {
  char tempmin = 0;
  char tempmax = 55;
  char temp[3600 / freqTemp * 24];

  int numSensores=0;
	printf("Quantos sensores de temperatura deseja criar? \n");
	scanf("%d", &numSensores);
  
  Sensor *sensTemperatura = malloc(numSensores * sizeof(Sensor));
  
  	for (int i = 0; i < numSensores; i++) {
    sensTemperatura[i].id = i + 1;
    sensTemperatura[i].sensor_type = 84;
    sensTemperatura[i].max_limit = tempmax;
    sensTemperatura[i].min_limit = tempmin;
    sensTemperatura[i].frequency = freqTemp;
    sensTemperatura[i].readings_size = 3600 / freqTemp * 24;
    sensTemperatura[i].readings = malloc(sensTemperatura->readings_size * sizeof(unsigned short));
	}
    
	for (int i = 0; i < numSensores; i++) {
    char comp_rand = pcg32_random_r() % 3;
    
    if (i != 0) {
      ult_temp = sensTemperatura[i - 1].readings[sensTemperatura[i - 1].readings_size - 1];
    }
    
    for (int j = 0; j < sensTemperatura[i].readings_size; j++) {
      sensTemperatura[i].readings[j] = (unsigned short) sens_temp(ult_temp, comp_rand);
    }

    
    for (int j = 0; j < sensTemperatura[i].readings_size; j++) {
      if (sensTemperatura[i].readings[j] > tempmax || sensTemperatura[i].readings[j] < tempmin) {
        erros++;
      } else {
        erros = 0;
      }
      if (erros == erroMaximo) {
        int init = j - 4;
        for (int k = 0; k < init; k++) {
          sensTemperatura[i].readings[k] = (unsigned short) sens_temp(ult_temp, comp_rand);
        }
        for (int k = init; k < sensTemperatura[i].readings_size; k++) {
          sensTemperatura[i].readings[k] = (unsigned short) sens_temp(sensTemperatura[i].readings[k - 1], comp_rand);
        }
        erros = 0;
      }
    }
  }
  int choice;
	
  
  while (1) {
    printf("\nMenu:\n");
    printf("1. Add a sensor\n");
    printf("2. Remove a sensor\n");
    printf("3. Analyze a sensor\n");
    printf("4. Quit\n");
    
    int choice;
    printf("Enter your choice: ");
    scanf("%d", &choice);
    
    if (choice == 4) {
      break;
    }
    
    if (choice == 1) {
      // Add a sensor
      numSensores++;
      sensTemperatura = realloc(sensTemperatura, numSensores * sizeof(Sensor));
      
      sensTemperatura[numSensores - 1].id = numSensores;
      sensTemperatura[numSensores - 1].sensor_type = 84;
      sensTemperatura[numSensores - 1].max_limit = tempmax;
      sensTemperatura[numSensores - 1].min_limit = tempmin;
      sensTemperatura[numSensores - 1].frequency = freqTemp;
      sensTemperatura[numSensores - 1].readings_size = 3600 / freqTemp * 24;
      sensTemperatura[numSensores - 1].readings = malloc(sensTemperatura->readings_size * sizeof(unsigned short));
      
      printf("Sensor added.\n");
    }

	if (choice == 2) {
      // Remove a sensor
      if (numSensores == 0) {
        printf("No sensors to remove.\n");
      } else {
        printf("Enter the ID of the sensor to remove: ");
        int id;
        scanf("%d", &id);
        
        int found = 0;
        for (int i = 0; i < numSensores; i++) {
          if (sensTemperatura[i].id == id) {
            found = 1;
            free(sensTemperatura[i].readings);
            for (int j = i; j < numSensores - 1; j++) {
              sensTemperatura[j] = sensTemperatura[j + 1];
            }
            numSensores--;
            sensTemperatura = realloc(sensTemperatura, numSensores * sizeof(Sensor));
            break;
          }
        }
        
        if (found) {
          printf("Sensor removed.\n");
        } else {
          printf("Sensor not found.\n");
        }
      }
    } else if (choice == 3) {
		if(numSensores>1){
		printf("Qual sensor deseja analisar? \n");
		for(int i=0;i<numSensores;i++){
			printf("Sensor %d \n", sensTemperatura[i].id);
		}
		scanf("%d", &sensor);
		while (sensor==0){
			printf("Sensor invalido, tente novamente \n");
			scanf("%d", &sensor);

		}

		//meter a média e a print da matriz
		

		
	}else{
		sensor=1;
	}

      



    
  }
}
}


	


	


void sensVelcVento(int i, int freqVelcVento){
	
	
	char velcvento[3600/freqVelcVento*24];
	unsigned char velcmin= 0;
	unsigned char velcmax= 150;

	Sensor sensVelocidadeVento;
	sensVelocidadeVento.id=2;
	sensVelocidadeVento.sensor_type=87;
	sensVelocidadeVento.max_limit=velcmax;
	sensVelocidadeVento.min_limit=velcmin;
	sensVelocidadeVento.frequency=freqVelcVento;
	sensVelocidadeVento.readings_size=sizeof(velcvento)/sizeof(unsigned long);
	sensVelocidadeVento.readings=malloc(sensVelocidadeVento.readings_size*sizeof(unsigned short));
	for(int i =0;i<sensVelocidadeVento.readings_size;i++){
		sensVelocidadeVento.readings[i]=(unsigned short) velcvento[i];
	} 

	for (i; i <sizeof(velcvento); i++){
			char comp_rand = pcg32_random_r() % 15;
		if(i!=0){
			ult_velc_vento=  velcvento[i-1];
		}
		velcvento[i]= sens_velc_vento(ult_velc_vento, comp_rand);
	}
	for (i = 0; i <sizeof(velcvento); i++)
	{
		if (velcvento[i]> velcmax || velcvento[i]< velcmin){
			erros++;
		}
		else{
			erros=0;
		}
		if(erros==erroMaximo){
			int init = i-4;
			sensVelcVento(init, freqVelcVento);
		}
	}
	for(int j=0;j<sizeof(velcvento);j++){
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
		
		free(sensVelocidadeVento.readings);
		sensVelocidadeVento.readings=NULL;
		

}



void sensDirVento(int i, int freqDirVento){
		

	
		char dirvento[3600/freqDirVento*24];
		unsigned short dirmin= 0;
		unsigned short dirmax= 359;
		
		
		Sensor sensorDirecaoVento;
		sensorDirecaoVento.id=3;
		sensorDirecaoVento.sensor_type=68;
		sensorDirecaoVento.max_limit=dirmax;
		sensorDirecaoVento.min_limit=dirmin;
		sensorDirecaoVento.frequency=freqDirVento;
		sensorDirecaoVento.readings_size=sizeof(dirvento)/sizeof(unsigned long);
		sensorDirecaoVento.readings=malloc(sensorDirecaoVento.readings_size*sizeof(unsigned short));
		for(int i =0;i<sensorDirecaoVento.readings_size;i++){
			sensorDirecaoVento.readings[i]=(unsigned short) dirvento[i];
		}

		for (i; i <sizeof(dirvento); i++){
			char comp_rand = pcg32_random_r() % 50;		
		if(i!=0){
			ult_dir_vento = dirvento[i-1];
		}
		dirvento[i]= sens_dir_vento(ult_dir_vento, comp_rand);
		
		}
		for (i = 0; i <sizeof(dirvento); i++)
	{
		if (dirvento[i]> dirmax || dirvento[i]< dirmin){
			erros++;
		}
		else{
			erros=0;
		}
		if(erros==erroMaximo){
			int init = i-4;
			sensDirVento(init, freqDirVento);
		}
	}

		for(int j=0;j<sizeof(dirvento);j++){
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

		free(sensorDirecaoVento.readings);
		sensorDirecaoVento.readings=NULL;
		
}
	
	



	//sensor pluvio



void  sensHumAtm(int i, int freqHumAtm, int freqPluvio){

		
		char humdatm[3600/freqHumAtm*24];
		char pluvio[3600/freqPluvio*24];

		unsigned char humatmmin= 0;
		unsigned char humatmmax= 5;
		char comp_rand;
		
		unsigned char ult_pluvio;
		
		unsigned char ult_hmd_atm;

		Sensor sensorHumidadeAtmosferica;
		sensorHumidadeAtmosferica.id=4;
		sensorHumidadeAtmosferica.sensor_type=65;
		sensorHumidadeAtmosferica.max_limit=humatmmax;
		sensorHumidadeAtmosferica.min_limit=humatmmin;
		sensorHumidadeAtmosferica.frequency=freqHumAtm;
		sensorHumidadeAtmosferica.readings_size=sizeof(humdatm)/sizeof(unsigned long);
		sensorHumidadeAtmosferica.readings=malloc(sensorHumidadeAtmosferica.readings_size*sizeof(unsigned char));
		for(int i =0;i<sensorHumidadeAtmosferica.readings_size;i++){
			sensorHumidadeAtmosferica.readings[i]=(unsigned char) humdatm[i];
		}


	for (i; i <sizeof(humdatm); i++){
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
		for (i = 0; i <sizeof(humdatm); i++)
	{
		if (humdatm[i]> humatmmax || humdatm[i]< humatmmin){
			erros++;
		}
		else{
			erros=0;
		}
		if(erros==erroMaximo){
			int init = i-4;
			sensHumAtm(init, freqHumAtm, freqPluvio);
		}
	}
	
	for(int j=0;j<sizeof(humdatm);j++){
		soma+=humdatm[j];  //dirvento
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
		

		free(sensorHumidadeAtmosferica.readings);
		sensorHumidadeAtmosferica.readings=NULL;
		
		
}
void sensHumSolo(int i, int freqHumSolo, int freqPluvio){

		
			char humdsolo[3600/freqHumSolo*24];
			char pluvio[3600/freqPluvio*24];

			unsigned char solomin= 0;
			unsigned char solomax= 5;
			char comp_rand;
			
			unsigned char ult_humd_solo;
			unsigned char ult_pluvio=0;
			
			Sensor sensorHumidadeSolo;
			sensorHumidadeSolo.id=5;
			sensorHumidadeSolo.sensor_type=71;
			sensorHumidadeSolo.max_limit=solomax;
			sensorHumidadeSolo.min_limit=solomin;
			sensorHumidadeSolo.frequency=freqHumSolo;
			sensorHumidadeSolo.readings_size=sizeof(humdsolo)/sizeof(unsigned long);
			sensorHumidadeSolo.readings=malloc(sensorHumidadeSolo.readings_size*sizeof(unsigned char));
			for(int i =0;i<sensorHumidadeSolo.readings_size;i++){
				sensorHumidadeSolo.readings[i]=(unsigned char) humdsolo[i];
			}		
			
			for (i; i <sizeof(humdsolo); i++){
				
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
	for (i = 0; i <sizeof(humdsolo); i++)
	{
		if (humdsolo[i]> solomax || humdsolo[i]< solomin){
			erros++;
		}
		else{
			erros=0;
		}
		if(erros==erroMaximo){
			int init = i-4;
			sensHumSolo(init, freqHumSolo, freqPluvio);
		}
	}
	
	
	for(int j=0;j<sizeof(humdsolo);j++){
		soma+=humdsolo[j];
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
		
		
		free(sensorHumidadeSolo.readings);
		sensorHumidadeSolo.readings=NULL;

}


	//sensor pluvio


void sensPluvio(int i, int freqPluvio, int freqTemp){


		
		char pluvio[3600/freqPluvio*24];
		char temp[3600/freqTemp*24];
		char minPluvio=0;
		char maxPluvio=5;
		
	
		
		unsigned char ult_pluvio;
		char ult_temp;
    	
		Sensor sensPluviosidade;
		sensPluviosidade.id=6;
		sensPluviosidade.sensor_type=82;
		sensPluviosidade.max_limit=maxPluvio;
		sensPluviosidade.min_limit=minPluvio;
		sensPluviosidade.frequency=freqPluvio;
		sensPluviosidade.readings_size=sizeof(pluvio)/sizeof(unsigned long);
		sensPluviosidade.readings=malloc(sensPluviosidade.readings_size*sizeof(unsigned char));
		for(int i = 0 ;i<sensPluviosidade.readings_size;i++){
			sensPluviosidade.readings[i]=(unsigned char) pluvio[i];
		}

    	for (i; i <sizeof(pluvio); i++){
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
		for (i = 0; i <sizeof(pluvio); i++){
		if (pluvio[i]> maxPluvio || pluvio[i]< minPluvio){
			erros++;
		}
		else{
			erros=0;
		}
		if(erros==erroMaximo){
			int init = i-4;
			sensPluvio(init, freqPluvio, freqTemp);
		}
	}
	for(int j=0;j<sizeof(pluvio);j++){
		soma+=pluvio[j];
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
		
	free (sensPluviosidade.readings);
	sensPluviosidade.readings=NULL;


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
	sensTemp(0,freqTemp);
    sensVelcVento(0,freqVelcVento);
    sensDirVento(0,freqDirVento);
    sensHumAtm(0,freqHumAtm,freqPluvio);
    sensHumSolo(0,freqHumSolo,freqPluvio);
	sensPluvio(0,freqPluvio,freqTemp);
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
			


