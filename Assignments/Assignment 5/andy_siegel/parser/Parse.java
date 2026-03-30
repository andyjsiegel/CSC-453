package parser;

import ast.*;
import lexer.*;
import java.io.*;

public class Parse {

    Lex scanner;
    Token current;

    public Parse(Lex s) {
        scanner = s;
        current = scanner.nextToken();
    }

    /********************************************************/
    /*                   Token Helpers                      */
    /********************************************************/
    boolean lookahead(int tok) {
        return current.getType() == tok;
    }

    boolean lookahead(TokenSet toks) {
        return toks.member(current);
    }

    int pos() {
        return current.getPosition();
    }

    String lex() {
        return current.getLexeme();
    }

    void match(int expected) {
        match(new TokenSet(expected));
    }

    void match(TokenSet expected) {
        if (lookahead(expected)) {
            current = scanner.nextToken();
        } else {
            String err = "<SYNTAX_ERROR " +
                "pos=\"" + pos() + "\" " +
                "expected=\"" + expected.toString(false) + "\" " +
                "found=\"" + Token.token2stringPrint(current.getType()) + "\"/>";
            System.out.println(err);
            System.exit(0);
        }
    }

    /********************************************************/
    /*                FIRST and FOLLOW sets                 */
    /********************************************************/
    TokenSet FIRST_decl = new TokenSet(new int[]{
        Token.TYPE, Token.VAR, Token.CONST, Token.PROCEDURE
    });

    TokenSet FIRST_stat = new TokenSet(new int[]{
        Token.IDENT, Token.READ, Token.WRITE, Token.WRITELN,
        Token.IF, Token.WHILE, Token.REPEAT, Token.LOOP, Token.EXIT
    });

    TokenSet FIRST_expr = new TokenSet(new int[]{
        Token.LPAREN, Token.MINUS, Token.NOT, Token.TRUNC,
        Token.FLOAT, Token.INTLIT, Token.REALLIT,
        Token.STRINGLIT, Token.CHARLIT, Token.IDENT
    });

    TokenSet FIRST_primary = new TokenSet(new int[]{
        Token.LPAREN, Token.INTLIT, Token.REALLIT,
        Token.STRINGLIT, Token.CHARLIT, Token.IDENT
    });

    TokenSet REL_OPS = new TokenSet(new int[]{
        Token.EQ, Token.NE, Token.LT, Token.LE, Token.GT, Token.GE
    });

    /********************************************************/
    /*                      Program                         */
    /********************************************************/
    public PROGRAM program() {
        match(Token.PROGRAM);
        String name = lex();
        int p = pos();
        match(Token.IDENT);
        match(Token.SEMICOLON);
        DECLS decls = declList();
        match(Token.BEGIN);
        STATS stats = statList();
        match(Token.END);
        match(Token.PERIOD);
        return new PROGRAM(name, decls, stats, p);
    }

    /********************************************************/
    /*                     Declarations                     */
    /*                                                      */
    /* IMPORTANT: decl-parsing methods do NOT consume the  */
    /* trailing ';'. declList() consumes it and uses its   */
    /* position as the DECLS node position.                */
    /********************************************************/
    DECLS declList() {
        if (lookahead(FIRST_decl)) {
            DECLARATION d = decl();
            int p = pos();           // current token is ';'
            match(Token.SEMICOLON);
            DECLS rest = declList();
            return new DECLS(d, rest, p);
        } else {
            return new DECLNULL(pos());
        }
    }

    DECLARATION decl() {
        if (lookahead(Token.TYPE))      return typeDecl();
        else if (lookahead(Token.VAR))  return varDecl();
        else if (lookahead(Token.CONST)) return constDecl();
        else                            return procDecl();
    }

    // TYPE ident = typeSpec   (no trailing ';')
    DECLARATION typeDecl() {
        match(Token.TYPE);
        String name = lex();
        int p = pos();
        match(Token.IDENT);
        match(Token.EQ);
        return typeSpec(name, p);
    }

    DECLARATION typeSpec(String name, int p) {
        if (lookahead(Token.RECORD)) {
            match(Token.RECORD);
            match(Token.LBRACK);
            DECLS fields = fieldList();
            match(Token.RBRACK);
            return new RECORDDECL(name, fields, p);
        } else if (lookahead(Token.ARRAY)) {
            match(Token.ARRAY);
            EXPRESSION count = expression();
            match(Token.OF);
            String elemType = lex();
            match(Token.IDENT);
            return new ARRAYDECL(name, count, elemType, p);
        } else {
            match(new TokenSet(Token.RECORD, Token.ARRAY));
            return null; // unreachable
        }
    }

