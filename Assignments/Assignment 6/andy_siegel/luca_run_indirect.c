#include <stdlib.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#define DEBUG 0

/*************************************************************/
/* Opcode definitions.                                       */
/*************************************************************/
#define DEF_STRING       0	
#define DEF_GLOBALS      55
#define PROG_BEGIN       1	
#define PROG_END         2	

#define PUSHADDR_local   3
#define PUSHADDR_formal  4
#define PUSHADDR_global  5
#define FIELD            6
#define INDEX            7

#define PLUS_i           8
#define MINUS_i          9
#define MULT_i           10
#define DIV_i            11
#define EQ_i             12
#define GE_i             13
#define GT_i             14
#define LT_i             15
#define LE_i             16
#define NE_i             17
#define PUSHCONST_i      18
#define LOAD_i           19
#define STORE_i          20
#define UMINUS_i         21
#define MOD_i            56

#define PLUS_f           22
#define MINUS_f          23
#define MULT_f           24
#define DIV_f            25
#define EQ_f             26
#define GE_f             27
#define GT_f             28
#define LT_f             29
#define LE_f             30
#define NE_f             31
#define PUSHCONST_f      32
#define LOAD_f           33
#define STORE_f          34
#define UMINUS_f         35

#define TRUNC            36
#define FLOAT            37

#define PROC_BEGIN       38
#define PROC_END         39
#define CALL             40

#define JUMP             41
#define ACTUAL_i         42
#define ACTUAL_f         43

#define WRITE_i          44
#define WRITE_f          45
#define WRITE_c          46
#define WRITE_s          47

#define READ_i           48
#define READ_f           49
#define READ_c           50

#define WRITELN          51

#define ISA              52
#define NARROW           53
#define NEW              54

/*************************************************************/
/* Sizes of instructions and data. The compiler generates    */
/* VM code assuming certain sizes of basic types, typically  */
/* 32 or 64 bits for ints and floats. We've chosen to go with*/
/* 64 bits (8 bytes) uniformly for all types. It makes life  */
/* easier of addresses and integers are the same type, for   */
/* example.                                                  */               
/*************************************************************/

#define INT_TP                long
#define FLOAT_TP              double
#define ADDR_TP               unsigned long*

#define ARG_SIZE              8
#define OP_SIZE               1

#define INT_FORMAT            "%ld"
#define FLOAT_FORMAT          "%lf"

#define DEF_STRING_SIZE       (0)
#define DEF_GLOBALS_SIZE      (0)
#define PROG_BEGIN_SIZE       (OP_SIZE)
#define PROG_END_SIZE         (OP_SIZE)
#define PROC_BEGIN_SIZE       (OP_SIZE+2*ARG_SIZE)
#define PROC_END_SIZE         (OP_SIZE+2*ARG_SIZE)

#define PUSHADDR_SIZE         (OP_SIZE+ARG_SIZE)
#define FIELD_SIZE            (OP_SIZE+ARG_SIZE)
#define INDEX_SIZE            (OP_SIZE+2*ARG_SIZE)

// Size of PLUS_i, PLUS_f, etc
#define BINARITH_SIZE         (OP_SIZE)

// Size of UMINUS_i, etc
#define UNARITH_SIZE          (OP_SIZE)

// Size of EQ_i, EQ_f, etc
#define COND_SIZE             (OP_SIZE+ARG_SIZE)

// Size of PUSHCONST_i, etc
#define PUSHCONST_SIZE        (OP_SIZE+ARG_SIZE)

#define LOAD_SIZE             (OP_SIZE)
#define STORE_i_SIZE          (OP_SIZE)
#define UMINUS_i_SIZE         (OP_SIZE)

#define LOAD_f_SIZE           (OP_SIZE)
#define STORE_f_SIZE          (OP_SIZE)

#define TRUNC_SIZE            (OP_SIZE)
#define FLOAT_SIZE            (OP_SIZE)

