/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

import java.io.*;
import java.util.*;

public class Opt {
    Sem sem;

    public Opt (Sem sem) {
       this.sem = sem;
       program((PROGRAM) sem.ast);
    }

    // Begin optimizing the AST at the root, PROGRAM, node.
    void program(PROGRAM n) {
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

    // Optimize the expression and replace the old
    // expression subtree with the new one.
    void assign(ASSIGN n) {
       n.expr = expr(n.expr);
    }

    // Optimize the expression and replace the old
    // expression subtree with the new one.
    void print(PRINT n) {
       n.expr = expr(n.expr);
    }

    EXPR expr(EXPR n) {
	if (n instanceof IDENT)
           return n;
	else if (n instanceof INTLIT)
           return n;
	else if (n instanceof BINOP)
           return binop((BINOP) n);
	return null;
    }

    // Optimize a binary arithmetic expression by replacing
    // 'BINOP(+,INTLIT,INTLIT)' with 'INTLIT(INTLIT+INTLIT)'.
    // This only partially works: 'x+5+6' is parsed as
    // 'BINOP(+,BINOP(+,IDENT(x),INTLIT(5)),INTLIT(6))', and
    // hence we will never see the '5+6' subn-expression.
    EXPR binop(BINOP n) {
        EXPR L = expr(n.left);
	EXPR R = expr(n.right);
        if ((n.OP == Token.PLUS) &&
	    (L instanceof INTLIT) &&
	    (R instanceof INTLIT)) {
            int l = ((INTLIT)L).val;
            int r = ((INTLIT)R).val;
            return new INTLIT(l+r);
	}
	return new BINOP(Token.PLUS, L, R);
    }

    public static void main (String args[]) throws IOException{
	Lex scanner = new Lex(args[0]);
        Parse parser = new Parse(scanner);
        Sem sem = new Sem(parser.ast);
        Opt opt = new Opt(sem);
        System.out.println(opt.sem.ast.toString());
    }
}