    // Field list for RECORD types
    DECLS fieldList() {
        if (lookahead(Token.IDENT)) {
            String fname = lex();
            int fp = pos();
            match(Token.IDENT);
            match(Token.COLON);
            String ftype = lex();
            match(Token.IDENT);
            FIELDDECL f = new FIELDDECL(fname, ftype, fp);
            return fieldListTail(f);
        } else {
            return new DECLNULL(pos());
        }
    }

    DECLS fieldListTail(FIELDDECL f) {
        if (lookahead(Token.SEMICOLON)) {
            int p = pos(); // ';' position
            match(Token.SEMICOLON);
            String fname = lex();
            int fp = pos();
            match(Token.IDENT);
            match(Token.COLON);
            String ftype = lex();
            match(Token.IDENT);
            FIELDDECL next = new FIELDDECL(fname, ftype, fp);
            DECLS rest = fieldListTail(next);
            return new DECLS(f, rest, p);
        } else {
            // Last field: wrap in DECLS using the field's own position
            return new DECLS(f, new DECLNULL(pos()), f.position);
        }
    }

    // VAR ident : type   (no trailing ';')
    VARDECL varDecl() {
        int p = pos(); // VAR position
        match(Token.VAR);
        String name = lex();
        match(Token.IDENT);
        match(Token.COLON);
        String type = lex();
        match(Token.IDENT);
        return new VARDECL(name, type, p);
    }

    // CONST ident : type = expr   (no trailing ';')
    CONSTDECL constDecl() {
        int p = pos(); // CONST position
        match(Token.CONST);
        String name = lex();
        match(Token.IDENT);
        match(Token.COLON);
        String type = lex();
        match(Token.IDENT);
        match(Token.EQ);
        EXPRESSION expr = expression();
        return new CONSTDECL(name, type, expr, p);
    }

    // PROCEDURE ident ( formals ) ; declList BEGIN statList END
    // (no trailing ';' -- declList() will consume it)
    PROCDECL procDecl() {
        match(Token.PROCEDURE);
        String name = lex();
        int p = pos();
        match(Token.IDENT);
        match(Token.LPAREN);
        DECLS formals = formalList();
        match(Token.RPAREN);
        match(Token.SEMICOLON); // ';' after ')'
        DECLS decls = declList();
        match(Token.BEGIN);
        STATS stats = statList();
        match(Token.END);
        // Don't match final ';' -- declList() will do it
        return new PROCDECL(name, formals, decls, stats, p);
    }

    DECLS formalList() {
        if (lookahead(Token.VAR) || lookahead(Token.IDENT)) {
            return parseFormal();
        } else {
            return new DECLNULL(pos());
        }
    }

    // Build DECLS chain for formals, using ';' position as DECLS.pos
    DECLS parseFormal() {
        FORMALDECL f = formal();
        if (lookahead(Token.SEMICOLON)) {
            int p = pos(); // ';' between formals
            match(Token.SEMICOLON);
            DECLS rest = parseFormal();
            return new DECLS(f, rest, p);
        } else {
            // Last formal: use formal's own position
            return new DECLS(f, new DECLNULL(pos()), f.position);
        }
    }

    FORMALDECL formal() {
        String mode = "VAL";
        if (lookahead(Token.VAR)) {
            match(Token.VAR);
            mode = "VAR";
        }
        String name = lex();
        int p = pos(); // position of the formal parameter ident
        match(Token.IDENT);
        match(Token.COLON);
        String type = lex();
        match(Token.IDENT);
        return new FORMALDECL(name, type, mode, p);
    }

    /********************************************************/
    /*                      Statements                      */
    /*                                                      */
    /* statList() consumes the ';' after each statement and */
    /* uses its position as the STATS node position.        */
    /********************************************************/
    STATS statList() {
        if (lookahead(FIRST_stat)) {
            STATEMENT s = stat();
            int p = pos(); // current token is ';'
            match(Token.SEMICOLON);
            STATS rest = statList();
            return new STATS(s, rest, p);
        } else {
            return new STATNULL(pos());
        }
    }

