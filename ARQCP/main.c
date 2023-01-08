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
unsigned char ult_velc_vento = 10;
unsigned short ult_dir_vento;

// US[104]
int erros;
int erroMaximo = 5;

// US[103]
short matriz[6][3];
short *ptrMatriz = &matriz[0][0];
short valorMinimo = 500;
short valorMaximo = 0;
short contador = 0;
short soma = 0;
short media = 0;

/* char velcvento[3600/freqVelcVento*24];
short dirvento[3600/freqDirVento*24];
char humdatm[3600/freqHumDatm*24];
char humdsolo[3600/freqHumDsolo*24];
char pluvio[3600/freqPluvio*24];*/

int seed()
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
	if (result < 1)
	{
		printf("Error , failed to read and words\n");
		return 1;
	}
	fread(&state, sizeof(uint64_t), 1, f);
	fread(&inc, sizeof(uint64_t), 1, f);
	return 0;
}

void sensTemp(int i, int freqTemp)
{
	char ult_temp = 15;
	char tempmin = 0;
	char tempmax = 25;
	

	int numSensores;
	do
	{
		printf("Quantos sensores de temperatura deseja criar? \n");
		scanf("%d", &numSensores);
		if (numSensores < 0)
		{
			printf("Numero invalido, tente novamente \n");
		}
	} while (numSensores < 0);

	Sensor *sensTemperatura = malloc(numSensores * sizeof(Sensor));

	for (int i = 0; i < numSensores; i++)
	{
		sensTemperatura[i].id = i + 1;
		sensTemperatura[i].sensor_type = 84;
		sensTemperatura[i].max_limit = tempmax;
		sensTemperatura[i].min_limit = tempmin;
		sensTemperatura[i].frequency = freqTemp;
		sensTemperatura[i].readings_size = (3600 / freqTemp * 24);
		sensTemperatura[i].readings = malloc(sensTemperatura[i].readings_size * sizeof(unsigned short));
		for (int j = 0; j < sensTemperatura[i].readings_size; j++)
		{
			char comp_rand = pcg32_random_r() % 3;

			if (j != 0)
			{
				// ult_temp = sensTemperatura[i - 1].readings[sensTemperatura[i - 1].readings_size - 1]; Quero ir buscar o sensor atual?
				ult_temp = sensTemperatura[i].readings[j - 1];
			}

			sensTemperatura[i].readings[j] = (char)sens_temp(ult_temp, comp_rand);

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
				// Se eu passar o J não estou a incializar de novo?
				// Como é que eu quando encontro um erro recuo 4 leituras e volto a gerar

				j = j - 4;
				seed();
			}
			// printf("%d\n", sensTemperatura[i].readings[j]);
			// printf("%ld", sensTemperatura[i].readings_size);
		}
	}
	int choice;
	while (1)
	{
		printf("\nMenu:\n");
		printf("1. Change temperature sensor frequency\n");
		printf("2. Add temperature sensor\n");
		printf("3. Remove temperature sensor\n");
		printf("4. Analyze sensor\n");

		int choice;
		printf("Enter your choice: ");
		scanf("%d", &choice);

		// Mudar a frequencia

		if (choice == 1)
		{
			printf("Enter the new frequency for temperature readings: ");
			int new_freq;
			scanf("%d", &new_freq);

			for (int i = 0; i < numSensores; i++)
			{
				sensTemperatura[i].frequency = new_freq;
				int old_size = sensTemperatura[i].readings_size;

				sensTemperatura[i].readings_size = (3600 / new_freq * 24) / sizeof(unsigned long);

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
		else if (choice == 2)
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
			sensTemperatura[numSensores - 1].readings = malloc(sensTemperatura[i].readings_size * sizeof(unsigned short));
			for (int j = 0; j < sensTemperatura[i].readings_size; j++)
			{
				char comp_rand = pcg32_random_r() % 3;

				if (j != 0)
				{
					// ult_temp = sensTemperatura[i - 1].readings[sensTemperatura[i - 1].readings_size - 1]; Quero ir buscar o sensor atual?
					ult_temp = sensTemperatura[i].readings[j - 1];
				}

				sensTemperatura[numSensores - 1].readings[j] = (char)sens_temp(ult_temp, comp_rand);
				// printf("%d\n", sensTemperatura[i].readings[j]);

				if (sensTemperatura[numSensores - 1].readings[j] > tempmax || sensTemperatura[numSensores - 1].readings[j] < tempmin)
				{
					erros++;
				}
				else
				{
					erros = 0;
				}
				if (erros == erroMaximo)
				{
					j = j - 4;
					seed();
				}
			}
			printf("Sensor added.\n");
		}
		else if (choice == 3)
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
		else if (choice == 4)
		{
			break;
		}
		else
		{
			printf("Opção inválida. \n");
		}
	}
	int opcao;
	if (numSensores > 1)
	{
		printf("Qual sensor deseja analisar? \n");
		for (int i = 0; i < numSensores; i++)
		{
			printf("Sensor %d \n", sensTemperatura[i].id);
		}
		scanf("%d", &opcao);
		while (opcao == 0)
		{
			printf("Sensor invalido, tente novamente \n");
			scanf("%d", &opcao);
		}
	}
	else
	{
		opcao = 1;
	}
	FILE *ficheiro = fopen("leituras.csv", "w");
	fprintf(ficheiro, "Sensor de Temperatura: \n");
	for (int i = 0; i < numSensores; i++)
	{
		fprintf(ficheiro, "Sensor %d: \n", sensTemperatura[i].id);
		// printf("%ld", sensTemperatura[i].readings_size);
		for (int j = 0; i < sensTemperatura[i].readings[j]; j++)
		{
			fprintf(ficheiro, "%d, ", sensTemperatura[i].readings[j]);
		}
		fprintf(ficheiro, "\n");
	}
	fclose(ficheiro);

	for (int j = 0; j < numSensores; j++)
	{
		for (int i = 0; i < sensTemperatura[j].readings_size; i++)
		{
			soma += sensTemperatura[j].readings[i];
			contador++;
			if (sensTemperatura[j].readings[i] < valorMinimo)
			{
				valorMinimo = sensTemperatura[j].readings[i];
			}
			if (sensTemperatura[j].readings[i] > valorMaximo)
			{
				valorMaximo = sensTemperatura[j].readings[i];
			}
		}
	}

	media = soma / contador;

	*ptrMatriz = valorMaximo;
	*(ptrMatriz + 1) = valorMinimo;
	*(ptrMatriz + 2) = media;

	valorMinimo = 500, valorMaximo = 0, contador = 0, media = 0, soma = 0, i = 0, j = 0;

	for (int i = 0; i < numSensores; i++)
	{
		free(sensTemperatura[i].readings);
		sensTemperatura[i].readings = NULL;
	}
}

