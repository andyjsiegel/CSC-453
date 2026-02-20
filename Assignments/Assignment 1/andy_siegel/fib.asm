	.data
newline:	.asciiz "\n"
var0:		.word 0
var1:		.word 0
var2:		.word 0
var3:		.word 0
var4:		.word 0
var5:		.word 0
var6:		.word 0
	.text
	.globl main
main:
	li	$s0,11
	sw	$s0,var0
	li	$s0,0
	sw	$s0,var1
	li	$s0,1
	sw	$s0,var2
	li	$s0,0
	sw	$s0,var3
	j	L0
L0:
	lw	$s0,var0
	lw	$s1,var3
	slt	$s2,$s0,$s1
	bne	$s2,$zero,L1
	lw	$s3,var1
	lw	$s4,var2
	add	$s5,$s3,$s4
	sw	$s5,var5
	lw	$s0,var2
	sw	$s0,var1
	lw	$s0,var5
	sw	$s0,var2
	lw	$s0,var3
	li	$s1,1
	add	$s2,$s0,$s1
	sw	$s2,var3
	j	L0
L1:
	lw	$s0,var1
	move	$a0,$s0
	li	$v0,1
	syscall
	la	$a0,newline
	li	$v0,4
	syscall
	li	$v0,10
	syscall

