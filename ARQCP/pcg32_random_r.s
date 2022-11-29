.section .text
	.global pcg32_random_r

.section .data

.global inc
.global state

pcg32_random_r:

        pushq   %rbp
        movq    %rsp, %rbp
        subq    $32, %rsp
        call    rand
        movl    %eax, %edx
        movl    %edx, %eax
        sarl    $31, %eax
        shrl    $26, %eax
        addl    %eax, %edx
        andl    $63, %edx
        subl    %eax, %edx
        movl    %edx, %eax
        cltq
        movq    %rax, -8(%rbp)
        call    rand
        movl    %eax, %edx
        movl    %edx, %eax
        sarl    $31, %eax
        shrl    $26, %eax
        addl    %eax, %edx
        andl    $63, %edx
        subl    %eax, %edx
        movl    %edx, %eax
        cltq
        movq    %rax, -16(%rbp)
        movq    -8(%rbp), %rax
        movq    %rax, -24(%rbp)
        movq    -24(%rbp), %rax
        movabsq $6364136223846793005, %rdx
        imulq   %rdx, %rax
        movq    -16(%rbp), %rdx
        orq     $1, %rdx
        addq    %rdx, %rax
        movq    %rax, -8(%rbp)
        movq    -24(%rbp), %rax
        shrq    $18, %rax
        xorq    -24(%rbp), %rax
        shrq    $27, %rax
        movl    %eax, -28(%rbp)
        movq    -24(%rbp), %rax
        shrq    $59, %rax
        movl    %eax, -32(%rbp)
        movl    -32(%rbp), %eax
        movl    -28(%rbp), %edx
        movl    %eax, %ecx
        rorl    %cl, %edx
        movl    %edx, %eax
        leave
        ret
		
		
		
		
		
		
        
