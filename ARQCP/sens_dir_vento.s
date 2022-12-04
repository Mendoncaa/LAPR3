.section .text
    .global sens_dir_vento

sens_dir_vento:   
    addw %si, %di
    cmpw $360, %di
    
    jg convert

    movw %di, %ax

    ret
   

convert:
    addw $360, %di
    movw %di, %ax
    jmp sens_dir_vento

    ret