#define CALL_SIZE             (OP_SIZE+2*ARG_SIZE)
#define ACTUAL_SIZE           (OP_SIZE)

#define JUMP_SIZE             (OP_SIZE+ARG_SIZE)

#define WRITE_SIZE            (OP_SIZE)
#define READ_SIZE             (OP_SIZE)
#define WRITELN_SIZE          (OP_SIZE)

/*************************************************************/
/* Initialization.                                           */
/*************************************************************/
char* op2string[100];

void init() {
      op2string[DEF_STRING     ]  = "DEF_STRING";
      op2string[DEF_GLOBALS    ]  = "DEF_GLOBALS";
      op2string[PROG_BEGIN     ]  = "PROG_BEGIN";
      op2string[PROG_END       ]  = "PROG_END";

      op2string[PUSHADDR_local ]  = "PUSHADDR_local";
      op2string[PUSHADDR_formal]  = "PUSHADDR_formal";
      op2string[PUSHADDR_global]  = "PUSHADDR_global";
      op2string[FIELD          ]  = "FIELD";
      op2string[INDEX          ]  = "INDEX";

      op2string[PLUS_i         ]  = "PLUS_i";
      op2string[MINUS_i        ]  = "MINUS_i";
      op2string[MULT_i         ]  = "MULT_i";
      op2string[DIV_i          ]  = "DIV_i";
      op2string[MOD_i          ]  = "MOD_i";
      op2string[EQ_i           ]  = "EQ_i";
      op2string[GE_i           ]  = "GE_i";
      op2string[GT_i           ]  = "GT_i";
      op2string[LT_i           ]  = "LT_i";
      op2string[LE_i           ]  = "LE_i";
      op2string[NE_i           ]  = "NE_i";
      op2string[PUSHCONST_i    ]  = "PUSHCONST_i";
      op2string[LOAD_i         ]  = "LOAD_i";
      op2string[STORE_i        ]  = "STORE_i";
      op2string[UMINUS_i       ]  = "UMINUS_i";

      op2string[PLUS_f         ]  = "PLUS_f";
      op2string[MINUS_f        ]  = "MINUS_f";
      op2string[MULT_f         ]  = "MULT_f";
      op2string[DIV_f          ]  = "DIV_f";
      op2string[EQ_f           ]  = "EQ_f";
      op2string[GE_f           ]  = "GE_f";
      op2string[GT_f           ]  = "GT_f";
      op2string[LT_f           ]  = "LT_f";
      op2string[LE_f           ]  = "LE_f";
      op2string[NE_f           ]  = "NE_f";
      op2string[PUSHCONST_f    ]  = "PUSHCONST_f";
      op2string[LOAD_f         ]  = "LOAD_f";
      op2string[STORE_f        ]  = "STORE_f";
      op2string[UMINUS_f       ]  = "UMINUS_f";

      op2string[TRUNC          ]  = "TRUNC";
      op2string[FLOAT          ]  = "FLOAT";

      op2string[PROC_BEGIN     ]  = "PROC_BEGIN";
      op2string[PROC_END       ]  = "PROC_END";
      op2string[CALL           ]  = "CALL";
      op2string[ACTUAL_i       ]  = "ACTUAL_i";
      op2string[ACTUAL_f       ]  = "ACTUAL_f";

      op2string[JUMP           ]  = "JUMP";

      op2string[ISA            ]  = "ISA";
      op2string[NARROW         ]  = "NARROW";
      op2string[NEW            ]  = "NEW";

      op2string[WRITE_i        ]  = "WRITE_i";
      op2string[WRITE_f        ]  = "WRITE_f";
      op2string[WRITE_c        ]  = "WRITE_c";
      op2string[WRITE_s        ]  = "WRITE_s";

      op2string[READ_i         ]  = "READ_i";
      op2string[READ_f         ]  = "READ_f";
      op2string[READ_c         ]  = "READ_c";

      op2string[WRITELN        ]  = "WRITELN";
}

