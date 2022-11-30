main: main.o pcg32_random_r.o
	gcc -Wall -g main.o pcg32_random_r.o -o main

main.o: main.c pcg32_random_r.h
	gcc -Wall -g -c main.c -o main.o

pcg32_random_r.o: pcg32_random_r.s
	gcc -Wall -g -c pcg32_random_r.s -o pcg32_random_r.o

run: main
	./main

clean: 
	rm *.o main
