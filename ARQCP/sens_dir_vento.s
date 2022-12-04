.section .text
    .global sens_dir_vento

sens_dir_vento:   
    
    cmpw $0, %di
    jl menor
    
    addw %si, %di
    
    jg maior

    movw %di, %ax

    ret
   

menor:
    addw $360, %di
    jmp sens_dir_vento

    ret

maior:
    subw $360, %di
    movw %di, %ax
    ret

