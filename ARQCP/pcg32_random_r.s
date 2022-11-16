.section .text
	.global pcg32_random_r

.section .data
	.global inc
	.global state
	.global oldstate
	.global xorshifted
	.global rot
	
pcg32_random_r:

		movq inc(%rip), %rsi
		movq state(%rip), %rdi
		
		movq $0, %rdi
		movq $0 %rsi
		
		movq oldstate(%rip), %rcx
		movl xorshifted(%rip), %edx
		movl rot(%rip), %eax
		
		movq %rcx,%rdi
		imull %rdi, 
