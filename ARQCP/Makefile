main: main.o pcg32_random_r.o sensor_pluvio.o sensor_humd_solo.o sensor_humd_atm.o sensor_velc_vento.o sensor_temp.o sensor_dir_vento.o
	gcc -Wall -g main.o pcg32_random_r.o sensor_temp.o sensor_velc_vento.o sensor_dir_vento.o sensor_humd_atm.o sensor_humd_solo.o sensor_pluvio.o -o main
    
main.o: main.c
	gcc -Wall -g -c main.c -o main.o

pcg32_random_r.o: pcg32_random_r.s
	gcc -Wall -g -c pcg32_random_r.s -o pcg32_random_r.o

sensor_temp.o: sensor_temp.s
	gcc -Wall -g -c sensor_temp.s -o sensor_temp.o

sensor_velc_vento.o: sensor_velc_vento.s
	gcc -Wall -g -c sensor_velc_vento.s -o sensor_velc_vento.o

sensor_dir_vento.o: sensor_dir_vento.s
	gcc -Wall -g -c sensor_dir_vento.s -o sensor_dir_vento.o

sensor_humd_atm.o: sensor_humd_atm.s
	gcc -Wall -g -c sensor_humd_atm.s -o sensor_humd_atm.o

sensor_humd_solo.o: sensor_humd_solo.s
	gcc -Wall -g -c sensor_humd_solo.s -o sensor_humd_solo.o

sensor_pluvio: sensor_pluvio.s
	gcc -Wall -g -c sensor_pluvio.s -o sensor_pluvio.o

run: main
	./main

clean: 
	rm *.o main