void sensVelcVento(int i, int freqVelcVento)
{

	char ult_velc_vento=50;
	unsigned char velcmin = 0;
	unsigned char velcmax = 150;

	int numSensores = 0;
	do
	{
		printf("Quantos sensores de velocidade do vento deseja criar? \n");
		scanf("%d", &numSensores);
		if (numSensores < 0)
		{
			printf("Numero invalido, tente novamente \n");
		}
	} while (numSensores < 0);

	Sensor *sensVelocidadeVento = malloc(numSensores * sizeof(Sensor));
	for (int i = 0; i < numSensores; i++)
	{
		sensVelocidadeVento[i].id = i + 2;
		sensVelocidadeVento[i].sensor_type = 87;
		sensVelocidadeVento[i].max_limit = velcmax;
		sensVelocidadeVento[i].min_limit = velcmin;
		sensVelocidadeVento[i].frequency = freqVelcVento;
		sensVelocidadeVento[i].readings_size = (3600 / freqVelcVento * 24);
		sensVelocidadeVento[i].readings = malloc(sensVelocidadeVento[i].readings_size * sizeof(unsigned short));
		for (int j = 0; j < sensVelocidadeVento[i].readings_size; j++)
		{
			char comp_rand = pcg32_random_r() % 3;
			if (j != 0)
			{
				ult_velc_vento = sensVelocidadeVento[i].readings[j - 1];
			}
			sensVelocidadeVento[i].readings[j] = (unsigned char) sens_velc_vento(ult_velc_vento, comp_rand);

			if (sensVelocidadeVento[i].readings[j] > velcmax || sensVelocidadeVento[i].readings[j] < velcmin)
			{
				erros++;
			}
			else
			{
				erros = 0;
			}
			if (erros = erroMaximo)
			{
				j = j - 4;

				seed();
			}
		}
	}
	int choice;
	while (1)
	{

		printf("\nMenu de Sensores de Velocidade do Vento\n");
		printf("1. Mudar a frequencia de leitura dos sensores de velocidade do vento\n");
		printf("2. Adicionar um sensor de velocidade do vento\n");
		printf("3. Remover um sensor de velocidade do vento\n");
		printf("4. Analisar os dados de um sensor de velocidade do vento\n");
		printf("5. Sair\n");

		int choice;
		printf("Escolha uma opção: ");
		scanf("%d", &choice);

		if (choice == 1)
		{
			printf("Insira a nova frequencia de leitura do sensor de velocidade de vento: ");
			int new_freq;
			scanf("%d", &new_freq);

			for (int i = 0; i < numSensores; i++)
			{
				sensVelocidadeVento[i].frequency = new_freq;
				int old_size = sensVelocidadeVento[i].readings_size;

				sensVelocidadeVento[i].readings_size = (3600 / new_freq * 24) / sizeof(unsigned long);

				unsigned short *new_readings = malloc(sensVelocidadeVento[i].readings_size * sizeof(unsigned short));
				int min_size;
				if (old_size < sensVelocidadeVento[i].readings_size)
				{
					min_size = old_size;
				}
				else
				{
					min_size = sensVelocidadeVento[i].readings_size;
				}

				for (int j = 0; j < min_size; j++)
				{
					new_readings[j] = sensVelocidadeVento[i].readings[j];
				}

				free(sensVelocidadeVento[i].readings);
				sensVelocidadeVento[i].readings = new_readings;
			}
		}
		if (choice == 2)
		{
			numSensores++;
			sensVelocidadeVento = realloc(sensVelocidadeVento, numSensores * sizeof(Sensor));
			sensVelocidadeVento[numSensores - 1].id = 2;
			sensVelocidadeVento[numSensores - 1].sensor_type = 87;
			sensVelocidadeVento[numSensores - 1].max_limit = velcmax;
			sensVelocidadeVento[numSensores - 1].min_limit = velcmin;
			sensVelocidadeVento[numSensores - 1].frequency = freqVelcVento;
			sensVelocidadeVento[numSensores - 1].readings_size = (3600/freqVelcVento*24);
			sensVelocidadeVento[numSensores - 1].readings = malloc(sensVelocidadeVento[numSensores - 1].readings_size * sizeof(unsigned short));
			for (int j = 0; j < sensVelocidadeVento[numSensores - 1].readings_size; j++)
			{
				char comp_rand = pcg32_random_r() % 15;
				if (j != 0)
				{

					ult_velc_vento = sensVelocidadeVento[i].readings[j - 1];
				}

				sensVelocidadeVento[i].readings[j] = (unsigned char)sens_velc_vento(ult_velc_vento, comp_rand);

				if (sensVelocidadeVento[i].readings[j] > velcmax || sensVelocidadeVento[i].readings[j] < velcmin)
				{
					erros++;
				}
				else
				{
					erros = 0;
				}
				if (erros = erroMaximo)
				{
					j = j - 4;
					seed();
				}
			}
			printf("Sensor adicionado!\n");
		}
		else if (choice == 3)
		{
			if (numSensores == 0)
			{
				printf("Não existem sensores de velocidade do vento para remover\n");
			}
			else
			{
				printf("Qual o ID do sensor que pretende remover?\n");
				int id;
				scanf("%d", &id);

				int found = 0;
				for (int i = 0; i < numSensores; i++)
				{
					if (sensVelocidadeVento[i].id == id)
					{
						found = 1;
						free(sensVelocidadeVento[i].readings);
						for (int j = i; j < numSensores - 1; j++)
						{
							sensVelocidadeVento[j] = sensVelocidadeVento[j + 1];
						}
						numSensores--;
						sensVelocidadeVento = realloc(sensVelocidadeVento, numSensores * sizeof(Sensor));
						break;
					}
				}
				if (found)
				{
					printf("Sensor removido com sucesso\n");
				}
				else
				{
					printf("Sensor não encontrado\n");
				}
			}
		}
		else if (choice == 4)
		{
			break;
		}
		else
		{
			printf("Opção Inválida. \n");
		}
	}
	int opcao;
	if (numSensores > 1)
	{
		printf("Qual o sensor que deseja analisar? \n");
		for (int i = 0; i < numSensores; i++)
		{
			printf("Sensor %d\n", sensVelocidadeVento[i].id);
		}
		scanf("%d", &opcao);
		while (opcao == 0)
		{
			printf("Sensor não encontrado\n");
			scanf("%d", &opcao);
		}
	}
	else
	{
		opcao = 1;
	}
	FILE *ficheiro = fopen("leituras.csv", "w");
	fprintf(ficheiro, "Sensor de velocidade do vento: \n");
	for (int i = 0; i < numSensores; i++)
	{
		fprintf(ficheiro, "Sensor %d: \n", sensVelocidadeVento[i].id);
		// printf("%ld", sensTemperatura[i].readings_size);
		for (int j = 0; i < sensVelocidadeVento[i].readings[j]; j++)
		{
			fprintf(ficheiro, "%d, ", sensVelocidadeVento[i].readings[j]);
		}
		fprintf(ficheiro, "\n");
	}

	for (int j = 0; j < numSensores; j++)
	{
		for (int i = 0; i < sensVelocidadeVento[j].readings_size; i++)
		{
			soma += sensVelocidadeVento[j].readings[i];
			contador++;
			if (sensVelocidadeVento[j].readings[i] < valorMinimo)
			{
				valorMinimo = sensVelocidadeVento[j].readings[i];
			}
			if (sensVelocidadeVento[j].readings[i] > valorMaximo)
			{
				valorMaximo = sensVelocidadeVento[j].readings[i];
			}
		}
	}
	media = soma / contador;

	*(ptrMatriz + 3) = valorMaximo;
	*(ptrMatriz + 4) = valorMinimo;
	*(ptrMatriz + 5) = media;

	valorMinimo = 500, valorMaximo = 0, contador = 0, media = 0, soma = 0, i = 0, j = 0;

	for (int i = 0; i < numSensores; i++)
	{
		free(sensVelocidadeVento[i].readings);
		sensVelocidadeVento[i].readings = NULL;
	}
}