    STATEMENT stat() {
        int p = pos();
        if (lookahead(Token.IDENT)) {
            VARREF des = designator();
            return statTail(des, p);
        } else if (lookahead(Token.READ)) {
            match(Token.READ);
            VARREF des = designator();
            return new READ(des, p);
        } else if (lookahead(Token.WRITE)) {
            match(Token.WRITE);
            EXPRESSION expr = expression();
            return new WRITE(expr, p);
        } else if (lookahead(Token.WRITELN)) {
            match(Token.WRITELN);
            return new WRITELN(p);
        } else if (lookahead(Token.IF)) {
            match(Token.IF);
            EXPRESSION expr = expression();
            match(Token.THEN);
            STATS then_ = statList();
            if (lookahead(Token.ELSE)) {
                match(Token.ELSE);
                STATS else_ = statList();
                match(Token.ENDIF);
                return new IF2(expr, then_, else_, p);
            } else {
                match(Token.ENDIF);
                return new IF1(expr, then_, p);
            }
        } else if (lookahead(Token.WHILE)) {
            match(Token.WHILE);
            EXPRESSION expr = expression();
            match(Token.DO);
            STATS stats = statList();
            match(Token.ENDDO);
            return new WHILE(expr, stats, p);
        } else if (lookahead(Token.REPEAT)) {
            match(Token.REPEAT);
            STATS stats = statList();
            match(Token.UNTIL);
            EXPRESSION expr = expression();
            return new REPEAT(expr, stats, p);
        } else if (lookahead(Token.LOOP)) {
            match(Token.LOOP);
            STATS stats = statList();
            match(Token.ENDLOOP);
            return new LOOP(stats, p);
        } else { // EXIT
            match(Token.EXIT);
            return new EXIT(p);
        }
    }

    STATEMENT statTail(VARREF des, int p) {
        if (lookahead(Token.COLONEQ)) {
            match(Token.COLONEQ);
            EXPRESSION expr = expression();
            return new ASSIGN(des, expr, p);
        } else if (lookahead(Token.LPAREN)) {
            match(Token.LPAREN);
            ACTUAL actuals = actualList(p);
            match(Token.RPAREN);
            return new PROCCALL(des, actuals, p);
        } else {
            match(new TokenSet(Token.COLONEQ, Token.LPAREN));
            return null; // unreachable
        }
    }

    ACTUAL actualList(int p) {
        if (lookahead(FIRST_expr)) {
            EXPRESSION expr = expression();
            ACTUAL rest = actualListTail(p);
            return new ACTUAL(expr, rest, p);
        } else {
            return new ACTUALNULL(p);
        }
    }

    ACTUAL actualListTail(int p) {
        if (lookahead(Token.COMMA)) {
            match(Token.COMMA);
            EXPRESSION expr = expression();
            ACTUAL rest = actualListTail(p);
            return new ACTUAL(expr, rest, p);
        } else {
            return new ACTUALNULL(p);
        }
    }

    /********************************************************/
    /*                     Designators                      */
    /********************************************************/
    VARREF designator() {
        String name = lex();
        int p = pos();
        match(Token.IDENT);
        DESIGNATOR next = designatorTail(p);
        return new VARREF(name, next, p);
    }

    // varPos: position of the VARREF, used for DESNULL
    DESIGNATOR designatorTail(int varPos) {
        if (lookahead(Token.PERIOD)) {
            match(Token.PERIOD);
            String fieldName = lex();
            int p = pos();
            match(Token.IDENT);
            DESIGNATOR next = designatorTail(varPos);
            return new FIELDREF(fieldName, next, p);
        } else if (lookahead(Token.LBRACK)) {
            int p = pos(); // position of '['
            match(Token.LBRACK);
            EXPRESSION idx = expression();
            match(Token.RBRACK);
            DESIGNATOR next = designatorTail(varPos);
            return new INDEX(idx, next, p);
        } else {
            return new DESNULL(varPos);
        }
    }

    /********************************************************/
    /*                     Expressions                      */
    /********************************************************/
    EXPRESSION expression() {
        EXPRESSION left = andExpr();
        while (lookahead(Token.OR)) {
            int p = pos();
            match(Token.OR);
            EXPRESSION right = andExpr();
            left = new BINARY(Token.OR, left, right, p);
        }
        return left;
    }

    EXPRESSION andExpr() {
        EXPRESSION left = relExpr();
        while (lookahead(Token.AND)) {
            int p = pos();
            match(Token.AND);
            EXPRESSION right = relExpr();
            left = new BINARY(Token.AND, left, right, p);
        }
        return left;
    }

