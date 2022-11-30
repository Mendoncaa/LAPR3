.section data
    .global ptrdirvento

.section .text
    .global sens_dir_vento

sens_dir_vento:
    movq ptrdirvento(%rip), %rdx

    cmpw $0, %dil
    jl convertl

    addw %si, %di

    cmpw $360, %di
    jg convert2

    movw %di, %ax
    movw %ax, (%rdx)

convet1:
    addw $360, %di
    jmp sens_dir_vento

convert2:
    subw $360, %di
    movw %di, %ax
    movw %ax, (%rdx)
    ret