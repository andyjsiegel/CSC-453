#include<stdio.h>

#define PRINT_i       1	
#define PUSHCONST_i   2
#define PLUS_i        3
#define MINUS_i       4
#define MULT_i        5
#define EXIT          6

union stackNode {
   char* a;     // Address
   int i;       // Integer
   float f;     // Float
};

#define BINOP(op,tp) {\
           stack[sp-2].tp = stack[sp-2].tp op stack[sp-1].tp; \
           sp--; \
           pc++; \
           break; \
        }

void interpret(char prog[]) {
   union stackNode stack[100];
   int sp = 0;
   int pc = 0;

   while(1) {
      switch (prog[pc]) {
         case PLUS_i:   BINOP(+,i);
         case MINUS_i:  BINOP(-,i);
         case MULT_i:   BINOP(*,i);
         case PUSHCONST_i: {
            stack[sp].i = prog[pc+1]; \
            sp++; \
            pc += 2; \
            break; \
         };
         case PRINT_i: {
            printf("%i\n",stack[sp-1].i); \
            sp--; \
            pc++; \
            break; \
         }
         case EXIT: {
            return; \
         }
      };
   }
}

int main(int argc, char**argv) {
   char prog[] = {PUSHCONST_i,8,PUSHCONST_i,6,MULT_i,PUSHCONST_i, 6, MINUS_i, PRINT_i,EXIT};
   interpret(prog);
   return 0;
}
