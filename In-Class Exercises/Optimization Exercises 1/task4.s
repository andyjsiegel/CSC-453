        .data
X:      .word 42
_nl:	.asciiz "\n"
        .text
        .globl main
main:
   la      $2, X
   la      $3, X
   lw      $4, 0($2)
   addiu   $4, $4, 1
   mulo    $4, $4, 2
   sll     $4, $4, 1
   sw      $4, 0($2)

   lw      $4, X
   neg     $4, $4
   neg     $4, $4
   move    $5, $4
   sw      $5, X

   lw      $a0, X
   li      $v0, 1        
   syscall

   li      $v0,  4        
   la      $a0,  _nl
   syscall

   lw      $4, X
   lw      $5, X
   sub     $4, $4, $5
   sw      $4, X

   lw      $a0, X
   li      $v0, 1        
   syscall

   li      $v0,  4        
   la      $a0,  _nl
   syscall
   
   li $v0, 10        
   syscall