void sensDirVento(int i, int freqDirVento)
{

	unsigned short ult_dir_vento=20; 
	unsigned short dirmin = 0;
	unsigned short dirmax = 359;

	int numSensores = 0;
	do
	{
		printf("Quantos sensores de direção do vento deseja criar? \n");
		scanf("%d", &numSensores);
	} while (numSensores < 0);

	Sensor *sensorDirecaoVento = malloc(numSensores * sizeof(Sensor));
	for (int i = 0; i < numSensores; i++)
	{
		sensorDirecaoVento[i].id = i + 3;
		sensorDirecaoVento[i].sensor_type = 68;
		sensorDirecaoVento[i].max_limit = dirmax;
		sensorDirecaoVento[i].min_limit = dirmin;
		sensorDirecaoVento[i].frequency = freqDirVento;
		sensorDirecaoVento[i].readings_size = 3600 / freqDirVento * 24;
		sensorDirecaoVento[i].readings = malloc(sensorDirecaoVento[i].readings_size * sizeof(unsigned short));
		for (int j = 0; j < sensorDirecaoVento[i].readings_size; j++)
		{
			char comp_rand = pcg32_random_r() % 50;

			if (j != 0)
			{
				ult_dir_vento = sensorDirecaoVento[i].readings[j - 1];
			}

			sensorDirecaoVento[i].readings[j] = (unsigned short)sens_dir_vento(ult_dir_vento, comp_rand);
			
			if (sensorDirecaoVento[i].readings[j] > dirmax || sensorDirecaoVento[i].readings[j] < dirmin)
			{
				erros++;
			}
			else
			{
				erros = 0;
			}
			if (erros == erroMaximo)
			{
				j = j - 4;
				seed();
			}
		}
	}
	int choice;
	while (1)
	{

		printf("\nMenu de opções: \n");
		printf("1. Alterar frequencia do sensor de direção do vento \n");
		printf("2. Adicionar sensor de direção do vento\n");
		printf("3. Remover sensor de direção do vento\n");
		printf("4. Analisar sensor\n");
		printf("5. Sair\n");

		int choice;
		printf("Enter your choice: ");
		scanf("%d", &choice);

		if (choice == 1)
		{

			printf("Insira a nova frequencia do sensor de direcao de vento: ");
			int new_freq;
			scanf("%d", &new_freq);

			for (int i = 0; i < numSensores; i++)
			{
				sensorDirecaoVento[i].frequency = new_freq;
				int old_size = sensorDirecaoVento[i].readings_size;

				sensorDirecaoVento[i].readings_size = (3600 / new_freq * 24) / sizeof(unsigned long);

				unsigned short *new_readings = malloc(sensorDirecaoVento[i].readings_size * sizeof(unsigned short));
				int min_size;
				if (old_size < sensorDirecaoVento[i].readings_size)
				{
					min_size = old_size;
				}
				else
				{
					min_size = sensorDirecaoVento[i].readings_size;
				}

				for (int j = 0; j < min_size; j++)
				{
					new_readings[j] = sensorDirecaoVento[i].readings[j];
				}

				free(sensorDirecaoVento[i].readings);
				sensorDirecaoVento[i].readings = new_readings;
			}
		}
		if (choice == 2)
		{
			numSensores++;
			sensorDirecaoVento = realloc(sensorDirecaoVento, numSensores * sizeof(Sensor));

			sensorDirecaoVento[numSensores - 1].id = 3;
			sensorDirecaoVento[numSensores - 1].sensor_type = 68;
			sensorDirecaoVento[numSensores - 1].max_limit = dirmax;
			sensorDirecaoVento[numSensores - 1].min_limit = dirmin;
			sensorDirecaoVento[numSensores - 1].frequency = freqDirVento;
			sensorDirecaoVento[numSensores - 1].readings_size = 3600 / freqDirVento * 24;
			sensorDirecaoVento[numSensores - 1].readings = malloc(sensorDirecaoVento[numSensores - 1].readings_size * sizeof(unsigned short));

			for (int j = 0; j < sensorDirecaoVento[numSensores - 1].readings_size; j++)
			{
				char comp_rand = pcg32_random_r() % 50;

				if (j != 0)
				{
					ult_dir_vento = sensorDirecaoVento[numSensores - 1].readings[j - 1];
				}
				sensorDirecaoVento[i].readings[j] = (unsigned short)sens_dir_vento(ult_dir_vento, comp_rand);

				if (sensorDirecaoVento[i].readings[j] > dirmax || sensorDirecaoVento[i].readings[j] < dirmin)
				{
					erros++;
				}
				else
				{
					erros = 0;
				}
				if (erros == erroMaximo)
				{
					j = j - 4;
					seed();
				}
			}
			printf("Sensor de Direção de vento adicionado! \n");
		}
		if (choice == 3)
		{
			if (numSensores == 0)
			{
				printf("Não existem sensores de direção do vento para remover!\n");
			}
			else
			{
				printf("Qual o id do sensor que pretende remover? ");
				int id;
				scanf("%d", &id);

				int found = 0;
				for (int i = 0; i < numSensores; i++)
				{
					if (sensorDirecaoVento[i].id == id)
					{
						found = 1;
						free(sensorDirecaoVento[i].readings);
						for (int j = i; j < numSensores - 1; j++)
						{
							sensorDirecaoVento[j] = sensorDirecaoVento[j + 1];
						}
						numSensores--;
						sensorDirecaoVento = realloc(sensorDirecaoVento, numSensores * sizeof(Sensor));
						break;
					}
				}
				if (found)
				{
					printf("Sensor de direção do vento removido com sucesso!\n");
				}
				else
				{
					printf("Não existe nenhum sensor de direção do vento com esse id!\n");
				}
			}
		}
		else if (choice == 4)
		{
			break;
		}
		else
		{
			printf("Opção Inválida.\n");
		}
	}
	int opcao;
	if (numSensores > 1)
	{
		printf("Qual o sensor que deseja analisar? \n");
		for (int i = 0; i < numSensores; i++)
		{
			printf("Sensor %d\n", sensorDirecaoVento[i].id);
		}
		scanf("%d", &opcao);
		while (opcao == 0)
		{
			printf("Sensor não encontrado\n");
			scanf("%d", &opcao);
		}
	}
	else
	{
		opcao = 1;
	}
	FILE *ficheiro = fopen("leituras.csv", "w");
	fprintf(ficheiro, "Sensor de Temperatura: \n");
	for (int i = 0; i < numSensores; i++)
	{
		fprintf(ficheiro, "Sensor %d: \n", sensorDirecaoVento[i].id);

		for (int j = 0; i < sensorDirecaoVento[i].readings[j]; j++)
		{
			fprintf(ficheiro, "%d, ", sensorDirecaoVento[i].readings[j]);
		}
		fprintf(ficheiro, "\n");
	}
	fclose(ficheiro);
	for (int j = 0; j < numSensores; j++)
	{
		for (int i = 0; i < sensorDirecaoVento[j].readings_size; i++)
		{
			soma += sensorDirecaoVento[j].readings[i];
			contador++;
			if (sensorDirecaoVento[j].readings[i] < valorMinimo)
			{
				valorMinimo = sensorDirecaoVento[j].readings[i];
			}
			if (sensorDirecaoVento[j].readings[i] > valorMaximo)
			{
				valorMaximo = sensorDirecaoVento[j].readings[i];
			}
		}
	}

	media = soma / contador;

	*(ptrMatriz + 6) = valorMaximo;
	*(ptrMatriz + 7) = valorMinimo;
	*(ptrMatriz + 8) = media;

	valorMinimo = 500, valorMaximo = 0, contador = 0, media = 0, soma = 0, i = 0, j = 0;

	for (int i = 0; i < numSensores; i++)
	{
		free(sensorDirecaoVento[i].readings);
		sensorDirecaoVento[i].readings = NULL;
	}
}
void sensHumAtm(int i, int freqHumAtm, int freqPluvio)
{

	
	unsigned char ult_hmd_atm= 5;
	unsigned char humatmmin = 0;
	unsigned char humatmmax = 5;
	char comp_rand;

	unsigned char ult_pluvio;

	

	int numSensores = 0;
	do
	{
		printf("Quantos sensores de humidade atmosferica deseja criar? \n");
		scanf("%d", &numSensores);
	} while (numSensores < 0);

	Sensor *sensorHumidadeAtmosferica = malloc(numSensores * sizeof(Sensor));
	for (int i = 0; i < numSensores; i++)
	{
		sensorHumidadeAtmosferica[i].id = i + 4;
		sensorHumidadeAtmosferica[i].sensor_type = 65;
		sensorHumidadeAtmosferica[i].max_limit = humatmmax;
		sensorHumidadeAtmosferica[i].min_limit = humatmmin;
		sensorHumidadeAtmosferica[i].frequency = freqHumAtm;
		sensorHumidadeAtmosferica[i].readings_size = 3600 / freqHumAtm * 24;
		sensorHumidadeAtmosferica[i].readings = malloc(sensorHumidadeAtmosferica[i].readings_size * sizeof(unsigned char));
		for (int j = 0; j < sensorHumidadeAtmosferica[i].readings_size; j++)
		{
			char comp_rand = pcg32_random_r() % 10;

			if (j != 0)
			{
				ult_hmd_atm = sensorHumidadeAtmosferica[i].readings[j - 1];
			}
			sensorHumidadeAtmosferica[i].readings[j] = (unsigned char)sens_humd_atm(ult_hmd_atm, ult_pluvio, comp_rand);

			if (sensorHumidadeAtmosferica[i].readings[j] > humatmmax || sensorHumidadeAtmosferica[i].readings[j] < humatmmin)
			{
				erros++;
			}
			else
			{
				erros = 0;
			}
			if (erros == erroMaximo)
			{
				j = j - 4;
				seed();
			}
		}
	}
	int choice;
	while (1)
	{

		printf("\nMenu:\n");
		printf("1. Mudar a frequencia do sensor de humidade atmosférica\n");
		printf("2. Adicionar sensor\n");
		printf("3. Remover sensor \n");
		printf("4. Analisar sensor\n");
		printf("5. Sair\n");

		int choice;
		printf("Enter your choice: ");
		scanf("%d", &choice);

		if (choice == 1)
		{
			printf("Escreva a nova frequencia: ");
			int new_freq;
			scanf("%d", &new_freq);

			for (int i = 0; i < numSensores; i++)
			{
				sensorHumidadeAtmosferica[i].frequency = new_freq;
				int old_size = sensorHumidadeAtmosferica[i].readings_size;

				sensorHumidadeAtmosferica[i].readings_size = (3600 / new_freq * 24) / sizeof(unsigned long);

				unsigned short *new_readings = malloc(sensorHumidadeAtmosferica[i].readings_size * sizeof(unsigned short));
				int min_size;
				if (old_size < sensorHumidadeAtmosferica[i].readings_size)
				{
					min_size = old_size;
				}
				else
				{
					min_size = sensorHumidadeAtmosferica[i].readings_size;
				}

				for (int j = 0; j < min_size; j++)
				{
					new_readings[j] = sensorHumidadeAtmosferica[i].readings[j];
				}

				free(sensorHumidadeAtmosferica[i].readings);
				sensorHumidadeAtmosferica[i].readings = new_readings;
			}
		}

		if (choice == 2)
		{
			// Add a sensor
			numSensores++;
			sensorHumidadeAtmosferica = realloc(sensorHumidadeAtmosferica, numSensores * sizeof(Sensor));

			sensorHumidadeAtmosferica[numSensores - 1].id = numSensores;
			sensorHumidadeAtmosferica[numSensores - 1].sensor_type = 84;
			sensorHumidadeAtmosferica[numSensores - 1].max_limit = humatmmax;
			sensorHumidadeAtmosferica[numSensores - 1].min_limit = humatmmin;
			sensorHumidadeAtmosferica[numSensores - 1].frequency = freqHumAtm;
			sensorHumidadeAtmosferica[numSensores - 1].readings_size = 3600 / freqHumAtm * 24;
			sensorHumidadeAtmosferica[numSensores - 1].readings = malloc(sensorHumidadeAtmosferica[i].readings_size * sizeof(unsigned short));

			for (int j = 0; j < sensorHumidadeAtmosferica[i].readings_size; j++)
			{
				char comp_rand = pcg32_random_r() % 10;

				if (j != 0)
				{

					ult_hmd_atm = sensorHumidadeAtmosferica[i].readings[j - 1];
				}

				sensorHumidadeAtmosferica[i].readings[j] = (unsigned char)sens_humd_atm(ult_hmd_atm, ult_pluvio, comp_rand);

				if (sensorHumidadeAtmosferica[i].readings[j] > humatmmax || sensorHumidadeAtmosferica[i].readings[j] < humatmmin)
				{
					erros++;
				}
				else
				{
					erros = 0;
				}
				if (erros == erroMaximo)
				{
					// Se eu passar o J não estou a incializar de novo?
					// Como é que eu quando encontro um erro recuo 4 leituras e volto a gerar

					j = j - 4;
					seed();
				}
			}
			printf("Sensor adicionado.\n");
		}

		else if (choice == 3)
		{
			// Remove a sensor
			if (numSensores == 0)
			{
				printf("Não há sensores para remover\n");
			}
			else
			{
				printf("Escreva o id do sensor a remover: ");
				int id;
				scanf("%d", &id);

				int found = 0;
				for (int i = 0; i < numSensores; i++)
				{
					if (sensorHumidadeAtmosferica[i].id == id)
					{
						found = 1;
						free(sensorHumidadeAtmosferica[i].readings);
						for (int j = i; j < numSensores - 1; j++)
						{
							sensorHumidadeAtmosferica[j] = sensorHumidadeAtmosferica[j + 1];
						}
						numSensores--;
						sensorHumidadeAtmosferica = realloc(sensorHumidadeAtmosferica, numSensores * sizeof(Sensor));
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
		else if (choice == 4)
		{
			break;
		}
		else
		{
			printf("Opção Inválida. \n");
		}
	}
	int opcao;
	if (numSensores > 1)
	{
		printf("Qual o sensor que deseja analisar? \n");
		for (int i = 0; i < numSensores; i++)
		{
			printf("Sensor %d\n", sensorHumidadeAtmosferica[i].id);
		}
		scanf("%d", &opcao);
		while (opcao == 0)
		{
			printf("Sensor não encontrado\n");
			scanf("%d", &opcao);
		}
	}
	else
	{
		opcao = 1;
	}
	FILE *ficheiro = fopen("leituras.csv", "w");
	fprintf(ficheiro, "Sensor de Temperatura: \n");
	for (int i = 0; i < numSensores; i++)
	{
		fprintf(ficheiro, "Sensor %d: \n", sensorHumidadeAtmosferica[i].id);
		// printf("%ld", sensTemperatura[i].readings_size);
		for (int j = 0; i < sensorHumidadeAtmosferica[i].readings[j]; j++)
		{
			fprintf(ficheiro, "%d, ", sensorHumidadeAtmosferica[i].readings[j]);
		}
		fprintf(ficheiro, "\n");
	}
	fclose(ficheiro);

	for (int j = 0; j < numSensores; j++)
	{
		for (int i = 0; i < sensorHumidadeAtmosferica[j].readings_size; i++)
		{
			soma += sensorHumidadeAtmosferica[j].readings[i];
			contador++;
			if (sensorHumidadeAtmosferica[j].readings[i] < valorMinimo)
			{
				valorMinimo = sensorHumidadeAtmosferica[j].readings[i];
			}
			if (sensorHumidadeAtmosferica[j].readings[i] > valorMaximo)
			{
				valorMaximo = sensorHumidadeAtmosferica[j].readings[i];
			}
		}
	}

	media = soma / contador;

	*(ptrMatriz + 8) = valorMaximo;
	*(ptrMatriz + 9) = valorMinimo;
	*(ptrMatriz + 10) = media;

	valorMinimo = 500, valorMaximo = 0, contador = 0, media = 0, soma = 0, i = 0, j = 0;

	for (int i = 0; i < numSensores; i++)
	{
		free(sensorHumidadeAtmosferica[i].readings);
		sensorHumidadeAtmosferica[i].readings = NULL;
	}
}
void sensHumSolo(int i, int freqHumSolo, int freqPluvio)
{

	

	unsigned char solomin = 0;
	unsigned char solomax = 5;
	char comp_rand;

	unsigned char ult_humd_solo;
	unsigned char ult_pluvio = 0;

	int numSensores = 0;
	do
	{
		printf("Quantos sensores de humidade do solo deseja criar? \n");
		scanf("%d", &numSensores);
	} while (numSensores < 0);

	Sensor *sensorHumidadeSolo = malloc(numSensores * sizeof(Sensor));
	for (int i = 0; i < numSensores; i++)
	{
		sensorHumidadeSolo[i].id = i + 5;
		sensorHumidadeSolo[i].sensor_type = 71;
		sensorHumidadeSolo[i].max_limit = solomax;
		sensorHumidadeSolo[i].min_limit = solomin;
		sensorHumidadeSolo[i].frequency = freqHumSolo;
		sensorHumidadeSolo[i].readings_size = 3600 / freqHumSolo * 24;
		sensorHumidadeSolo[i].readings = malloc(sensorHumidadeSolo[i].readings_size * sizeof(unsigned char));
		for (int i = 0; i < sensorHumidadeSolo[i].readings_size; i++)
		{
			comp_rand = pcg32_random_r() % 10;

			if (j != 0)
			{

				ult_humd_solo = sensorHumidadeSolo[i].readings[j - 1];
			}

			sensorHumidadeSolo[i].readings[j] = (unsigned char)sens_humd_solo(ult_humd_solo, ult_pluvio, comp_rand);

			if (sensorHumidadeSolo[i].readings[j] > solomax || sensorHumidadeSolo[i].readings[j] < solomin)
			{
				erros++;
			}
			else
			{
				erros = 0;
			}
			if (erros == erroMaximo)
			{

				j = j - 4;
				seed();
			}
		}
	}
	int choice;
	while (1)
	{

		printf("\nMenu:\n");
		printf("1. Mudar a frequencia do sensor de humidade no solo\n");
		printf("2. Adicionar sensor\n");
		printf("3. Remover sensor\n");
		printf("4. Analisar sensor\n");
		printf("5. Sair\n");

		int choice;
		printf("Qual a sua opção: ");
		scanf("%d", &choice);

		// Mudar a frequencia

		if (choice == 1)
		{
			printf("Escreva a nova frequencia: ");
			int new_freq;
			scanf("%d", &new_freq);

			for (int i = 0; i < numSensores; i++)
			{
				sensorHumidadeSolo[i].frequency = new_freq;
				int old_size = sensorHumidadeSolo[i].readings_size;

				sensorHumidadeSolo[i].readings_size = (3600 / new_freq * 24) / sizeof(unsigned long);

				unsigned short *new_readings = malloc(sensorHumidadeSolo[i].readings_size * sizeof(unsigned short));
				int min_size;
				if (old_size < sensorHumidadeSolo[i].readings_size)
				{
					min_size = old_size;
				}
				else
				{
					min_size = sensorHumidadeSolo[i].readings_size;
				}

				for (int j = 0; j < min_size; j++)
				{
					new_readings[j] = sensorHumidadeSolo[i].readings[j];
				}

				free(sensorHumidadeSolo[i].readings);
				sensorHumidadeSolo[i].readings = new_readings;
			}
		}

		if (choice == 2)
		{
			// Add a sensor
			numSensores++;
			sensorHumidadeSolo = realloc(sensorHumidadeSolo, numSensores * sizeof(Sensor));

			sensorHumidadeSolo[numSensores - 1].id = numSensores;
			sensorHumidadeSolo[numSensores - 1].sensor_type = 84;
			sensorHumidadeSolo[numSensores - 1].max_limit = solomax;
			sensorHumidadeSolo[numSensores - 1].min_limit = solomin;
			sensorHumidadeSolo[numSensores - 1].frequency = freqHumSolo;
			sensorHumidadeSolo[numSensores - 1].readings_size = 3600 / freqHumSolo * 24;
			sensorHumidadeSolo[numSensores - 1].readings = malloc(sensorHumidadeSolo[i].readings_size * sizeof(unsigned short));

			for (int j = 0; j < sensorHumidadeSolo[i].readings_size; j++)
			{
				char comp_rand = pcg32_random_r() % 10;

				if (j != 0)
				{

					ult_humd_solo = sensorHumidadeSolo[i].readings[j - 1];
				}

				sensorHumidadeSolo[i].readings[j] = (unsigned char)sens_humd_solo(ult_humd_solo, ult_pluvio, comp_rand);
				printf("%d\n", sensorHumidadeSolo[i].readings[j]);

				if (sensorHumidadeSolo[i].readings[j] > solomax || sensorHumidadeSolo[i].readings[j] < solomin)
				{
					erros++;
				}
				else
				{
					erros = 0;
				}
				if (erros == erroMaximo)
				{

					j = j - 4;
					seed();
				}
			}
			printf("Sensor adicionado.\n");
		}

		else if (choice == 3)
		{
			// Remove a sensor
			if (numSensores == 0)
			{
				printf("Não há sensores para remover.\n");
			}
			else
			{
				printf("Escreva o id do sensor que quer remover: ");
				int id;
				scanf("%d", &id);

				int found = 0;
				for (int i = 0; i < numSensores; i++)
				{
					if (sensorHumidadeSolo[i].id == id)
					{
						found = 1;
						free(sensorHumidadeSolo[i].readings);
						for (int j = i; j < numSensores - 1; j++)
						{
							sensorHumidadeSolo[j] = sensorHumidadeSolo[j + 1];
						}
						numSensores--;
						sensorHumidadeSolo = realloc(sensorHumidadeSolo, numSensores * sizeof(Sensor));
						break;
					}
				}

				if (found)
				{
					printf("Sensor removido.\n");
				}
				else
				{
					printf("Sensor não encontrado.\n");
				}
			}
		}
		else if (choice == 4)
		{
			break;
		}
		else
		{
			printf("Opção inválida. \n");
		}
	}
	int opcao;
	if (numSensores > 1)
	{
		printf("Qual o sensor que deseja analisar? \n");
		for (int i = 0; i < numSensores; i++)
		{
			printf("Sensor %d\n", sensorHumidadeSolo[i].id);
		}
		scanf("%d", &opcao);
		while (opcao == 0)
		{
			printf("Sensor não encontrado\n");
			scanf("%d", &opcao);
		}
	}
	else
	{
		opcao = 1;
	}

	FILE *ficheiro = fopen("leituras.csv", "w");
	fprintf(ficheiro, "Sensor de Humidade de Solo: \n");
	for (int i = 0; i < numSensores; i++)
	{
		fprintf(ficheiro, "Sensor %d: \n", sensorHumidadeSolo[i].id);

		for (int j = 0; i < sensorHumidadeSolo[i].readings[j]; j++)
		{
			fprintf(ficheiro, "%d, ", sensorHumidadeSolo[i].readings[j]);
		}
		fprintf(ficheiro, "\n");
	}
	fclose(ficheiro);

	for (int j = 0; j < numSensores; j++)
	{
		for (int i = 0; i < sensorHumidadeSolo[j].readings_size; i++)
		{
			soma += sensorHumidadeSolo[j].readings[i];
			contador++;
			if (sensorHumidadeSolo[j].readings[i] < valorMinimo)
			{
				valorMinimo = sensorHumidadeSolo[j].readings[i];
			}
			if (sensorHumidadeSolo[j].readings[i] > valorMaximo)
			{
				valorMaximo = sensorHumidadeSolo[j].readings[i];
			}
		}
	}

	media = soma / contador;

	*(ptrMatriz + 11) = valorMaximo;
	*(ptrMatriz + 12) = valorMinimo;
	*(ptrMatriz + 13) = media;

	valorMinimo = 500, valorMaximo = 0, contador = 0, media = 0, soma = 0, i = 0, j = 0;

	for (int i = 0; i < numSensores; i++)
	{
		free(sensorHumidadeSolo[i].readings);
		sensorHumidadeSolo[i].readings = NULL;
	}
}

void sensPluvio(int i, int freqPluvio, int freqTemp)
{

	char pluvio[3600 / freqPluvio * 24];
	char temp[3600 / freqTemp * 24];
	char minPluvio = 0;
	char maxPluvio = 5;

	unsigned char ult_pluvio;
	char ult_temp;

	int numSensores;
	do
	{
		printf("Quantos sensores de pluviosidade deseja criar? \n");
		scanf("%d", &numSensores);
	} while (numSensores < 0);

	Sensor *sensPluviosidade = malloc(numSensores * sizeof(Sensor));

	for (int i = 0; i < numSensores; i++)
	{
		sensPluviosidade[i].id = i + 6;
		sensPluviosidade[i].sensor_type = 82;
		sensPluviosidade[i].max_limit = maxPluvio;
		sensPluviosidade[i].min_limit = minPluvio;
		sensPluviosidade[i].frequency = freqPluvio;
		sensPluviosidade[i].readings_size = sizeof(pluvio) / sizeof(unsigned long);
		sensPluviosidade[i].readings = malloc(sensPluviosidade[i].readings_size * sizeof(unsigned char));

		for (int j = 0; j < sensPluviosidade[i].readings_size; i++)
		{

			char ult_temp = sensPluviosidade[i].readings[j - 1];

			char comp_rand = pcg32_random_r() % 5;

			int comp_relative = pcg32_random_r() % ult_temp;

			if (comp_relative != 0)
			{
				comp_rand = 0;
			}

			if (j != 0)
			{
				ult_pluvio = pluvio[j - 1];
			}

			pluvio[i] = sens_pluvio(ult_pluvio, ult_temp, comp_rand);

			if (j != 0)
			{

				ult_pluvio = sensPluviosidade[i].readings[j - 1];
			}
			sensPluviosidade[i].readings[j] = (unsigned char)sens_pluvio(ult_pluvio, ult_temp, comp_rand);

			if (sensPluviosidade[i].readings[j] > maxPluvio || sensPluviosidade[i].readings[j] < minPluvio)
			{
				erros++;
			}
			else
			{
				erros = 0;
			}
			if (erros == erroMaximo)
			{

				j = j - 4;
				seed();
			}
		}
	}
	int choice;
	while (1)
	{

		printf("\nMenu:\n");
		printf("1. Mudar a frequencia do sensor pluviosidade\n");
		printf("2. Adicionar sensor\n");
		printf("3. Remover sensor\n");
		printf("4. Analisar sensor\n");
		printf("5. Sair\n");

		int choice;
		printf("Escolha a sua opção: ");
		scanf("%d", &choice);

		// Mudar a frequencia

		if (choice == 1)
		{
			printf("Insira a nova frequencia para o sensor: ");
			int new_freq;
			scanf("%d", &new_freq);

			for (int i = 0; i < numSensores; i++)
			{
				sensPluviosidade[i].frequency = new_freq;
				int old_size = sensPluviosidade[i].readings_size;

				sensPluviosidade[i].readings_size = (3600 / new_freq * 24) / sizeof(unsigned long);

				unsigned short *new_readings = malloc(sensPluviosidade[i].readings_size * sizeof(unsigned short));
				int min_size;
				if (old_size < sensPluviosidade[i].readings_size)
				{
					min_size = old_size;
				}
				else
				{
					min_size = sensPluviosidade[i].readings_size;
				}

				for (int j = 0; j < min_size; j++)
				{
					new_readings[j] = sensPluviosidade[i].readings[j];
				}

				free(sensPluviosidade[i].readings);
				sensPluviosidade[i].readings = new_readings;
			}
		}

		if (choice == 2)
		{
			// Add a sensor
			numSensores++;
			sensPluviosidade = realloc(sensPluviosidade, numSensores * sizeof(Sensor));

			sensPluviosidade[numSensores - 1].id = numSensores;
			sensPluviosidade[numSensores - 1].sensor_type = 84;
			sensPluviosidade[numSensores - 1].max_limit = maxPluvio;
			sensPluviosidade[numSensores - 1].min_limit = minPluvio;
			sensPluviosidade[numSensores - 1].frequency = freqPluvio;
			sensPluviosidade[numSensores - 1].readings_size = 3600 / freqPluvio * 24;
			sensPluviosidade[numSensores - 1].readings = malloc(sensPluviosidade[i].readings_size * sizeof(unsigned short));
			for (int j = 0; j < sensPluviosidade[i].readings_size; j++)
			{

				char ult_temp = temp[i];

				char comp_rand = pcg32_random_r() % 5;

				int comp_relative = pcg32_random_r() % ult_temp;

				if (comp_relative != 0)
				{
					comp_rand = 0;
				}

				if (i != 0)
				{
					ult_pluvio = pluvio[i - 1];
				}
				pluvio[i] = sens_pluvio(ult_pluvio, ult_temp, comp_rand);

				sensPluviosidade[i].readings[j] = (unsigned char)sens_pluvio(ult_pluvio, ult_temp, comp_rand);
				printf("%d\n", sensPluviosidade[i].readings[j]);

				if (sensPluviosidade[i].readings[j] > maxPluvio || sensPluviosidade[i].readings[j] < minPluvio)
				{
					erros++;
				}
				else
				{
					erros = 0;
				}
				if (erros == erroMaximo)
				{
					// Se eu passar o J não estou a incializar de novo?
					// Como é que eu quando encontro um erro recuo 4 leituras e volto a gerar

					j = j - 4;
					seed();
				}
			}
			printf("Sensor de Pluviosidade adicionado com sucesso.\n");
		}

		if (choice == 3)
		{
			// Remove a sensor
			if (numSensores == 0)
			{
				printf("Não há sensores para remover.\n");
			}
			else
			{
				printf("Adicionar o id do sensor a remover: ");
				int id;
				scanf("%d", &id);

				int found = 0;
				for (int i = 0; i < numSensores; i++)
				{
					if (sensPluviosidade[i].id == id)
					{
						found = 1;
						free(sensPluviosidade[i].readings);
						for (int j = i; j < numSensores - 1; j++)
						{
							sensPluviosidade[j] = sensPluviosidade[j + 1];
						}
						numSensores--;
						sensPluviosidade = realloc(sensPluviosidade, numSensores * sizeof(Sensor));
						break;
					}
				}

				if (found)
				{
					printf("Sensor removido.\n");
				}
				else
				{
					printf("Sensor não encontrado.\n");
				}
			}
		}
		else if (choice == 4)
		{
			break;
		}
		else
		{
			printf("Opção Inválida. \n");
		}
	}
	int opcao;
	if (numSensores > 1)
	{
		printf("Qual o sensor que deseja analisar? \n");
		for (int i = 0; i < numSensores; i++)
		{
			printf("Sensor %d\n", sensPluviosidade[i].id);
		}
		scanf("%d", &opcao);
		while (opcao == 0)
		{
			printf("Sensor não encontrado\n");
			scanf("%d", &opcao);
		}
	}
	else
	{
		opcao = 1;
	}
	FILE *ficheiro = fopen("leituras.csv", "w");
	fprintf(ficheiro, "Sensor de Temperatura: \n");
	for (int i = 0; i < numSensores; i++)
	{
		fprintf(ficheiro, "Sensor %d: \n", sensPluviosidade[i].id);
		// printf("%ld", sensTemperatura[i].readings_size);
		for (int j = 0; i < sensPluviosidade[i].readings[j]; j++)
		{
			fprintf(ficheiro, "%d, ", sensPluviosidade[i].readings[j]);
		}
		fprintf(ficheiro, "\n");
	}
	fclose(ficheiro);

	for (int j = 0; j < numSensores; j++)
	{
		for (int i = 0; i < sensPluviosidade[j].readings_size; i++)
		{
			soma += sensPluviosidade[j].readings[i];
			contador++;
			if (sensPluviosidade[j].readings[i] < valorMinimo)
			{
				valorMinimo = sensPluviosidade[j].readings[i];
			}
			if (sensPluviosidade[j].readings[i] > valorMaximo)
			{
				valorMaximo = sensPluviosidade[j].readings[i];
			}
		}
	}

	media = soma / contador;

	*(ptrMatriz + 14) = valorMaximo;
	*(ptrMatriz + 15) = valorMinimo;
	*(ptrMatriz + 16) = media;

	valorMinimo = 500, valorMaximo = 0, contador = 0, media = 0, soma = 0, i = 0, j = 0;

	for (int i = 0; i < numSensores; i++)
	{
		free(sensPluviosidade[i].readings);
		sensPluviosidade[i].readings = NULL;
	}
}

int main()
{
	seed();
	printf("Qual a frequencia de leitura dos sensor de temperatura? (em segundos)\n");
	int freqTemp;
	scanf("%d", &freqTemp);
	printf("Qual a frequencia de leitura dos sensor de velocidade do vento? (em segundos)\n");
	int freqVelcVento;
	scanf("%d", &freqVelcVento);
	printf("Qual a frequencia de leitura dos sensor de direção do vento? (em segundos)\n");
	int freqDirVento;
	scanf("%d", &freqDirVento);
	printf("Qual a frequencia de leitura dos sensor de humidade atmosferica? (em segundos)\n");
	int freqHumAtm;
	scanf("%d", &freqHumAtm);
	printf("Qual a frequencia de leitura dos sensor de humidade do solo? (em segundos)\n");
	int freqHumSolo;
	scanf("%d", &freqHumSolo);
	printf("Qual a frequencia de leitura dos sensor de pluviosidade? (em segundos)\n");
	int freqPluvio;
	scanf("%d", &freqPluvio);
	int sensor;
	printf("Qual o sensor que pretende ler?\n");
	printf("1 - Temperatura\n\n");
	printf("2 - Velocidade do Vento\n\n");
	printf("3 - Direção do Vento\n\n");
	printf("4 - Humidade Atmosférica\n\n");
	printf("5 - Humidade do Solo\n\n");
	printf("6 - Pluviosidade\n\n");
	scanf("%d", &sensor);

	switch (sensor)
	{
	case 1:
		printf("Temperatura\n");
		sensTemp(0, freqTemp);
		break;

	case 2:
		printf("Velocidade do vento\n");
		sensVelcVento(0, freqVelcVento);
		break;

	case 3:
		printf("Direção do vento\n");
		sensDirVento(0, freqDirVento);
		break;

	case 4:
		printf("Humidade atmosférica\n");
		sensHumAtm(0, freqHumAtm, freqPluvio);
		break;

	case 5:
		printf("Humidade do solo\n");
		sensHumSolo(0, freqHumSolo, freqPluvio);
		break;

	case 6:
		printf("Pluviosidade\n");
		sensPluvio(0, freqPluvio, freqTemp);
		break;
	}
	/*sensTemp(0, freqTemp);
	sensVelcVento(0, freqVelcVento);
	sensDirVento(0, freqDirVento);
	sensHumAtm(0, freqHumAtm, freqPluvio);
	sensHumSolo(0, freqHumSolo, freqPluvio);
	sensPluvio(0, freqPluvio, freqTemp);
	*/
	FILE *ficheiro = fopen("matrix.csv", "w");
	fprintf(ficheiro, "\t\t\tValor Maximo\tValor Minimo\tMedia dos valores");
	printf("\t\t\tValor Maximo\tValor Minimo\tMedia dos valores");
	fprintf(ficheiro, "\n");
	printf("\n");
	for (int i = 0; i < 6; i++)
	{
		switch (i)
		{
		case 0:
			fprintf(ficheiro, "Temperatura:\t\t");
			printf("Temperatura:\t\t");
			break;

		case 1:
			fprintf(ficheiro, "Velocidade do Vento:\t");
			printf("Velocidade do vento:\t");
			break;

		case 2:
			fprintf(ficheiro, "Direção do Vento:\t");
			printf("Direcao do vento:\t");
			break;

		case 3:
			fprintf(ficheiro, "Humidade atmosferica:\t");
			printf("Humidade atmosferica:\t");
			break;

		case 4:
			fprintf(ficheiro, "Humidade do solo:\t");
			printf("Humidade do solo:\t");
			break;

		case 5:
			fprintf(ficheiro, "Pluviosidade:\t");
			printf("Pluviosidade:\t\t");
			break;
		}
		for (int j = 0; j < 3; j++)
		{
			fprintf(ficheiro, "%10d\t", matriz[i][j]);
			printf("%10d\t", matriz[i][j]);
		}
		fprintf(ficheiro, "\n");
		printf("\n");
		
	}
	fclose(ficheiro);
}


