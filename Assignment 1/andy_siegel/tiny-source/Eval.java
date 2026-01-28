/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */
import java.io.*;
import java.util.*;

public class Eval {
    Sem sem;
    int[] memory;        // Variable store.
    
    public Eval (Sem sem) {
       this.sem = sem;
       program((PROGRAM) sem.ast);
    }

    // Start evaluating an AST at the root, PROGRAM, node.
    // We must have performed semantic analysis before
    // the evaluation, so that variables have been assigned
    // identifier numbers. These numbers are used to index
    // 'memory', an array that holds current variable values.
    void program(PROGRAM n) {
       memory = new int[sem.sytab.size()];
       stats(n.stats);
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

    // Evaluate the expression, and assign the result to
    // the appropriate variable in 'memory'.
    void assign(ASSIGN n) {
        int v = expr(n.expr);
        memory[sem.sytab.lookup(n.ident)] = v;
    }

    // Evaluate the expression, and print the result.
    void print(PRINT n) {
       int v = expr(n.expr);
       System.out.println(v);
    }

    // Evaluate an expression.
    int expr(EXPR n) {
       if (n instanceof IDENT)
           return ident((IDENT) n);
       else if (n instanceof INTLIT)
           return intlit((INTLIT) n);
       else if (n instanceof BINOP)
           return binop((BINOP) n);
       return -1;
    }

    // Look up the identifier number, and return the current
    // value from the memory cell.
    int ident(IDENT n) {
        return memory[sem.sytab.lookup(n.ident)];
    }

    int intlit(INTLIT n) {
        return n.val;
    }

    // Evaluate an binary arithmetic expression.
   int binop(BINOP n) {
      int l = expr(n.left);
      int r = expr(n.right);
      if (n.OP == Token.PLUS)
          return l + r;
      if(n.OP == Token.MINUS)
        return l - r;
      if (n.OP == Token.LESSTHAN)
        return l < r ? 1 : 0;
      return -1;
    }

   public static void main (String args[]) throws Exception{
       Lex scanner = new Lex(args[0]);
       Parse parser = new Parse(scanner);
       Sem sem = new Sem(parser.ast);
       Eval eval = new Eval(sem);
    }
}


