.section data
    .global ptrtemp
.section .text
    .global sens_temp

sens_temp:
    movq ptrtemp(%rip), %rdx
    addb %sil, %dil
    movb %dil, %al
    movb %al, (%rdx)
    ret