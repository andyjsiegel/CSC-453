/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

import java.lang.*;
import java.io.*;

public class Interpreter {

   // Evaluation stack.
   static int[] stack = new int[100];
   static int sp = 0;
   static void push (int v) {stack[sp++] = v;}
   static int pop() {return stack[--sp]; }

  static void run (int[] prog) throws Exception {
      int[] memory=null;
      int pc = 0;
      while (true) {
      switch (prog[pc]) {
      case IR.HEADER    : {
         if (prog[pc+1]!=IR.MAGIC) {
             System.err.println("Wrong magic number.");
             throw new Exception();
         }
         memory = new int[prog[pc+2]];
         pc+=3; break;
      }   
      case IR.ADD    : {
         int right = pop(); int left  = pop(); push(left+right); pc++; break;
      }   
      case IR.LOAD   : {
         push(memory[(int)prog[pc+1]]); pc+=2; break;
      }
      case IR.STORE  : {
         memory[prog[pc+1]] = pop(); pc+=2; break;
      }
      case IR.PUSH  : {
         push(prog[pc+1]); pc+=2; break;
      }
      case IR.PRINT  : {
         System.out.print(pop()); pc++; break;
      } 
      case IR.PRINTLN: {
         System.out.println(); pc++; break;
      } 
      case IR.EXIT   : {
        return;
      } 
      default : 
              System.err.println("Illegal instruction: " + prog[pc]);
              throw new Exception();
        }
      }
   }

    public static void main(String args[]) throws Exception{
        int[] code = IR.read(args[0]);
        run(code);
    }
}
