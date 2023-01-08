INCLUDES = pcg32_random_r.h
SOURCES = pcg32_random_r.s sens_temp.s sens_velc_vento.s sens_dir_vento.s sens_pluvio.s sens_humd_atm.s sens_humd_solo.s main.c
OBJFILES = pcg32_random_r.o sens_temp.o sens_velc_vento.o sens_dir_vento.o sens_pluvio.o sens_humd_atm.o sens_humd_solo.o main.o
PROG = prog

.SUFFIXES: .c .o .s

.c.o.s:
	gcc -Wall -g -c $<

${PROG}: ${OBJFILES}
	gcc -Wall -g -o ${PROG} ${OBJFILES}

${OBJFILES}: ${SOURCES} ${INCLUDES}

run: ${PROG}
	./${PROG}

clear:
	rm -f ${OBJFILES} ${PROG}