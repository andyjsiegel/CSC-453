	.data
newline:	.asciiz "\n"
var0:		.word 0
	.text
	.globl main
main:
	li	$s0,1
	bne	$s0,$zero,L45
	li	$s1,99
	move	$a0,$s1
	li	$v0,1
	syscall
	la	$a0,newline
	li	$v0,4
	syscall
L45:
	li	$s0,33
	move	$a0,$s0
	li	$v0,1
	syscall
	la	$a0,newline
	li	$v0,4
	syscall
	li	$v0,10
	syscall

