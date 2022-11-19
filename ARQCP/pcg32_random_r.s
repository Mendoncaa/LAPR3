.section .text
	.global pcg32_random_r

.section .data

.global inc
.global state

		
pcg32_random_r:

		##PROLOGO#
		pushq %rbp
		movq %rsp,%rbp

		movq state(%rip), %rax
		movq %rax, -8(%rbp)
		
		movq -8(%rbp), %rax
		movabsq $6364136223846793005, %rdx
		imulq %rdx,%rax
		movq inc(%rip), %rdx
		orq $1,%rdx
		addq %rdx, %rax
		movq %rax, state(%rip)
		
		movq -8(%rbp), %rax
		shrq $18, %rax
		xorq -8(%rbp), %rax
		shrq $27, %rax
		movl %eax, -12(%rbp)
		
		movq -8(%rbp), %rax
		shrq $59, %rax
		movl %eax, -16(%rbp)
		
		movl -16(%rbp), %eax
		movl -12(%rbp), %edx
		movl %eax, %ecx
		rorl %cl, %edx
		movl %edx, %eax
		
		
		##EPILOGO##
		popq %rbp
		
		ret
		
		
		
		
		
		
        
