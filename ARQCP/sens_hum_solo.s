.section .text
    .global sens_humd_solo

sens_humd_solo:
    

    addb %dl, %dil

    cmpb $0, %dil
    jl zero

    movb %dil, %al
    
    ret

zero:
    movb $0, %al

    ret
    
