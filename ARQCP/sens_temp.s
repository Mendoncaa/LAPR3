.section .text
    .global sens_temp

sens_temp:
    addb %sil, %dil 
    movb %dil, %al
    
    
    ret