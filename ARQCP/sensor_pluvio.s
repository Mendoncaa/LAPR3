.section data
    .global ptrpluvio
.section .text
    .global sens_pluvio

sens_pluvio:
    movq ptrpluvio(%rip), %rcx

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