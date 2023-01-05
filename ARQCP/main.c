#include <stdio.h>
#include <stdint.h>
#include <unistd.h>
#include <stdlib.h>
#include "struct.h"
#include "pcg32_random_r.h"
#include "sensores.h"

uint64_t state = 0;
uint64_t inc = 0;

int j = 0;

// Arrays para a US[102]
/*char temp[30];
char velcvento[30];
short dirvento[30];
char humdatm[30];
char humdsolo[30];
char pluvio[30];*/

// Inicializador de variaveis sem dependeencias
unsigned char ult_temp = 15;
unsigned char ult_velc_vento = 10;
unsigned short ult_dir_vento;

// US[104]
int erros;
int erroMaximo = 5;

// US[103]
short matriz[6][3];
short *ptrMatriz = &matriz[0][0];
short valorMinimo = 00;
short valorMaximo = 0;
short contador = 0;
short soma = 0;
short media = 0;

/* char velcvento[3600/freqVelcVento*24];
short dirvento[3600/freqDirVento*24];
char humdatm[3600/freqHumDatm*24];
char humdsolo[3600/freqHumDsolo*24];
char pluvio[3600/freqPluvio*24];*/

void sensTemp(int i, int freqTemp)
{
	char tempmin = 0;
	char tempmax = 55;
	char temp[3600 / freqTemp * 24];

<<<<<<< HEAD
	int numSensores = 0;
	printf("Quantos sensores de temperatura deseja criar? \n");
	scanf("%d", &numSensores);

	Sensor *sensTemperatura = malloc(numSensores * sizeof(Sensor));
=======

void sensTemp(int i, int freqTemp) {
  char tempmin = 0;
  char tempmax = 55;
  char temp[3600 / freqTemp * 24];
  char *ptrTemp = temp;

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
    sensTemperatura[i].readings = malloc(sensTemperatura[i].readings_size * sizeof(unsigned long));
	for(int j=0; j<sensTemperatura[i].readings_size;j++){
		sensTemperatura[i].readings[j]=*ptrTemp;
		ptrTemp++;
	}
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
    while (choice != 4) {
    printf("\nMenu:\n");
    printf("1. Generate temperature readings\n");
    printf("2. Change temperature sensor frequency\n");
    printf("3. Add temperature sensor\n");
    printf("4. Remove temperature sensor\n");
    printf("5. Quit\n");
    
    int choice;
    printf("Enter your choice: ");
    scanf("%d", &choice);

	if(choice=2){
		printf("Enter the new frequency for temperature readings: ");
		int new_freq;
        scanf("%d", &new_freq);
        
  		for (int i = 0; i < numSensores; i++) {
    	sensTemperatura[i].frequency = new_freq;
    	int old_size = sensTemperatura[i].readings_size;
    	sensTemperatura[i].readings_size = 3600 / new_freq * 24;
    	unsigned short *new_readings = malloc(sensTemperatura[i].readings_size * sizeof(unsigned short));
    	int min_size = old_size < sensTemperatura[i].readings_size ? old_size : sensTemperatura[i].readings_size;
    	for (int j = 0; j < min_size; j++) {
      	new_readings[j] = sensTemperatura[i].readings[j];
    	}

    	free(sensTemperatura[i].readings);
    	sensTemperatura[i].readings = new_readings;
  		}
		}
        
      

	
    
    
    
    if (choice == 3) {
      // Add a sensor
      numSensores++;
      sensTemperatura = realloc(sensTemperatura, numSensores * sizeof(Sensor));
      
      sensTemperatura[numSensores - 1].id = numSensores;
      sensTemperatura[numSensores - 1].sensor_type = 84;
      sensTemperatura[numSensores - 1].max_limit = tempmax;
      sensTemperatura[numSensores - 1].min_limit = tempmin;
      sensTemperatura[numSensores - 1].frequency = freqTemp;
      sensTemperatura[numSensores - 1].readings_size = 3600 / freqTemp * 24;
      sensTemperatura[numSensores - 1].readings = malloc(sensTemperatura[i].readings_size * sizeof(unsigned short));
      
      printf("Sensor added.\n");
    }



	if (choice == 4) {
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
		int sensor;
		if(numSensores>1){
		printf("Qual sensor deseja analisar? \n");
		for(int i=0;i<numSensores;i++){
			printf("Sensor %d \n", sensTemperatura[i].id);
		}
		scanf("%d", &sensor);
		while (sensor==0){
			printf("Sensor invalido, tente novamente \n");
			scanf("%d", &sensor);
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451

	for (int i = 0; i < numSensores; i++)
	{
		sensTemperatura[i].id = i + 1;
		sensTemperatura[i].sensor_type = 84;
		sensTemperatura[i].max_limit = tempmax;
		sensTemperatura[i].min_limit = tempmin;
		sensTemperatura[i].frequency = freqTemp;
		sensTemperatura[i].readings_size = 3600 / freqTemp * 24;
		sensTemperatura[i].readings = malloc(sensTemperatura->readings_size * sizeof(unsigned long));
		for (int j = 0; j < sensTemperatura->readings_size; j++)
		{
			sensTemperatura[i].readings[j] = (unsigned short)temp[j];
		}

<<<<<<< HEAD
		for (int i = 0; i < numSensores; i++)
		{

			for (int j = 0; j < sensTemperatura[i].readings_size; j++)
			{
				char comp_rand = pcg32_random_r() % 3;

				if (j != 0)
				{
					ult_temp = sensTemperatura[i - 1].readings[sensTemperatura[i - 1].readings_size - 1];
				}

				sensTemperatura[i].readings[j] = (unsigned short)sens_temp(ult_temp, comp_rand);
			}

			for (int j = 0; j < sensTemperatura[i].readings_size; j++)
			{
				if (sensTemperatura[i].readings[j] > tempmax || sensTemperatura[i].readings[j] < tempmin)
				{
					erros++;
				}
				else
				{
					erros = 0;
				}
				if (erros == erroMaximo)
				{
					int init = j - 4;
					sensTemp(init, freqTemp);
				}
			}
=======
		//meter a média e a print da matriz
	
	for (int i = 0; i < numSensores; i++) {
    free(sensTemperatura[i].readings);
  	}
  	free(sensTemperatura);
	}else{
		sensor=1;
	}
	for(int i=0;i<sensTemperatura[i].readings_size;i++){
		soma+=sensTemperatura[sensor-1].readings[i];
		contador++;
		if(sensTemperatura[sensor-1].readings[i]<valorMinimo){
			valorMinimo=sensTemperatura[sensor-1].readings[i];
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451
		}
		int choice;

		while (1)
		{
			while (choice !=6)
			{
				printf("\nMenu:\n");
				printf("1. Generate temperature readings\n");
				printf("2. Change temperature sensor frequency\n");
				printf("3. Add temperature sensor\n");
				printf("4. Remove temperature sensor\n");
				printf("5. Analyze sensor\n");
				printf("6. Quit\n");

				int choice;
				printf("Enter your choice: ");
				scanf("%d", &choice);

				// Mudar a frequencia

				if (choice = 2)
				{
					printf("Enter the new frequency for temperature readings: ");
					int new_freq;
					scanf("%d", &new_freq);

					for (int i = 0; i < numSensores; i++)
					{
						sensTemperatura[i].frequency = new_freq;
						int old_size = sensTemperatura[i].readings_size;

						sensTemperatura[i].readings_size = 3600 / new_freq * 24;
						unsigned short *new_readings = malloc(sensTemperatura[i].readings_size * sizeof(unsigned short));
						int min_size;
						if (old_size < sensTemperatura[i].readings_size)
						{
							min_size = old_size;
						}
						else
						{
							min_size = sensTemperatura[i].readings_size;
						}

						for (int j = 0; j < min_size; j++)
						{
							new_readings[j] = sensTemperatura[i].readings[j];
						}

						free(sensTemperatura[i].readings);
						sensTemperatura[i].readings = new_readings;
					}
				}

				if (choice == 3)
				{
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

				if (choice == 4)
				{
					// Remove a sensor
					if (numSensores == 0)
					{
						printf("No sensors to remove.\n");
					}
					else
					{
						printf("Enter the ID of the sensor to remove: ");
						int id;
						scanf("%d", &id);

						int found = 0;
						for (int i = 0; i < numSensores; i++)
						{
							if (sensTemperatura[i].id == id)
							{
								found = 1;
								free(sensTemperatura[i].readings);
								for (int j = i; j < numSensores - 1; j++)
								{
									sensTemperatura[j] = sensTemperatura[j + 1];
								}
								numSensores--;
								sensTemperatura = realloc(sensTemperatura, numSensores * sizeof(Sensor));
								break;
							}
						}

						if (found)
						{
							printf("Sensor removed.\n");
						}
						else
						{
							printf("Sensor not found.\n");
						}
					}
				}
				else if (choice == 3)
				{
					int sensor;
					if (numSensores > 1)
					{
						printf("Qual sensor deseja analisar? \n");
						for (int i = 0; i < numSensores; i++)
						{
							printf("Sensor %d \n", sensTemperatura[i].id);
						}
						scanf("%d", &sensor);
						while (sensor == 0)
						{
							printf("Sensor invalido, tente novamente \n");
							scanf("%d", &sensor);
						}

						// meter a média e a print da matriz

						for (int i = 0; i < numSensores; i++)
						{
							free(sensTemperatura[i].readings);
						}
						free(sensTemperatura);
					}
					else
					{
						sensor = 1;
					}
					for (int i = 0; i < sensTemperatura->readings_size; i++)
					{
						soma += sensTemperatura[sensor - 1].readings[i];
						contador++;
						if (sensTemperatura[sensor - 1].readings[i] < valorMinimo)
						{
							valorMinimo = sensTemperatura[sensor - 1].readings[i];
						}
						if (sensTemperatura[sensor - 1].readings[i] > valorMaximo)
						{
							valorMaximo = sensTemperatura[sensor - 1].readings[i];
						}
					}
					media = soma / contador;

					*ptrMatriz = valorMaximo;
					*(ptrMatriz + 1) = valorMinimo;
					*(ptrMatriz + 2) = media;

					valorMinimo = 500, valorMaximo = 0, contador = 0, media = 0, soma = 0, i = 0, j = 0;

					free(sensTemperatura->readings);
					sensTemperatura->readings = NULL;
				}
			}
		}
	}
<<<<<<< HEAD
=======
	media=soma/contador;
	
		*ptrMatriz=valorMaximo;
		*(ptrMatriz+1)=valorMinimo;
		*(ptrMatriz+2)=media;
		
		valorMinimo=500, valorMaximo=0, contador=0,media=0,soma=0, i=0,j=0;
		
		free(sensTemperatura[i].readings);
		sensTemperatura[i].readings=NULL;
  	}
	}
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451
}

		void sensVelcVento(int i, int freqVelcVento)
		{

			char velcvento[3600 / freqVelcVento * 24];
			unsigned char velcmin = 0;
			unsigned char velcmax = 150;

			int numSensores = 0;
			printf("Quantos sensores de velocidade do vento deseja criar? \n");
			scanf("%d", &numSensores);

			Sensor *sensVelocidadeVento = malloc(numSensores * sizeof(Sensor));
			sensVelocidadeVento[i].id = 2;
			sensVelocidadeVento[i].sensor_type = 87;
			sensVelocidadeVento[i].max_limit = velcmax;
			sensVelocidadeVento[i].min_limit = velcmin;
			sensVelocidadeVento[i].frequency = freqVelcVento;
			sensVelocidadeVento[i].readings_size = sizeof(velcvento) / sizeof(unsigned long);
			sensVelocidadeVento[i].readings = malloc(sensVelocidadeVento->readings_size * sizeof(unsigned short));
			for (int j = 0; j < sensVelocidadeVento->readings_size; j++)
			{
				sensVelocidadeVento[i].readings[j] = (unsigned short)velcvento[j];
			}

			for (i; i < sizeof(velcvento); i++)
			{
				char comp_rand = pcg32_random_r() % 15;
				if (i != 0)
				{
					ult_velc_vento = velcvento[i - 1];
				}
				velcvento[i] = sens_velc_vento(ult_velc_vento, comp_rand);
			}
			for (i = 0; i < sizeof(velcvento); i++)
			{
				if (velcvento[i] > velcmax || velcvento[i] < velcmin)
				{
					erros++;
				}
				else
				{
					erros = 0;
				}
				if (erros == erroMaximo)
				{
					int init = i - 4;
					sensVelcVento(init, freqVelcVento);
				}
			}
			int sensor;
			if (numSensores > 1)
			{
				printf("Qual sensor deseja analisar? \n");
				for (int i = 0; i < numSensores; i++)
				{
					printf("Sensor %d \n", sensVelocidadeVento[i].id);
				}
				scanf("%d", &sensor);
				while (sensor == 0)
				{
					printf("Sensor invalido, tente novamente \n");
					scanf("%d", &sensor);
				}
			}
			else
			{
				sensor = 2;
			}
			for (int i = 0; i < sensVelocidadeVento->readings_size; i++)
			{
				soma += sensVelocidadeVento[sensor - 1].readings[i];
				contador++;
				if (sensVelocidadeVento[sensor - 1].readings[i] < valorMinimo)
				{
					valorMinimo = sensVelocidadeVento[sensor - 1].readings[i];
				}
				if (sensVelocidadeVento[sensor - 1].readings[i] > valorMaximo)
				{
					valorMaximo = sensVelocidadeVento[sensor - 1].readings[i];
				}
			}

<<<<<<< HEAD
			media = soma / contador;
=======
void sensVelcVento(int i, int freqVelcVento){
	
	
	char velcvento[3600/freqVelcVento*24];
	unsigned char velcmin= 0;
	unsigned char velcmax= 150;
	char *ptrVelcVento=velcvento;
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451

			*(ptrMatriz + 3) = valorMaximo;
			*(ptrMatriz + 4) = valorMinimo;
			*(ptrMatriz + 5) = media;

<<<<<<< HEAD
			valorMinimo = 500, valorMaximo = 0, contador = 0, media = 0, soma = 0, i = 0, j = 0;
=======
	Sensor *sensVelocidadeVento=malloc(numSensores * sizeof(Sensor));
	for(int i=0;i<numSensores;i++){
	sensVelocidadeVento[i].id=2;
	sensVelocidadeVento[i].sensor_type=87;
	sensVelocidadeVento[i].max_limit=velcmax;
	sensVelocidadeVento[i].min_limit=velcmin;
	sensVelocidadeVento[i].frequency=freqVelcVento;
	sensVelocidadeVento[i].readings_size=sizeof(velcvento)/sizeof(unsigned long);
	sensVelocidadeVento[i].readings=malloc(sensVelocidadeVento[i].readings_size*sizeof(unsigned short));
	for(int j=0;j<sensVelocidadeVento[i].readings_size;j++){
		sensVelocidadeVento[i].readings[j]=*ptrVelcVento;
		ptrVelcVento++;
	} 
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
	int sensor;
	if(numSensores>1){
		printf("Qual sensor deseja analisar? \n");
		for(int i=0;i<numSensores;i++){
			printf("Sensor %d \n", sensVelocidadeVento[i].id);
		}
		scanf("%d", &sensor);
		while (sensor==0){
			printf("Sensor invalido, tente novamente \n");
			scanf("%d", &sensor);
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451

			free(sensVelocidadeVento->readings);
			sensVelocidadeVento->readings = NULL;
		}
<<<<<<< HEAD
=======
	}else{
		sensor=2;
	}
	for(int i=0;i<sensVelocidadeVento[i].readings_size;i++){
		soma+=sensVelocidadeVento[sensor-1].readings[i];
		contador++;
		if(sensVelocidadeVento[sensor-1].readings[i]<valorMinimo){
			valorMinimo=sensVelocidadeVento[sensor-1].readings[i];
		}
		if(sensVelocidadeVento[sensor-1].readings[i]>valorMaximo){
			valorMaximo=sensVelocidadeVento[sensor-1].readings[i];
		}
	}

	media=soma/contador;
	
		*(ptrMatriz+3)=valorMaximo;
		*(ptrMatriz+4)=valorMinimo;
		*(ptrMatriz+5)=media;

		valorMinimo=500, valorMaximo=0, contador=0,media=0,soma=0, i=0,j=0;
		
		free(sensVelocidadeVento[i].readings);
		sensVelocidadeVento[i].readings=NULL;
		

}
		
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451

void sensDirVento(int i, int freqDirVento)
		{

			char dirvento[3600 / freqDirVento * 24];
			unsigned short dirmin = 0;
			unsigned short dirmax = 359;

			int numSensores = 0;
			printf("Quantos sensores de direcao do vento deseja criar? \n");
			scanf("%d", &numSensores);

			Sensor *sensorDirecaoVento = malloc(numSensores * sizeof(Sensor));
			sensorDirecaoVento[i].id = 3;
			sensorDirecaoVento[i].sensor_type = 68;
			sensorDirecaoVento[i].max_limit = dirmax;
			sensorDirecaoVento[i].min_limit = dirmin;
			sensorDirecaoVento[i].frequency = freqDirVento;
			sensorDirecaoVento[i].readings_size = sizeof(dirvento) / sizeof(unsigned long);
			sensorDirecaoVento[i].readings = malloc(sensorDirecaoVento->readings_size * sizeof(unsigned short));
			for (int j = 0; j < sensorDirecaoVento->readings_size; j++)
			{
				sensorDirecaoVento[i].readings[j] = (unsigned short)dirvento[j];
			}

			for (i; i < sizeof(dirvento); i++)
			{
				char comp_rand = pcg32_random_r() % 50;
				if (i != 0)
				{
					ult_dir_vento = dirvento[i - 1];
				}
				dirvento[i] = sens_dir_vento(ult_dir_vento, comp_rand);
			}
			for (i = 0; i < sizeof(dirvento); i++)
			{
				if (dirvento[i] > dirmax || dirvento[i] < dirmin)
				{
					erros++;
				}
				else
				{
					erros = 0;
				}
				if (erros == erroMaximo)
				{
					int init = i - 4;
					sensDirVento(init, freqDirVento);
				}
			}
			int sensor;
			if (numSensores > 1)
			{
				printf("Qual sensor deseja analisar? \n");
				for (int i = 0; i < numSensores; i++)
				{
					printf("Sensor %d \n", sensorDirecaoVento[i].id);
				}
				scanf("%d", &sensor);
				while (sensor == 0)
				{
					printf("Sensor invalido, tente novamente \n");
					scanf("%d", &sensor);
				}
			}
			else
			{
				sensor = 3;
			}
			for (int i = 0; i < sensorDirecaoVento->readings_size; i++)
			{
				soma += sensorDirecaoVento[sensor - 1].readings[i];
				contador++;
				if (sensorDirecaoVento[sensor - 1].readings[i] < valorMinimo)
				{
					valorMinimo = sensorDirecaoVento[sensor - 1].readings[i];
				}
				if (sensorDirecaoVento[sensor - 1].readings[i] > valorMaximo)
				{
					valorMaximo = sensorDirecaoVento[sensor - 1].readings[i];
				}
			}
			media = soma / contador;

<<<<<<< HEAD
			*(ptrMatriz + 6) = valorMaximo;
			*(ptrMatriz + 7) = valorMinimo;
			*(ptrMatriz + 8) = media;

			valorMinimo = 500, valorMaximo = 0, contador = 0, media = 0, soma = 0, i = 0, j = 0;
=======
	
		char dirvento[3600/freqDirVento*24];
		unsigned short dirmin= 0;
		unsigned short dirmax= 359;
		char *ptrDirVento=dirvento;
		
		int numSensores=0;
		printf("Quantos sensores de direcao do vento deseja criar? \n");
		scanf("%d", &numSensores);
		
		Sensor *sensorDirecaoVento=malloc(numSensores * sizeof(Sensor));
		for(int i=0;i<numSensores;i++){
		sensorDirecaoVento[i].id=3;
		sensorDirecaoVento[i].sensor_type=68;
		sensorDirecaoVento[i].max_limit=dirmax;
		sensorDirecaoVento[i].min_limit=dirmin;
		sensorDirecaoVento[i].frequency=freqDirVento;
		sensorDirecaoVento[i].readings_size=sizeof(dirvento)/sizeof(unsigned long);
		sensorDirecaoVento[i].readings=malloc(sensorDirecaoVento[i].readings_size*sizeof(unsigned short));
		for(int j=0;j<sensorDirecaoVento[i].readings_size;j++){
			sensorDirecaoVento[i].readings[j]=*ptrDirVento;
			ptrDirVento++;
		}
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
		int sensor;
	if(numSensores>1){
		printf("Qual sensor deseja analisar? \n");
		for(int i=0;i<numSensores;i++){
			printf("Sensor %d \n", sensorDirecaoVento[i].id);
		}
		scanf("%d", &sensor);
		while (sensor==0){
			printf("Sensor invalido, tente novamente \n");
			scanf("%d", &sensor);
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451

			free(sensorDirecaoVento->readings);
			sensorDirecaoVento->readings = NULL;
		}
<<<<<<< HEAD
=======
	}else{
		sensor=3;
	}
	for(int i=0; i<sensorDirecaoVento[i].readings_size;i++){
		soma+=sensorDirecaoVento[sensor-1].readings[i];
		contador++;
		if(sensorDirecaoVento[sensor-1].readings[i]<valorMinimo){
			valorMinimo=sensorDirecaoVento[sensor-1].readings[i];
		}
		if(sensorDirecaoVento[sensor-1].readings[i]>valorMaximo){
			valorMaximo=sensorDirecaoVento[sensor-1].readings[i];
		}
	}
	media=soma/contador;
	
		*(ptrMatriz+6)=valorMaximo;
		*(ptrMatriz+7)=valorMinimo;
		*(ptrMatriz+8)=media;
		
		valorMinimo=500, valorMaximo=0, contador=0,media=0,soma=0, i=0,j=0;

		free(sensorDirecaoVento[i].readings);
		sensorDirecaoVento[i].readings=NULL;
		
}
	
	

>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451

		// sensor pluvio

		void sensHumAtm(int i, int freqHumAtm, int freqPluvio)
		{

			char humdatm[3600 / freqHumAtm * 24];
			char pluvio[3600 / freqPluvio * 24];

			unsigned char humatmmin = 0;
			unsigned char humatmmax = 5;
			char comp_rand;

			unsigned char ult_pluvio;

<<<<<<< HEAD
			unsigned char ult_hmd_atm;
=======
		
		char humdatm[3600/freqHumAtm*24];
		char pluvio[3600/freqPluvio*24];
		char *ptrHumAtm=humdatm;
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451

			int numSensores = 0;
			printf("Quantos sensores de humidade atmosferica deseja criar? \n");
			scanf("%d", &numSensores);

			Sensor *sensorHumidadeAtmosferica = malloc(numSensores * sizeof(Sensor));
			sensorHumidadeAtmosferica[i].id = 4;
			sensorHumidadeAtmosferica[i].sensor_type = 65;
			sensorHumidadeAtmosferica[i].max_limit = humatmmax;
			sensorHumidadeAtmosferica[i].min_limit = humatmmin;
			sensorHumidadeAtmosferica[i].frequency = freqHumAtm;
			sensorHumidadeAtmosferica[i].readings_size = sizeof(humdatm) / sizeof(unsigned long);
			sensorHumidadeAtmosferica[i].readings = malloc(sensorHumidadeAtmosferica->readings_size * sizeof(unsigned char));
			for (int j = 0; j < sensorHumidadeAtmosferica->readings_size; j++)
			{
				sensorHumidadeAtmosferica[i].readings[j] = (unsigned char)humdatm[j];
			}

<<<<<<< HEAD
			for (i; i < sizeof(humdatm); i++)
			{
				ult_pluvio = pluvio[i];

				if (ult_pluvio == 0)
				{
					comp_rand = pcg32_random_r() % 10;
				}
				else
				{
					comp_rand = pcg32_random_r() % 2;
				}
=======
		Sensor *sensorHumidadeAtmosferica=malloc(numSensores * sizeof(Sensor));
		for(int i=0;i<numSensores;i++){
		sensorHumidadeAtmosferica[i].id=4;
		sensorHumidadeAtmosferica[i].sensor_type=65;
		sensorHumidadeAtmosferica[i].max_limit=humatmmax;
		sensorHumidadeAtmosferica[i].min_limit=humatmmin;
		sensorHumidadeAtmosferica[i].frequency=freqHumAtm;
		sensorHumidadeAtmosferica[i].readings_size=sizeof(humdatm)/sizeof(unsigned long);
		sensorHumidadeAtmosferica[i].readings=malloc(sensorHumidadeAtmosferica[i].readings_size*sizeof(unsigned char));
		for(int j=0;j<sensorHumidadeAtmosferica[i].readings_size;j++){
			sensorHumidadeAtmosferica[i].readings[j]=*ptrHumAtm;
			ptrHumAtm++;
		}
		}
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451

				if (i != 0)
				{
					ult_hmd_atm = humdatm[i - 1];
				}

				humdatm[i] = sens_humd_atm(ult_hmd_atm, ult_pluvio, comp_rand);
			}
			for (i = 0; i < sizeof(humdatm); i++)
			{
				if (humdatm[i] > humatmmax || humdatm[i] < humatmmin)
				{
					erros++;
				}
				else
				{
					erros = 0;
				}
				if (erros == erroMaximo)
				{
					int init = i - 4;
					sensHumAtm(init, freqHumAtm, freqPluvio);
				}
			}
			int sensor;
			if (numSensores > 1)
			{
				printf("Qual sensor deseja analisar? \n");
				for (int i = 0; i < numSensores; i++)
				{
					printf("Sensor %d \n", sensorHumidadeAtmosferica[i].id);
				}
				scanf("%d", &sensor);
				while (sensor == 0)
				{
					printf("Sensor invalido, tente novamente \n");
					scanf("%d", &sensor);
				}
			}
			else
			{
				sensor = 4;
			}

			for (int i = 0; i < sensorHumidadeAtmosferica->readings_size; i++)
			{
				soma += sensorHumidadeAtmosferica[sensor - 1].readings[i];
				contador++;
				if (sensorHumidadeAtmosferica[sensor - 1].readings[i] < valorMinimo)
				{
					valorMinimo = sensorHumidadeAtmosferica[sensor - 1].readings[i];
				}
				if (sensorHumidadeAtmosferica[sensor - 1].readings[i] > valorMaximo)
				{
					valorMaximo = sensorHumidadeAtmosferica[sensor - 1].readings[i];
				}
			}
			media = soma / contador;

			*(ptrMatriz + 9) = valorMaximo;
			*(ptrMatriz + 10) = valorMinimo;
			*(ptrMatriz + 11) = media;

			valorMinimo = 500, valorMaximo = 0, contador = 0, media = 0, soma = 0, i = 0, j = 0;

			free(sensorHumidadeAtmosferica->readings);
			sensorHumidadeAtmosferica->readings = NULL;
		}
<<<<<<< HEAD
		void sensHumSolo(int i, int freqHumSolo, int freqPluvio)
		{

			char humdsolo[3600 / freqHumSolo * 24];
			char pluvio[3600 / freqPluvio * 24];
=======
	}else{
		sensor=4;
	}
	
	for(int i=0; i<sensorHumidadeAtmosferica[i].readings_size;i++){
		soma+=sensorHumidadeAtmosferica[sensor-1].readings[i];
		contador++;
		if(sensorHumidadeAtmosferica[sensor-1].readings[i]<valorMinimo){
			valorMinimo=sensorHumidadeAtmosferica[sensor-1].readings[i];
		}
		if(sensorHumidadeAtmosferica[sensor-1].readings[i]>valorMaximo){
			valorMaximo=sensorHumidadeAtmosferica[sensor-1].readings[i];
		}
	}
	media=soma/contador;
	
		*(ptrMatriz+9)=valorMaximo;
		*(ptrMatriz+10)=valorMinimo;
		*(ptrMatriz+11)=media;
		
		valorMinimo=500, valorMaximo=0, contador=0,media=0,soma=0, i=0,j=0;
		

		free(sensorHumidadeAtmosferica[i].readings);
		sensorHumidadeAtmosferica[i].readings=NULL;
		
		
}
void sensHumSolo(int i, int freqHumSolo, int freqPluvio){

		
			char humdsolo[3600/freqHumSolo*24];
			char pluvio[3600/freqPluvio*24];
			char *ptrHumSolo=humdsolo;
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451

			unsigned char solomin = 0;
			unsigned char solomax = 5;
			char comp_rand;

			unsigned char ult_humd_solo;
			unsigned char ult_pluvio = 0;

			int numSensores = 0;
			printf("Quantos sensores de humidade do solo deseja criar? \n");
			scanf("%d", &numSensores);
<<<<<<< HEAD
=======
			
			Sensor *sensorHumidadeSolo=malloc(numSensores * sizeof(Sensor));
			for(int i=0;i<numSensores;i++){
				sensorHumidadeSolo[i].id=i+1;
				sensorHumidadeSolo[i].sensor_type=71;
				sensorHumidadeSolo[i].max_limit=solomax;
				sensorHumidadeSolo[i].min_limit=solomin;
				sensorHumidadeSolo[i].frequency=freqHumSolo;
				sensorHumidadeSolo[i].readings_size=sizeof(humdsolo)/sizeof(unsigned long);
				sensorHumidadeSolo[i].readings=malloc(sensorHumidadeSolo[i].readings_size*sizeof(unsigned char));
				for(int j =0;j<sensorHumidadeSolo[i].readings_size;j++){
					sensorHumidadeSolo[i].readings[j]=*ptrHumSolo;
					ptrHumSolo++;
				}
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
	int sensor;
	if(numSensores>1){
		printf("Qual o sensor de humidade do solo que deseja consultar? \n");
		for(int i=0;i<numSensores;i++){
			printf("Sensor %d \n", sensorHumidadeSolo[i].id);
		}
		scanf("%d", &sensor);
		while (sensor==0){
			printf("Sensor invalido, tente novamente \n");
			scanf("%d", &sensor);
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451

			Sensor *sensorHumidadeSolo = malloc(numSensores * sizeof(Sensor));
			sensorHumidadeSolo[i].id = 5;
			sensorHumidadeSolo[i].sensor_type = 71;
			sensorHumidadeSolo[i].max_limit = solomax;
			sensorHumidadeSolo[i].min_limit = solomin;
			sensorHumidadeSolo[i].frequency = freqHumSolo;
			sensorHumidadeSolo[i].readings_size = sizeof(humdsolo) / sizeof(unsigned long);
			sensorHumidadeSolo[i].readings = malloc(sensorHumidadeSolo->readings_size * sizeof(unsigned char));
			for (int i = 0; i < sensorHumidadeSolo->readings_size; i++)
			{
				sensorHumidadeSolo[j].readings[i] = (unsigned char)humdsolo[i];
			}
<<<<<<< HEAD
=======
	}
	media=soma/contador;
	
		*(ptrMatriz+12)=valorMaximo;
		*(ptrMatriz+13)=valorMinimo;
		*(ptrMatriz+14)=media;
		
		valorMinimo=500, valorMaximo=0, contador=0,media=0,soma=0, i=0,j=0;
		
		
		free(sensorHumidadeSolo[i].readings);
		sensorHumidadeSolo[i].readings=NULL;
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451

			for (i; i < sizeof(humdsolo); i++)
			{

				ult_pluvio = pluvio[i];

				if (ult_pluvio == 0)
				{

					comp_rand = pcg32_random_r() % 10;
				}
				else
				{
					comp_rand = pcg32_random_r() % 2;
				}

				if (i != 0)
				{

					ult_humd_solo = humdsolo[i - 1];
				}
				humdsolo[i] = sens_humd_solo(ult_humd_solo, ult_pluvio, comp_rand);
			}
			for (i = 0; i < sizeof(humdsolo); i++)
			{
				if (humdsolo[i] > solomax || humdsolo[i] < solomin)
				{
					erros++;
				}
				else
				{
					erros = 0;
				}
				if (erros == erroMaximo)
				{
					int init = i - 4;
					sensHumSolo(init, freqHumSolo, freqPluvio);
				}
			}
			int sensor;
			if (numSensores > 1)
			{
				printf("Qual o sensor de humidade do solo que deseja consultar? \n");
				for (int i = 0; i < numSensores; i++)
				{
					printf("Sensor %d \n", sensorHumidadeSolo[i].id);
				}
				scanf("%d", &sensor);
				while (sensor == 0)
				{
					printf("Sensor invalido, tente novamente \n");
					scanf("%d", &sensor);
				}
			}
			else
			{
				sensor = 5;
			}

<<<<<<< HEAD
			for (i = 0; i < sizeof(humdsolo); i++)
			{
				soma += sensorHumidadeSolo[sensor - 1].readings[i];
				contador++;
				if (sensorHumidadeSolo[sensor - 1].readings[i] < valorMinimo)
				{
					valorMinimo = sensorHumidadeSolo[sensor - 1].readings[i];
				}
				if (sensorHumidadeSolo[sensor - 1].readings[i] > valorMaximo)
				{
					valorMaximo = sensorHumidadeSolo[sensor - 1].readings[i];
				}
			}
			media = soma / contador;

			*(ptrMatriz + 12) = valorMaximo;
			*(ptrMatriz + 13) = valorMinimo;
			*(ptrMatriz + 14) = media;
=======
		
		char pluvio[3600/freqPluvio*24];
		char temp[3600/freqTemp*24];
		char *ptrPluvio=pluvio;
		char minPluvio=0;
		char maxPluvio=5;
		
	
		
		unsigned char ult_pluvio;
		char ult_temp;
    	int numSensores=0;
		printf("Quantos sensores de pluviosidade deseja criar? \n");
		scanf("%d", &numSensores);

		Sensor *sensPluviosidade=malloc(numSensores*sizeof(Sensor));
		for(int i=0;i<numSensores;i++){
			sensPluviosidade[i].id=i+1;
			sensPluviosidade[i].sensor_type=82;
			sensPluviosidade[i].max_limit=maxPluvio;
			sensPluviosidade[i].min_limit=minPluvio;
			sensPluviosidade[i].frequency=freqPluvio;
			sensPluviosidade[i].readings_size=sizeof(pluvio)/sizeof(unsigned long);
			sensPluviosidade[i].readings=malloc(sensPluviosidade[i].readings_size*sizeof(unsigned char));
			for(int j=0 ;j<sensPluviosidade[i].readings_size;i++){
				sensPluviosidade[i].readings[j]=*ptrPluvio;
				ptrPluvio++;
			}
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
			
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451

			valorMinimo = 500, valorMaximo = 0, contador = 0, media = 0, soma = 0, i = 0, j = 0;

			free(sensorHumidadeSolo->readings);
			sensorHumidadeSolo->readings = NULL;
		}
<<<<<<< HEAD
=======
	}else{
		sensor=6;
	}
	for (i = 0; i <sizeof(pluvio); i++)
		{
			soma+=sensPluviosidade[sensor-1].readings[i];
			contador++;
			if(sensPluviosidade[sensor-1].readings[i]<valorMinimo){
				valorMinimo=sensPluviosidade[sensor-1].readings[i];
			}
			if(sensPluviosidade[sensor-1].readings[i]>valorMaximo){
				valorMaximo=sensPluviosidade[sensor-1].readings[i];
			}
	}
	media=soma/contador;
	
		*(ptrMatriz+15)=valorMaximo;
		*(ptrMatriz+16)=valorMinimo;
		*(ptrMatriz+17)=media;
		
		valorMinimo=500, valorMaximo=0, contador=0,media=0,soma=0, i=0,j=0;
		
	free (sensPluviosidade[i].readings);
	sensPluviosidade[i].readings=NULL;
>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451

		// sensor pluvio

		void sensPluvio(int i, int freqPluvio, int freqTemp)
		{

			char pluvio[3600 / freqPluvio * 24];
			char temp[3600 / freqTemp * 24];
			char minPluvio = 0;
			char maxPluvio = 5;

<<<<<<< HEAD
			unsigned char ult_pluvio;
			char ult_temp;
			int numSensores = 0;
			printf("Quantos sensores de pluviosidade deseja criar? \n");
			scanf("%d", &numSensores);

			Sensor *sensPluviosidade;
			sensPluviosidade[i].id = 6;
			sensPluviosidade[i].sensor_type = 82;
			sensPluviosidade[i].max_limit = maxPluvio;
			sensPluviosidade[i].min_limit = minPluvio;
			sensPluviosidade[i].frequency = freqPluvio;
			sensPluviosidade[i].readings_size = sizeof(pluvio) / sizeof(unsigned long);
			sensPluviosidade[i].readings = malloc(sensPluviosidade->readings_size * sizeof(unsigned char));
			for (int j = 0; j < sensPluviosidade->readings_size; i++)
			{
				sensPluviosidade[i].readings[j] = (unsigned char)pluvio[j];
			}

			for (i; i < sizeof(pluvio); i++)
			{
				char ult_temp = temp[i];
				// sleep(k); //criar função para dar output de x em x segundos
				char comp_rand = pcg32_random_r() % 5; // mudar a alteração de temp
				// componente aleatoria que gera um numero random, alteração
				int comp_relative = pcg32_random_r() % ult_temp;
				//
				if (comp_relative != 0)
				{
					comp_rand = 0;
				}
				// O valor só altera se a componente gerar o valor 0
				if (i != 0)
				{
					ult_pluvio = pluvio[i - 1];
				}
				// o ultimo valor da pluviosidade é a posição anterior para onde o apontador está a apontar
				pluvio[i] = sens_pluvio(ult_pluvio, ult_temp, comp_rand);
			}
			for (i = 0; i < sizeof(pluvio); i++)
			{
				if (pluvio[i] > maxPluvio || pluvio[i] < minPluvio)
				{
					erros++;
				}
				else
				{
					erros = 0;
				}
				if (erros == erroMaximo)
				{
					int init = i - 4;
					sensPluvio(init, freqPluvio, freqTemp);
				}
			}
			int sensor;
			if (numSensores > 1)
			{
				printf("Qual o sensor de pluviosidade que deseja consultar? \n");
				for (int i = 0; i < numSensores; i++)
				{
					printf("Sensor %d \n", sensPluviosidade[i].id);
				}
				scanf("%d", &sensor);
				while (sensor == 0)
				{
					printf("Sensor invalido, tente novamente \n");
					scanf("%d", &sensor);
				}
			}
			else
			{
				sensor = 6;
			}
			for (i = 0; i < sizeof(pluvio); i++)
			{
				soma += sensPluviosidade[sensor - 1].readings[i];
				contador++;
				if (sensPluviosidade[sensor - 1].readings[i] < valorMinimo)
				{
					valorMinimo = sensPluviosidade[sensor - 1].readings[i];
				}
				if (sensPluviosidade[sensor - 1].readings[i] > valorMaximo)
				{
					valorMaximo = sensPluviosidade[sensor - 1].readings[i];
				}
			}
			media = soma / contador;

			*(ptrMatriz + 15) = valorMaximo;
			*(ptrMatriz + 16) = valorMinimo;
			*(ptrMatriz + 17) = media;

			valorMinimo = 500, valorMaximo = 0, contador = 0, media = 0, soma = 0, i = 0, j = 0;

			free(sensPluviosidade->readings);
			sensPluviosidade->readings = NULL;
		}

		int main()
		{
			uint32_t buffer[64];
			FILE *f;
			int result;
			f = fopen("/dev/urandom", "r");
			if (f == NULL)
			{
				printf("Error: open() failed to open /dev/random for reading\n");
				return 1;
			}
			result = fread(buffer, sizeof(uint32_t), 64, f);
			fread(&state, sizeof(uint64_t), 1, f);
			fread(&inc, sizeof(uint64_t), 1, f);
			if (result < 1)
			{
				printf("Error , failed to read and words\n");
				return 1;
			}

			printf("Qual a frequencia de leitura dos sensor de temperatura? (em segundos)\n");
			int freqTemp;
			scanf("%d", &freqTemp);
			printf("Qual a frequencia de leitura dos sensor de velocidade do vento? (em segundos)\n");
			int freqVelcVento;
			scanf("%d", &freqVelcVento);
			printf("Qual a frequencia de leitura dos sensor de direção do vento? (em segundos)\n");
			int freqDirVento;
			scanf("%d", &freqDirVento);
			printf("Qual a frequencia de leitura dos sensor de humidade atmosférica? (em segundos)\n");
			int freqHumAtm;
			scanf("%d", &freqHumAtm);
			printf("Qual a frequencia de leitura dos sensor de humidade do solo? (em segundos)\n");
			int freqHumSolo;
			scanf("%d", &freqHumSolo);
			printf("Qual a frequencia de leitura dos sensor de pluviosidade? (em segundos)\n");
			int freqPluvio;
			scanf("%d", &freqPluvio);
			sensTemp(0, freqTemp);
			sensVelcVento(0, freqVelcVento);
			sensDirVento(0, freqDirVento);
			sensHumAtm(0, freqHumAtm, freqPluvio);
			sensHumSolo(0, freqHumSolo, freqPluvio);
			sensPluvio(0, freqPluvio, freqTemp);
			printf("\t\t\tValor Mínimo\tValor Máximo\tMédia dos valores");
			printf("\n");
			for (int i = 0; i < 6; i++)
			{
				switch (i)
				{
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
				for (int j = 0; j < 3; j++)
				{
					printf("%10d\t", matriz[i][j]);
				}
				printf("\n");
			}
		}
	
=======
}

>>>>>>> e225b01be739dbe1273a4769585f82d34a82e451
