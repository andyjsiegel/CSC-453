	.data
newline:	.asciiz "\n"
var0:		.word 0
var1:		.word 0
var2:		.word 0
var3:		.word 0
var4:		.word 0
var5:		.word 0
var6:		.word 0
var7:		.word 0
	.text
	.globl main
main:
	li	$s0,10
	sw	$s0,var0
	li	$s0,20
	sw	$s0,var1
	li	$s0,1
	sw	$s0,var2
	li	$s0,0
	lw	$s1,var0
	slt	$s2,$s0,$s1
	bne	$s2,$zero,L0
	li	$s3,0
	sw	$s3,var2
	li	$s0,0
	lw	$s1,var0
	sub	$s2,$s0,$s1
	sw	$s2,var0
L0:
	li	$s0,0
	sw	$s0,var4
L1:
	lw	$s0,var0
	li	$s1,0
	slt	$s2,$s0,$s1
	bne	$s2,$zero,L2
	lw	$s3,var4
	lw	$s4,var1
	add	$s5,$s3,$s4
	sw	$s5,var4
	lw	$s0,var0
	li	$s1,1
	sub	$s2,$s0,$s1
	sw	$s2,var0
	j	L1
L2:
	lw	$s0,var2
	bne	$s0,$zero,L3
	li	$s1,0
	lw	$s2,var4
	sub	$s3,$s1,$s2
	sw	$s3,var4
L3:
	lw	$s0,var4
	move	$a0,$s0
	li	$v0,1
	syscall
	la	$a0,newline
	li	$v0,4
	syscall
	li	$v0,10
	syscall

