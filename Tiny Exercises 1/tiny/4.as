	.data
newline:	.asciiz "\n"
var0:		.word 0
	.text
	.align 2
	.globl main
main:
	li	$s0,5
	lw	$s1,var-1
	add	$s2,$s0,$s1
	sw	$s2,var0
	li	$v0,10
	syscall

