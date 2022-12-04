.section .text
    .global sens_pluvio

sens_pluvio:
    addb %dl, %dil

    cmpb $0, %dil
    jl zero

    movb %dil, %al
    

    ret

zero:
    movb $0, %dil
    movb %dil, %al
 
    ret