    EXPRESSION relExpr() {
        EXPRESSION left = addExpr();
        if (lookahead(REL_OPS)) {
            int op = current.getType();
            int p = pos();
            match(REL_OPS);
            EXPRESSION right = addExpr();
            return new BINARY(op, left, right, p);
        }
        return left;
    }

    EXPRESSION addExpr() {
        EXPRESSION left = mulExpr();
        while (lookahead(Token.PLUS) || lookahead(Token.MINUS)) {
            int op = current.getType();
            int p = pos();
            match(new TokenSet(Token.PLUS, Token.MINUS));
            EXPRESSION right = mulExpr();
            left = new BINARY(op, left, right, p);
        }
        return left;
    }

    EXPRESSION mulExpr() {
        EXPRESSION left = unaryExpr();
        while (lookahead(Token.STAR) || lookahead(Token.SLASH) || lookahead(Token.PERCENT)) {
            int op = current.getType();
            int p = pos();
            match(new TokenSet(Token.STAR, Token.SLASH, Token.PERCENT));
            EXPRESSION right = unaryExpr();
            left = new BINARY(op, left, right, p);
        }
        return left;
    }

    EXPRESSION unaryExpr() {
        if (lookahead(Token.NOT)) {
            int p = pos();
            match(Token.NOT);
            return new UNARY(Token.NOT, unaryExpr(), p);
        } else if (lookahead(Token.MINUS)) {
            int p = pos();
            match(Token.MINUS);
            return new UNARY(Token.MINUS, unaryExpr(), p);
        } else if (lookahead(Token.TRUNC)) {
            int p = pos();
            match(Token.TRUNC);
            return new UNARY(Token.TRUNC, unaryExpr(), p);
        } else if (lookahead(Token.FLOAT)) {
            int p = pos();
            match(Token.FLOAT);
            return new UNARY(Token.FLOAT, unaryExpr(), p);
        } else if (lookahead(FIRST_primary)) {
            return primary();
        } else {
            match(FIRST_expr); // force error
            return null;       // unreachable
        }
    }

    EXPRESSION primary() {
        if (lookahead(Token.INTLIT)) {
            String val = lex(); int p = pos();
            match(Token.INTLIT);
            return new INTLIT(val, p);
        } else if (lookahead(Token.REALLIT)) {
            String val = lex(); int p = pos();
            match(Token.REALLIT);
            return new REALLIT(val, p);
        } else if (lookahead(Token.STRINGLIT)) {
            String val = lex(); int p = pos();
            match(Token.STRINGLIT);
            // Strip surrounding double quotes from lexeme
            if (val.length() >= 2 && val.charAt(0) == '"' && val.charAt(val.length()-1) == '"')
                val = val.substring(1, val.length()-1);
            return new STRINGLIT(val, p);
        } else if (lookahead(Token.CHARLIT)) {
            String val = lex(); int p = pos();
            match(Token.CHARLIT);
            // Strip surrounding single quotes from lexeme
            if (val.length() >= 2 && val.charAt(0) == '\'' && val.charAt(val.length()-1) == '\'')
                val = val.substring(1, val.length()-1);
            return new CHARLIT(val, p);
        } else if (lookahead(Token.LPAREN)) {
            match(Token.LPAREN);
            EXPRESSION e = expression();
            match(Token.RPAREN);
            return e;
        } else {
            return designator(); // VARREF extends DESIGNATOR extends EXPRESSION
        }
    }

    /********************************************************/
    /*                        Main                          */
    /********************************************************/
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Usage: luca_ast <input.luc> <output.xml> <output.gv>");
            System.exit(1);
        }
        String inputFile = args[0];
        String xmlFile   = args[1];
        String gvFile    = args[2];

        Lex scanner = new Lex(inputFile);
        Parse parser = new Parse(scanner);
        PROGRAM prog = parser.program();

        // Write XML
        PrintWriter xmlWriter = new PrintWriter(new FileWriter(xmlFile));
        xmlWriter.println("<block>");
        xmlWriter.print(prog.toString(0));
        xmlWriter.println("</block>");
        xmlWriter.close();

        // Write Graphviz
        Graphviz.clear();
        prog.toGraphviz();
        Graphviz.toFile("AST", gvFile);
    }
}
