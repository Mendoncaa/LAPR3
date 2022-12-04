.section .text
    .global sens_velc_vento

sens_velc_vento:
    addb %sil, %dil
    movb %dil, %al
  
    
    ret