/*************************************************************/
/* Read the VM file.                                         */
/* On completion:                                            */
/*    1) prog[] holds the bytecode read from path.           */
/*       While the input program is a pure textfile, the     */
/*       prog array is a binary representation. For example, */
/*       the "PUSHCONST_f 3.14" instruction will add the     */
/*       binary representation of 3.14 somewhere in prog[].  */
/*    2) programPC holds the index in prog[] where           */
/*       "main()" starts, i.e. the index of the PROG_BEGIN   */
/*       instruction.                                        */
/*    3) strings[] holds the constant strings defined in the */
/*       program, i.e. the strings provided in the           */
/*       DEF_STRING instructions.                            */
/*    4) globalSize holds the size of global variables       */
/*       from the DEF_GLOBALS instruction.                   */
/*************************************************************/

int programPC = 0;
char* strings[100];
int globalSize = 0;

#define READ_DEBUG 0

void read(char* path, char prog[]) {
   
   FILE *fptr = fopen(path, "r");
   if (fptr == NULL) {
      printf("Error opening file %s\n", path);
      exit(-1);
   }
   char buf[1000];
   char* pc = prog;
   char *dest[10];
   while(1) {
      char* r = fgets(buf, 1000, fptr);
      
      if (r == NULL) return;
      char *st = buf;
      int i=0;
      while (1) {
          if (i >= 10) break;
          char* token = strsep(&st, " ");
          if (token == NULL) break;
          if (token[0] == '#') break;
          dest[i] = token;
          i++;
       };

       int op;
       sscanf(dest[0], "%d", &op);

       int relativePC = (int)(pc - prog);

       if (op == PROG_BEGIN) {
          programPC = relativePC;
          if (READ_DEBUG) printf("READ PROG_BEGIN: programPC=%i\n", programPC);
       };
       if (op == PROC_BEGIN) {
          if (READ_DEBUG) printf("READ PROC_BEGIN: PC=%i\n", relativePC);
       };
       if (op == PUSHCONST_f) {
          *pc = op;
          pc += OP_SIZE;
          double arg;
          sscanf(dest[1], "%lf", &arg);
          FLOAT_TP* fltpc = (FLOAT_TP*)(pc);
          *fltpc = arg;
          pc += ARG_SIZE;
          if (READ_DEBUG) printf("READ FLOAT PC=%i; ARG = %lf\n", relativePC, arg);
       } else if (op == DEF_STRING) {
         int idx;
         sscanf(dest[1], "%d", &idx);
         int len = strlen(dest[2]) - 2;
         char* string = malloc(len);
         strncpy(string, dest[2]+1, len);
         string[len-1] = 0;
         strings[idx] = string;
         if (READ_DEBUG) printf("READ DEF_STRING: %i \"%s\"\n", idx, string);
       } else if (op == DEF_GLOBALS) {
         int size;
         sscanf(dest[1], "%d", &size);
         globalSize = size;
         if (READ_DEBUG) printf("READ DEF_GLOBALS: globalSize=%i\n", size);
       } else {
          *pc = op;
          if (READ_DEBUG) printf("READ PC=%i; OP = %d: %s\n", relativePC, op, op2string[op]);
          pc += OP_SIZE;
          for(int j=1; j<i; j++) {
             int arg;
             sscanf(dest[j], "%d", &arg);
             ADDR_TP intpc = (ADDR_TP)pc;
             *intpc = arg;
             if (READ_DEBUG) printf("    PC=%i; ARG %i = %d\n",(int) (pc-prog), j, arg);
             pc += ARG_SIZE;
          }
       }
   }
}

/*************************************************************/
/* The interpreter.                                          */
/*************************************************************/
union stackNode {
   char* a;     // Address
   INT_TP i;    // Integer
   FLOAT_TP f;  // Float
};


