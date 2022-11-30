.section .data
    .global ptrhumsolo
.section .text
    .global sens_humd_solo

sens_humd_solo:
    movq ptrhumsolo(%rip), %rcx

    addb %dl, %dil

    cmpb $0, %dil
    jl zero

    mob %dil, %al
    movb %al, (%rcx)

zero:
    movb $0, %dil
    movb %dil, %al
    movb %al, (%rcx)
    
