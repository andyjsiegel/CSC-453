/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

import java.io.*;
import java.util.*;

public class GenC {
    Sem sem;

    public GenC (Sem sem) {
       this.sem = sem;
       program((PROGRAM) sem.ast);
    }

    public String code = "";
    void add(String instr) {
       code += instr + "\n";
    }

    // Start generating IR code from the AST.
    void program(PROGRAM n) {
       add("#include<stdio.h>");
       add("int main(char* args) {");
       add("   int vars[" + sem.sytab.size() + "];");
       stats(n.stats);
       add("   exit(0);");
       add("}");
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

    void assign(ASSIGN n) {
        String e = expr(n.expr);
        add("   vars[" + sem.sytab.lookup(n.ident) + "] = " + e + ";");
    }

    void print(PRINT n) {
       String e = expr(n.expr);
       add("   printf(\"%i\\n\"," + e + ");");
    }

    String expr(EXPR n) {
       if (n instanceof IDENT)
         return ident((IDENT) n);
       else if (n instanceof INTLIT)
         return intlit((INTLIT) n);
       else if (n instanceof BINOP)
         return binop((BINOP) n);
       return "";
    }

    String ident(IDENT n) {
        return "vars[" + sem.sytab.lookup(n.ident) + "]";
    }

    String intlit(INTLIT n) {
        return java.lang.Integer.toString(n.val);
    }

    String binop(BINOP n) {
       String l = expr(n.left);
       String r = expr(n.right);
       if (n.OP == Token.PLUS)
          return "(" + l + " + " + r + ")";
       else 
          return "";
    }

    public void write () {
      System.out.println(code);
    }

    public static void main (String args[]) throws Exception{
       Lex scanner = new Lex(args[0]);
       Parse parser = new Parse(scanner);
       Sem sem = new Sem(parser.ast);
       GenC ir = new GenC(sem);
       ir.write();
    }
}