#define CALL_STACK_SIZE 4096
#define EVAL_STACK_SIZE 100

void interpret(char prog[]) {
   union stackNode stack[EVAL_STACK_SIZE];
   int sp = 0;
   int pc = programPC;

   char* globalMem = calloc(globalSize * ARG_SIZE + 16, 1);
   
   void* dispatch_table[60];
   dispatch_table[PROG_BEGIN]      = &&L_PROG_BEGIN;
   dispatch_table[PROG_END]        = &&L_PROG_END;
   dispatch_table[PROC_BEGIN]      = &&L_PROC_BEGIN;
   dispatch_table[PROC_END]        = &&L_PROC_END;
   dispatch_table[PUSHADDR_local]  = &&L_PUSHADDR_local;
   dispatch_table[PUSHADDR_formal] = &&L_PUSHADDR_formal;
   dispatch_table[PUSHADDR_global] = &&L_PUSHADDR_global;
   dispatch_table[FIELD]           = &&L_FIELD;
   dispatch_table[INDEX]           = &&L_INDEX;
   dispatch_table[PLUS_i]          = &&L_PLUS_i;
   dispatch_table[MINUS_i]         = &&L_MINUS_i;
   dispatch_table[MULT_i]          = &&L_MULT_i;
   dispatch_table[DIV_i]           = &&L_DIV_i;
   dispatch_table[MOD_i]           = &&L_MOD_i;
   dispatch_table[EQ_i]            = &&L_EQ_i;
   dispatch_table[GE_i]            = &&L_GE_i;
   dispatch_table[GT_i]            = &&L_GT_i;
   dispatch_table[LT_i]            = &&L_LT_i;
   dispatch_table[LE_i]            = &&L_LE_i;
   dispatch_table[NE_i]            = &&L_NE_i;
   dispatch_table[PUSHCONST_i]     = &&L_PUSHCONST_i;
   dispatch_table[LOAD_i]          = &&L_LOAD_i;
   dispatch_table[STORE_i]         = &&L_STORE_i;
   dispatch_table[UMINUS_i]        = &&L_UMINUS_i;
   dispatch_table[PLUS_f]          = &&L_PLUS_f;
   dispatch_table[MINUS_f]         = &&L_MINUS_f;
   dispatch_table[MULT_f]          = &&L_MULT_f;
   dispatch_table[DIV_f]           = &&L_DIV_f;
   dispatch_table[EQ_f]            = &&L_EQ_f;
   dispatch_table[GE_f]            = &&L_GE_f;
   dispatch_table[GT_f]            = &&L_GT_f;
   dispatch_table[LT_f]            = &&L_LT_f;
   dispatch_table[LE_f]            = &&L_LE_f;
   dispatch_table[NE_f]            = &&L_NE_f;
   dispatch_table[PUSHCONST_f]     = &&L_PUSHCONST_f;
   dispatch_table[LOAD_f]          = &&L_LOAD_f;
   dispatch_table[STORE_f]         = &&L_STORE_f;
   dispatch_table[UMINUS_f]        = &&L_UMINUS_f;
   dispatch_table[TRUNC]           = &&L_TRUNC;
   dispatch_table[FLOAT]           = &&L_FLOAT;
   dispatch_table[CALL]            = &&L_CALL;
   dispatch_table[ACTUAL_i]        = &&L_ACTUAL_i;
   dispatch_table[ACTUAL_f]        = &&L_ACTUAL_f;
   dispatch_table[JUMP]            = &&L_JUMP;
   dispatch_table[WRITE_i]         = &&L_WRITE_i;
   dispatch_table[WRITE_f]         = &&L_WRITE_f;
   dispatch_table[WRITE_c]         = &&L_WRITE_c;
   dispatch_table[WRITE_s]         = &&L_WRITE_s;
   dispatch_table[READ_i]          = &&L_READ_i;
   dispatch_table[READ_f]          = &&L_READ_f;
   dispatch_table[READ_c]          = &&L_READ_c;
   dispatch_table[WRITELN]         = &&L_WRITELN;
   dispatch_table[ISA]             = &&L_ISA;
   dispatch_table[NARROW]          = &&L_NARROW;
   dispatch_table[NEW]             = &&L_NEW;

   
   #define DISPATCH() goto *dispatch_table[(unsigned char)prog[pc]]

   DISPATCH();

   /**********************************************/
   /* Main program declaration.                  */
   /**********************************************/
   L_PROG_BEGIN:
      pc += PROG_BEGIN_SIZE;
      DISPATCH();
   L_PROG_END:
      free(globalMem);
      return;
   L_PROC_BEGIN:
      pc += PROC_BEGIN_SIZE;
      DISPATCH();
   L_PROC_END:
      pc += PROC_END_SIZE;
      DISPATCH();

   /**********************************************/
   /* Designators.                               */
   /**********************************************/
   
   L_PUSHADDR_local: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      stack[sp].a = offset * ARG_SIZE + globalMem;
      pc += PUSHADDR_SIZE;
      sp++;
      DISPATCH();
   }
   L_PUSHADDR_formal: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      stack[sp].a = offset * ARG_SIZE + globalMem;
      pc += PUSHADDR_SIZE;
      sp++;
      DISPATCH();
   }
   L_PUSHADDR_global: {
      INT_TP globalOffset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      stack[sp].a = globalOffset * ARG_SIZE + globalMem;
      pc += PUSHADDR_SIZE;
      sp++;
      DISPATCH();
   }
   
   L_FIELD: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      stack[sp-1].a += offset;
      pc += FIELD_SIZE;
      DISPATCH();
   }
   
   L_INDEX: {
      INT_TP elSz    = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      INT_TP elCount = (*(INT_TP*)(&prog[pc+OP_SIZE+ARG_SIZE]));
      INT_TP I = stack[sp-1].i;
      char*  B = stack[sp-2].a;
      if (I < 0 || I >= elCount) {
         printf("<RUNTIME_ERROR pos=\"X\" message=\"Array index out of range\"/>\n");
         exit(1);
      }
      stack[sp-2].a = B + elSz * I;
      sp--;
      pc += INDEX_SIZE;
      DISPATCH();
   }

   /**********************************************/
   /* Integer arithmetic.                        */
   /**********************************************/
   
   L_PLUS_i: {
      INT_TP B = stack[--sp].i;
      stack[sp-1].i += B;
      pc += BINARITH_SIZE;
      DISPATCH();
   }
   L_MINUS_i: {
      INT_TP B = stack[--sp].i;
      stack[sp-1].i -= B;
      pc += BINARITH_SIZE;
      DISPATCH();
   }
   L_MULT_i: {
      INT_TP B = stack[--sp].i;
      stack[sp-1].i *= B;
      pc += BINARITH_SIZE;
      DISPATCH();
   }
   
   L_DIV_i: {
      INT_TP B = stack[--sp].i;
      if (B == 0) {
         printf("<RUNTIME_ERROR pos=\"X\" message=\"Division by zero\"/>\n");
         exit(1);
      }
      stack[sp-1].i /= B;
      pc += BINARITH_SIZE;
      DISPATCH();
   }
   L_MOD_i: {
      INT_TP B = stack[--sp].i;
      if (B == 0) {
         printf("<RUNTIME_ERROR pos=\"X\" message=\"Division by zero\"/>\n");
         exit(1);
      }
      stack[sp-1].i %= B;
      pc += BINARITH_SIZE;
      DISPATCH();
   }
   
   
   L_EQ_i: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      INT_TP B = stack[--sp].i; INT_TP A = stack[--sp].i;
      if (A == B) pc += offset; else pc += COND_SIZE;
      DISPATCH();
   }
   L_GE_i: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      INT_TP B = stack[--sp].i; INT_TP A = stack[--sp].i;
      if (A >= B) pc += offset; else pc += COND_SIZE;
      DISPATCH();
   }
   L_GT_i: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      INT_TP B = stack[--sp].i; INT_TP A = stack[--sp].i;
      if (A > B) pc += offset; else pc += COND_SIZE;
      DISPATCH();
   }
   L_LT_i: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      INT_TP B = stack[--sp].i; INT_TP A = stack[--sp].i;
      if (A < B) pc += offset; else pc += COND_SIZE;
      DISPATCH();
   }
   L_LE_i: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      INT_TP B = stack[--sp].i; INT_TP A = stack[--sp].i;
      if (A <= B) pc += offset; else pc += COND_SIZE;
      DISPATCH();
   }
   L_NE_i: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      INT_TP B = stack[--sp].i; INT_TP A = stack[--sp].i;
      if (A != B) pc += offset; else pc += COND_SIZE;
      DISPATCH();
   }
   
   L_PUSHCONST_i: {
      stack[sp].i = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      sp++;
      pc += PUSHCONST_SIZE;
      DISPATCH();
   }
   
   L_LOAD_i: {
      stack[sp-1].i = *(INT_TP*)(stack[sp-1].a);
      pc += LOAD_SIZE;
      DISPATCH();
   }
   
   L_STORE_i: {
      INT_TP val = stack[--sp].i;
      *(INT_TP*)(stack[--sp].a) = val;
      pc += STORE_i_SIZE;
      DISPATCH();
   }
   
   L_UMINUS_i: {
      stack[sp-1].i = -stack[sp-1].i;
      pc += UNARITH_SIZE;
      DISPATCH();
   }

   /**********************************************/
   /* Float arithmetic.                          */
   /**********************************************/
   
   L_PLUS_f: {
      FLOAT_TP B = stack[--sp].f;
      stack[sp-1].f += B;
      pc += BINARITH_SIZE;
      DISPATCH();
   }
   L_MINUS_f: {
      FLOAT_TP B = stack[--sp].f;
      stack[sp-1].f -= B;
      pc += BINARITH_SIZE;
      DISPATCH();
   }
   L_MULT_f: {
      FLOAT_TP B = stack[--sp].f;
      stack[sp-1].f *= B;
      pc += BINARITH_SIZE;
      DISPATCH();
   }
   L_DIV_f: {
      FLOAT_TP B = stack[--sp].f;
      if (B == 0.0) {
         printf("<RUNTIME_ERROR pos=\"X\" message=\"Division by zero\"/>\n");
         exit(1);
      }
      stack[sp-1].f /= B;
      pc += BINARITH_SIZE;
      DISPATCH();
   }
   
   L_EQ_f: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      FLOAT_TP B = stack[--sp].f; FLOAT_TP A = stack[--sp].f;
      if (A == B) pc += offset; else pc += COND_SIZE;
      DISPATCH();
   }
   L_GE_f: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      FLOAT_TP B = stack[--sp].f; FLOAT_TP A = stack[--sp].f;
      if (A >= B) pc += offset; else pc += COND_SIZE;
      DISPATCH();
   }
   L_GT_f: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      FLOAT_TP B = stack[--sp].f; FLOAT_TP A = stack[--sp].f;
      if (A > B) pc += offset; else pc += COND_SIZE;
      DISPATCH();
   }
   L_LT_f: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      FLOAT_TP B = stack[--sp].f; FLOAT_TP A = stack[--sp].f;
      if (A < B) pc += offset; else pc += COND_SIZE;
      DISPATCH();
   }
   L_LE_f: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      FLOAT_TP B = stack[--sp].f; FLOAT_TP A = stack[--sp].f;
      if (A <= B) pc += offset; else pc += COND_SIZE;
      DISPATCH();
   }
   L_NE_f: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      FLOAT_TP B = stack[--sp].f; FLOAT_TP A = stack[--sp].f;
      if (A != B) pc += offset; else pc += COND_SIZE;
      DISPATCH();
   }
   
   L_PUSHCONST_f: {
      stack[sp].f = *(FLOAT_TP*)(&prog[pc+OP_SIZE]);
      sp++;
      pc += PUSHCONST_SIZE;
      DISPATCH();
   }
   L_LOAD_f: {
      stack[sp-1].f = *(FLOAT_TP*)(stack[sp-1].a);
      pc += LOAD_f_SIZE;
      DISPATCH();
   }
   L_STORE_f: {
      FLOAT_TP val = stack[--sp].f;
      *(FLOAT_TP*)(stack[--sp].a) = val;
      pc += STORE_f_SIZE;
      DISPATCH();
   }
   L_UMINUS_f: {
      stack[sp-1].f = -stack[sp-1].f;
      pc += UNARITH_SIZE;
      DISPATCH();
   }

   /**********************************************/
   /* TRUNC/FLOAT.                               */
   /**********************************************/
   L_TRUNC: {
      stack[sp-1].i = (INT_TP)stack[sp-1].f;
      pc += TRUNC_SIZE;
      DISPATCH();
   }
   L_FLOAT: {
      stack[sp-1].f = (FLOAT_TP)stack[sp-1].i;
      pc += FLOAT_SIZE;
      DISPATCH();
   }

   /**********************************************/
   /* PROCEDURE CALL.                            */
   /**********************************************/
   L_CALL:
      pc += CALL_SIZE;
      DISPATCH();
   L_ACTUAL_i:
      pc += ACTUAL_SIZE;
      DISPATCH();
   L_ACTUAL_f:
      pc += ACTUAL_SIZE;
      DISPATCH();

   /**********************************************/
   /* Branches.                                  */
   /**********************************************/
   L_JUMP: {
      INT_TP offset = (*(INT_TP*)(&prog[pc+OP_SIZE]));
      pc += offset;
      DISPATCH();
   }

   /**********************************************/
   /* IO.                                        */
   /**********************************************/
   L_WRITE_i:
      printf("%ld", stack[--sp].i);
      pc += WRITE_SIZE;
      DISPATCH();
   L_WRITE_f:
      printf("%lf", stack[--sp].f);
      pc += WRITE_SIZE;
      DISPATCH();
   L_WRITE_c:
      printf("%c", (char)stack[--sp].i);
      pc += WRITE_SIZE;
      DISPATCH();

   L_WRITE_s:
      printf("%s", strings[(int)stack[--sp].i]);
      pc += WRITE_SIZE;
      DISPATCH();
   L_READ_i: {
      char* addr = stack[--sp].a;
      scanf("%ld", (INT_TP*)addr);
      pc += READ_SIZE;
      DISPATCH();
   }
   L_READ_f: {
      char* addr = stack[--sp].a;
      scanf("%lf", (FLOAT_TP*)addr);
      pc += READ_SIZE;
      DISPATCH();
   }
   L_READ_c: {
      char* addr = stack[--sp].a;
      char c;
      scanf(" %c", &c);
      *(INT_TP*)addr = (INT_TP)(unsigned char)c;
      pc += READ_SIZE;
      DISPATCH();
   }
   L_WRITELN:
      printf("\n");
      pc += WRITELN_SIZE;
      DISPATCH();

   /**********************************************/
   /* OO.  IGNORE.                               */
   /**********************************************/
   L_ISA:
      pc += OP_SIZE;
      DISPATCH();
   L_NARROW:
      pc += OP_SIZE;
      DISPATCH();
   L_NEW:
      pc += OP_SIZE;
      DISPATCH();

   #undef DISPATCH
}

/*************************************************************/
/* Main.                                                     */
/*************************************************************/
int main(int argc, char**argv) {
   init();
   char prog[10000];
   read(argv[1], prog);
   interpret(prog);
   return 0;
}
