#include<stdio.h>

/**********************************************************************/
/* These are the operators.                                           */
/**********************************************************************/

#define ADD 0
#define SUB 1
#define MUL 2
#define DIV 3
#define LOADINT 4
#define PRINT 5
#define EXIT 6
#define IF_EQ 7
#define GOTO 8

char* Name[] = {"ADD","SUB","MUL","DIV","LOADINT","PRINT","EXIT","IF_EQ","GOTO"};

/**********************************************************************/
/* Register values are stored here.                                   */
/**********************************************************************/

// REGISTERS     R0   R1    R2    R3    R4    R5    R6
int regs[100] = {0,   0,    0,    0,    0,    0,    0};

/**********************************************************************/
/* The definition of the quadruple data structure.                    */
/**********************************************************************/

struct Quad {
   int result;
   int op;
   int arg1;
   int arg2;
};

/**********************************************************************/
/* Here are some example programs. Programs are arrays of Quad        */
/* structs.                                                           */
/**********************************************************************/

// PRINT 42+67
struct Quad code1[] = {
   {0, LOADINT, 42, 0}, // R0 = LOADINT 42
   {1, LOADINT, 67, 0}, // R1 = LOADINT 67
   {2, ADD,     0,  1}, // R2 = R1 + R1
   {0, PRINT,   2,  0}, // PRINT R2
   {0, EXIT,    0,  0}  // EXIT
};

// IF 42 == 43 THEN PRINT 1 ELSE PRINT 0
struct Quad code2[] = {
   {0, LOADINT, 42, 0}, // [0]: R0 = LOADINT 42
   {1, LOADINT, 43, 0}, // [1]: R1 = LOADINT 43
   {6, IF_EQ,   0,  1}, // [2]: IF R0 == R1 GOTO 6
   {3, LOADINT, 0,  0}, // [3]: R3 = LOADINT 0
   {0, PRINT,   3,  0}, // [4]; PRINT 1
   {8, GOTO,    0,  0}, // [5]: GOTO 8
   {3, LOADINT, 1,  0}, // [6]: R3 = LOADINT 1
   {0, PRINT,   3,  0}, // [7]: PRINT 1
   {0, EXIT,    0,  0}  // [8]: EXIT
};

// PRINT (42+67) + 11
struct Quad code3[] = {
   {0, LOADINT, 42, 0}, // R0 = LOADINT 42
   {1, LOADINT, 67, 0}, // R1 = LOADINT 67
   {2, ADD,     0,  1}, // R2 = R1 + R1
   {3, LOADINT, 11, 0}, // R3 = LOADINT 11
   {4, ADD,     2,  3}, // R4 = R2 + R3
   {0, PRINT,   4,  0}, // PRINT R4
   {0, EXIT,    0,  0}  // EXIT
};

/**********************************************************************/
/* Print the instructions in human readable form.                     */
/**********************************************************************/
void printQuad(int pc, struct Quad q) {
   int res = q.result;
   int a1  = q.arg1;
   int a2  = q.arg2;
   int op  = q.op;
   printf("[%i]: ", pc);
   switch (op) {
      case ADD:
      case SUB:
      case MUL:
      case DIV:     {printf("R%i = R%i %s R%i\n", res, a1, Name[op], a2); break;}
      case IF_EQ:   {printf("IF R%i = R%i GOTO %i\n", a1, a2, res);       break;}
      case GOTO:    {printf("GOTO %i\n", res);                            break;}
      case LOADINT: {printf("R%i = LOADINT %i\n", res, a1);               break;}
      case PRINT:   {printf("PRINT R%i\n", a1);                           break;}
      case EXIT:    {printf("EXIT\n"); return;                            break;}
   }
}

void print(struct Quad code[]) {
   int pc = 0;
   while (1) {
      printQuad(pc, code[pc]);
      if (code[pc].op == EXIT) return;
      pc++;
   }
}

/**********************************************************************/
/* Interpreter to execute a program.                                  */
/**********************************************************************/
void execute(struct Quad code[]) {
   int pc = 0;
   while (1) {
      int res = code[pc].result;
      int a1  = code[pc].arg1;
      int a2  = code[pc].arg2;
      switch (code[pc].op) {
         case ADD:     {regs[res] = regs[a1] + regs[a2]; pc++; break;}
         case SUB:     {regs[res] = regs[a1] - regs[a2]; pc++; break;}
         case MUL:     {regs[res] = regs[a1] * regs[a2]; pc++; break;}
         case DIV:     {regs[res] = regs[a1] / regs[a2]; pc++; break;}
         case IF_EQ:   {pc = (regs[a1] == regs[a2])?res:(pc+1);    break;}
         case GOTO:    {pc = res;                              break;}
         case LOADINT: {regs[res] = a1;                  pc++; break;}
         case PRINT:   {printf("%i\n", regs[a1]);        pc++; break;}
         case EXIT:    {return;                          pc++; break;}
      };
   }
}

/**********************************************************************/
/* Print the windows of the program. Use this code as a template for  */
/* the peephole optimizers.                                           */
/**********************************************************************/
#define WINDOW_SIZE 3

void printWindows(struct Quad code[]) {
   int pc = 0;
   printf("-----------------------------\n");
   while (1) {
      int res = code[pc].result;
      int a1  = code[pc].arg1;
      int a2  = code[pc].arg2;
      int op  = code[pc].op;
      for(int i=0; i<WINDOW_SIZE; i++) {
         if (code[pc].op == EXIT) return;
         printQuad(pc+i, code[pc+i]);
      };
      printf("-----------------------------\n");
      pc++;
   }
}

/**********************************************************************/
/* Testing.                                                           */
/**********************************************************************/
void show (char* title, struct Quad code[]) {
   printf("=================================================================\n");
   printf("= %s\n", title);
   printf("=================================================================\n");
   print(code);

   printf("\nExecution:\n");
   execute(code);

   printf("\nWindows:\n");
   printWindows(code);

   printf("\n");
}

/**********************************************************************/
/* TASK 3 local optimizations.                                        */
/**********************************************************************/

void constFold (struct Quad code[]) {
}

void removeJumps2Jumps (struct Quad code[]) {
}

void algebraicSimplification (struct Quad code[]) {
}

/**********************************************************************/
/* Main entry point.                                                  */
/**********************************************************************/
int main() {
    show("code3 before constFold", code3);
    constFold(code3);
    show("code3 aftery constFold", code3);
}

   
