/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

import java.io.*;
import java.util.*;

public class GenIR {
    Sem sem;
    int pc = 0;

    public GenIR (Sem sem) {
       this.sem = sem;
       program((PROGRAM) sem.ast);
    }

    // List of generated IR instructions.
    public int[] code = new int[100];
    void add(int instr) {
       code[pc++] = instr;
    }

    // Start generating IR code from the AST.
    void program(PROGRAM n) {
       add(IR.HEADER);  add(IR.MAGIC); add(sem.sytab.size());
       stats(n.stats);
       add(IR.EXIT);
    }

    void stats(STATSEQ n) {
       if (n instanceof NULL) return;
       stat(n.stat);
       stats(n.next);
    }

    void stat(STAT n) {
       if (n instanceof ASSIGN) 
          assign((ASSIGN)n);
       else if (n instanceof PRINT) 
          print((PRINT)n);
    }

    // Generate code for an assignment statement. We first
    // generate code that pushes the value of the expression
    // on the stack, then issue a 'STORE' instruction that
    // pops this value off the stack and assigns it to the
    // appropriate memory cell.
    void assign(ASSIGN n) {
        expr(n.expr);
        add(IR.STORE);
        add(sem.sytab.lookup(n.ident));
    }

    // Generate code for a print statement. We first
    // generate code that pushes the value of the expression
    // on the stack, then issue a 'PRINT' instruction which
    // pops this value off the stack and prints it.
    void print(PRINT n) {
       expr(n.expr);
       add(IR.PRINT);
       add(IR.PRINTLN);
    }

    void expr(EXPR n) {
       if (n instanceof IDENT)
         ident((IDENT) n);
       else if (n instanceof INTLIT)
         intlit((INTLIT) n);
       else if (n instanceof BINOP)
         binop((BINOP) n);
    }

    // Push the value of a variable.
    void ident(IDENT n) {
        add(IR.LOAD);
        add(sem.sytab.lookup(n.ident));
    }

    // Push an integer literal value.
    void intlit(INTLIT n) {
        add(IR.PUSH);
        add(n.val);
    }

    // Generate code for a binary arithmetic expression. First
    // generate code that pushes the value of the left hand
    // and right hand sides on the stack. Then generate an 'ADD'
    // instruction which pops these two values, adds them together,
    // and pushes the result.
    void binop(BINOP n) {
       expr(n.left);
       expr(n.right);
       if (n.OP == Token.PLUS)
           add(IR.ADD);
    }

    public void write () {
      IR.write(code,pc);
    }

    public static void main (String args[]) throws Exception{
       Lex scanner = new Lex(args[0]);
       Parse parser = new Parse(scanner);
       Sem sem = new Sem(parser.ast);
       GenIR ir = new GenIR(sem);
       ir.write();
    }
}


