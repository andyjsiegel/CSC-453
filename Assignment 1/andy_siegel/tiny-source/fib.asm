	.data
newline:	.asciiz "\n"
var0:		.word 0
var1:		.word 0
var2:		.word 0
var3:		.word 0
	.text
	.globl main
main:
	li	$s0,11
	sw	$s0,var0
	li	$s0,0
	sw	$s0,var1
	j	L0
L0:
	lw	$s0,var0
	li	$s1,2
	slt	$s2,$s0,$s1
	bne	$s2,$zero,L1
	lw	$s3,var1
	lw	$s4,var0
	add	$s5,$s3,$s4
	sw	$s5,var1
L1:
	lw	$s0,var0
	li	$s1,1
	sub	$s2,$s0,$s1
	sw	$s2,var0
	j	L0
	lw	$s0,var0
	li	$s1,1
	sub	$s2,$s0,$s1
	sw	$s2,var0
	j	L0
	li	$v0,10
	syscall

