.section .text
    .global sens_humd_atm

sens_humd_atm:
    addb %dl, %dil

    cmpb $0, %dil
    jl zero
    movb %dil, %al
    
    ret

zero:
     movb $0, %dil
     movb %dil, %al
    
    ret
    