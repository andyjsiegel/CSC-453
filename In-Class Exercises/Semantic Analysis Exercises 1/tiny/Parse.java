/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

import java.io.*;

public class Parse {
    Lex scanner;
    Token currentToken;
    public AST ast;

    public Parse (Lex scanner) {
       this.scanner = scanner;
       next();
       ast = parse();
    }

    void next() {
       currentToken = scanner.nextToken();
    }

    boolean lookahead(int tokenKind) {
       return currentToken.kind == tokenKind;
    }

    void match(int tokenKind) {
       if (!lookahead(tokenKind)) {
            System.err.println("Parsing error, line " + currentToken.position);
            System.exit(-1);
        }
        next();
    }

    // Build an AST node for either a variable reference
    // or a literal integer reference.
    EXPR factor() {
        EXPR expr = null;
        if (lookahead(Token.IDENT)) {
           expr = new IDENT(currentToken.ident, currentToken.position);
           match(Token.IDENT);
        } else if (lookahead(Token.INTLIT)) {
           expr = new INTLIT(currentToken.ivalue, currentToken.position);
           match(Token.INTLIT);
        } else if (lookahead(Token.FLTLIT)) {
           expr = new FLTLIT(currentToken.fvalue, currentToken.position);
           match(Token.FLTLIT);
        }
        return expr;
    }

    // Build an AST subtree for an expression.
   EXPR expr() {
      EXPR f = factor();
      while (true) {
         if (lookahead(Token.PLUS)) {
            match(Token.PLUS);
            EXPR e = factor();
            f = new BINOP(Token.PLUS, f, e, currentToken.position);
         } else if (lookahead(Token.MINUS)) {
            match(Token.MINUS);
            EXPR e = factor();
            f = new BINOP(Token.MINUS, f, e, currentToken.position);
         } else if (lookahead(Token.MULT)) {
            match(Token.MULT);
            EXPR e = factor();
            f = new BINOP(Token.MULT, f, e, currentToken.position);
         } else if (lookahead(Token.DIV)) {
            match(Token.DIV);
            EXPR e = factor();
            f = new BINOP(Token.DIV, f, e, currentToken.position);
         } else if (lookahead(Token.LT)) {
            match(Token.LT);
            EXPR e = factor();
            f = new BINOP(Token.LT, f, e, currentToken.position);
         } else
            break;
      }
      return f;
   }

   DECL varDecl() {
        match(Token.VAR);
        String ident = currentToken.ident;
        match(Token.IDENT);
        match(Token.COLON);
        String type = currentToken.ident;
        match(Token.IDENT);
        return new VAR(ident, type, currentToken.position);
   }

   DECL constDecl() {
        match(Token.CONST);
        String ident = currentToken.ident;
        match(Token.IDENT);
        match(Token.COLON);
        String type = currentToken.ident;
        match(Token.IDENT);
        match(Token.EQUAL);
        EXPR e = expr();
        return new CONST(ident, type, e, currentToken.position);
   }

    DECLSEQ decls() {
        DECL decl;
        if (lookahead(Token.VAR)) {
           decl = varDecl();
        } else if (lookahead(Token.CONST)) {
           decl = constDecl();
        } else 
           return new DECLNULL(currentToken.position);
        match(Token.SEMICOLON);
        DECLSEQ next = decls();
        return new DECLSEQ(decl, next, currentToken.position);
    }

    // Build an ASSIGN subtree.
   STAT assignStat() {
        String ident = currentToken.ident;
        match(Token.IDENT);
        match(Token.EQUAL);
        EXPR e = expr();
        return new ASSIGN(ident, e, currentToken.position);
   }

    // Build a PRINT subtree.
    STAT printStat() {
        match(Token.PRINT);
        EXPR e = expr();
        return new PRINT(e, currentToken.position);
    }

    // Build a LABEL subtree.
   STAT labelStat() {
        int lab = currentToken.ivalue;
        match(Token.INTLIT);
        match(Token.COLON);
        return new LABEL(lab, currentToken.position);
   }

    // Build an IF-statement subtree.
   STAT ifStat() {
        match(Token.IF);
        EXPR e = expr();
        match(Token.GOTO);
        int lab = currentToken.ivalue;
        match(Token.INTLIT);
        return new IF(e, lab, currentToken.position);
   }

    // Build a GOTO-statement subtree.
   STAT gotoStat() {
        match(Token.GOTO);
        int lab = currentToken.ivalue;
        match(Token.INTLIT);
        return new GOTO(lab, currentToken.position);
   }

    // Build a STATSEQ subtree. The bottom/rightmost
    // subtree will be a NULL node, indicating the
    // end of the statement sequence. 
    STATSEQ stats() {
        STAT stat;
        if (lookahead(Token.IDENT)) {
           stat = assignStat();
        } else if (lookahead(Token.PRINT)) {
           stat = printStat();
        } else if (lookahead(Token.INTLIT)) {
           stat = labelStat();
        } else if (lookahead(Token.IF)) {
           stat = ifStat();
        } else if (lookahead(Token.GOTO)) {
           stat = gotoStat();
        } else 
           return new STATNULL(currentToken.position);
        match(Token.SEMICOLON);
        STATSEQ next = stats();
        return new STATSEQ(stat, next, currentToken.position);
    }

    // Build a tree whose root is a PROGRAM node.
    AST parse() {
        int pos = currentToken.position;
        match(Token.BEGIN);
        DECLSEQ d = decls();
        STATSEQ s = stats();
        PROGRAM p = new PROGRAM(d,s, pos);
        match(Token.END);
        match(Token.EOF);
        return p;
    }

    public static void main (String args[]) throws IOException{
        Lex scanner = new Lex(args[0]);
        Parse parser = new Parse(scanner);
        System.out.println(parser.ast.toString());
    }
}
