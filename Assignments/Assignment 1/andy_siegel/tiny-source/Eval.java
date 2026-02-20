/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */
import java.io.*;
import java.util.*;

public class Eval {
    Sem sem;
    int[] memory;        // Variable store.
    HashMap<String, Integer> labelMap;
    int pc;
    STATSEQ currentStats;
    STATSEQ statsHead;   // Start of the statement sequence
    
    public Eval (Sem sem) {
       this.sem = sem;
       labelMap = new HashMap<>();
       program((PROGRAM) sem.ast);
    }

    void buildLabelMap(STATSEQ n) {
       int index = 0;
       STATSEQ current = n;
       while (!(current instanceof NULL)) {
           if (current.stat instanceof LABEL) {
               LABEL label = (LABEL) current.stat;
               labelMap.put(label.label, index);
           }
           index++;
           current = current.next;
       }
    }

    // Start evaluating an AST at the root, PROGRAM, node.
    // We must have performed semantic analysis before
    // the evaluation, so that variables have been assigned
    // identifier numbers. These numbers are used to index
    // 'memory', an array that holds current variable values.
    void program(PROGRAM n) {
       memory = new int[sem.sytab.size()];
       statsHead = n.stats;
       buildLabelMap(n.stats);
       stats(n.stats);
    }


    void stats(STATSEQ n) {
       currentStats = n;
       while (!(currentStats instanceof NULL)) {
           stat(currentStats.stat);
           if (currentStats == null) break; // Jump occurred
           currentStats = currentStats.next;
       }
    }

    void stat(STAT n) {
       if (n instanceof ASSIGN) 
          assign((ASSIGN)n);
       else if (n instanceof PRINT) 
          print((PRINT)n);
       else if (n instanceof GOTO)
          jump((GOTO) n);
       else if (n instanceof IF)
          branch((IF) n);
       else if (n instanceof LABEL)
          label((LABEL) n);
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

    void jump(GOTO n) {
        jumpToLabel(n.label);
    }

    void branch(IF n) {
        int v = expr(n.expr);
        if(v != 0) {
            jumpToLabel(n.label);
        }
    }

    void label(LABEL n) {
        // we dont need to do anything for labels
    }
    
    void jumpToLabel(String label) {
        Integer targetIndex = labelMap.get(label);
        if (targetIndex == null) {
            System.err.println("Label not found: " + label);
            return;
        }
        
        STATSEQ temp = currentStats;
        int currentIndex = 0;
        // get reference to first statement for jump
        temp = getStatsFromStart();
        
        for (int i = 0; i < targetIndex && !(temp instanceof NULL); i++) {
            temp = temp.next;
        }
        
        currentStats = temp;
    }
    
    STATSEQ getStatsFromStart() {
        // keep a reference to the beginning of the sequence of statements to be able to iterate thru consistently and jump
        return statsHead;
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


