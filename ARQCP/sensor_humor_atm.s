.section data
    .global ptrhumatm
.section text
    .global sens_humd_atm

sen_humd_atm:
    movq ptrhumatm(%rip), %rcx

    addb %dl, %dil

    cmpb $0, %dil
    jl zero
    movb %dil, %al
    movb %al, (%rcx)
    ret

zero:
    movb $0, %dil
    movb %dil, %al
    movb %al, (%rcx)
    ret