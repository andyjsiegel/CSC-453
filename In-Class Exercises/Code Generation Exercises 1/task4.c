        .data
_i:     .word 0        
_nl:    .asciiz "\n"
        .text
        .globl main
main:
  li       $14, 1
  sw       $14, _i

$32:
   lw      $a0, _i
   li      $v0, 1        
   syscall

   li      $v0,  4        
   la      $a0,  _nl
   syscall
   
   lw      $9, _i
   addu    $10, $9, 1
   sw      $10, _i
   ble     $10, 10, $32

   li $v0, 10        
   syscall
