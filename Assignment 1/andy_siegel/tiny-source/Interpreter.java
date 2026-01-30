/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

import java.lang.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
public class Interpreter {

   static HashMap<Integer, Integer> buildLabelMap(int[] prog) {
      HashMap<Integer, Integer> labelMap = new HashMap<>();
      for (int i = 0; i < prog.length; i++) {
         if (prog[i] == IR.LABEL) {
               int labelId = prog[i + 1];
               labelMap.put(labelId, i + 2);  
               i++; 
         }
      }
      return labelMap;
   }


   // Evaluation stack.
   static int[] stack = new int[100];
   static int sp = 0;
   static void push (int v) {stack[sp++] = v;}
   static int pop() {return stack[--sp]; }

  static void run (int[] prog) throws Exception {
      HashMap<Integer, Integer> labelMap = buildLabelMap(prog);
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
      case IR.SUB    : {
         int right = pop(); int left = pop(); push(left-right); pc++; break;
      }   
      case IR.MUL    : {
         int right = pop(); int left  = pop(); push(left*right); pc++; break;
      }
      case IR.LT     : {
         int right = pop(); int left  = pop(); push(left < right ? 1 : 0); pc++; break;
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
      case IR.LABEL: {
         pc += 2; // skip labels
         break;
      }
      case IR.GOTO : {
         int labelId = prog[pc + 1];
         pc = labelMap.get(labelId);  // Jump to label address
         break;
      }
      case IR.BRANCH : {
         int condition = pop();
         int labelId = prog[pc + 1];
         if (condition != 0) {
            pc = labelMap.get(labelId);  // Jump if true
         } else {
            pc += 2;  // Skip to next instruction if false
         }
         break;
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
