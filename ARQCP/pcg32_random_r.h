#ifndef PCG32_RANDOM_R_H
#define PCG32_RANDOM_R_H
int pcg32_random_r();
char sens_temp(char ult_temp, char comp_rand);
unsigned char sens_velc_vento(unsigned char ult_velc_vento, char comp_rand);
unsigned short sens_dir_vento(unsigned short ult_dir_vento, short comp_rand);
unsigned char sens_humd_atm(unsigned char ult_hmd_atm, unsigned char ult_pluvio, char comp_rand);
unsigned char sens_humd_solo(unsigned char ult_hmd_solo, unsigned char ult_pluvio, char comp_rand);
unsigned char sens_pluvio(unsigned char ult_pluvio, char ult_temp, char comp_rand);
#endif
