.section data
    .global ptrvento

.section .text
    .global sens_velc_vento

sens_velc_vento:
    movq ptrvento (%rip),%rdx
    addb %sil, %dil
    movb %dil, %al
    movb %al, (%rdx)
    ret
