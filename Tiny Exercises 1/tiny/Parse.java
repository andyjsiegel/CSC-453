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
           expr = new IDENT(currentToken.ident);
           match(Token.IDENT);
        } else if (lookahead(Token.INTLIT)) {
           expr = new INTLIT(currentToken.value);
           match(Token.INTLIT);
        }
        return expr;
    }

    // Build an AST subtree for an expression.
   EXPR expr() {
      EXPR f = factor();
      while (lookahead(Token.PLUS) || lookahead(Token.TIMES)) { // ADDED IN CLASS
         if(lookahead(Token.PLUS)) { 
            match(Token.PLUS); // ADDED IN CLASS
            EXPR e = factor();
            f = new BINOP(Token.PLUS, f, e);
         }
         else if(lookahead(Token.TIMES)) {
            match(Token.TIMES); // ADDED IN CLASS
            EXPR e = factor();
            f = new BINOP(Token.TIMES, f, e);
         }
      }
      return f;
   }

    // Build an ASSIGN subtree.
   STAT assign() {
        String ident = currentToken.ident;
        match(Token.IDENT);
        match(Token.EQUAL);
        EXPR e = expr();
        return new ASSIGN(ident, e);
   }

    // Build a PRINT subtree.
    STAT print() {
        match(Token.PRINT);
        EXPR e = expr();
        return new PRINT(e);
    }

    // Build a STATSEQ subtree. The bottom/rightmost
    // subtree will be a NULL node, indicating the
    // end of the statement sequence. 
    STATSEQ stats() {
        STAT stat;
        if (lookahead(Token.IDENT)) {
           stat = assign();
        } else if (lookahead(Token.PRINT)) {
           stat = print();
        } else 
           return new NULL();
        match(Token.SEMICOLON);
        STATSEQ next = stats();
        return new STATSEQ(stat, next);
    }

    // Build a tree whose root is a PROGRAM node.
    AST parse() {
        match(Token.BEGIN);
        STATSEQ s = stats();
        PROGRAM p = new PROGRAM(s);
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
