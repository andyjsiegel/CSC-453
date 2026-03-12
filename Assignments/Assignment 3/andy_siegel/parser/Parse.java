package parser;

import java.lang.*;
import java.io.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Parse {

   lexer.Lex scanner;
   lexer.Token current;
   String parseTraceFileName;

   public Parse(lexer.Lex s, String t) {
      scanner = s;
      parseTraceFileName = t;
      current = scanner.nextToken();
      openTraceFile();
   };

   /********************************************************/
   /*                        Tracing                       */
   /********************************************************/
   int level = 0;
   PrintWriter traceWriter;
   boolean traceToFile = false;  // true when writing to a file rather than stdout

   void openTraceFile () {
      if (parseTraceFileName != null) {
         try {
            traceWriter = new PrintWriter(new BufferedWriter(new FileWriter(parseTraceFileName)));
            traceToFile = true;
         } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
         }
      } else {
         // Default: write trace to stdout
         traceWriter = new PrintWriter(System.out, true);
         traceToFile = false;
      }
   }

   void closeTraceFile () {
      if (traceWriter != null) {
         traceWriter.flush();
         if (traceToFile) {
            traceWriter.close();
         }
      }
   }

   void TRACE(String x, String e, String y, boolean args) {
      if (traceWriter != null) {
         StringBuilder sb = new StringBuilder();
         for (int i = 0; i < level; i++) {
            sb.append("   ");
         }
         sb.append(x).append(e);
         if (args) {
            sb.append(" token=\"").append(lexer.Token.token2string(current.getType())).append("\"");
            sb.append(" line=\"").append(current.getPosition()).append("\"");
         }
         sb.append(y);
         traceWriter.println(sb.toString());
      }
   }

   void ENTER(String e) {
      TRACE("<", e, ">", true);
      level++;
   }

   void EXIT(String e) {
      level--;
      TRACE("</", e, ">", false);
   }

   void MATCH() {
     TRACE("<", "MATCH", "/>", true);
   }

   /********************************************************/
   /*                        Matching                      */
   /********************************************************/
   boolean lookahead(int tok) {
        return current.getType() == tok;
   }

   boolean lookahead(lexer.TokenSet toks) {
      return toks.member(current);
   }

   void match(int expected) {
      match(new lexer.TokenSet(expected));
   }

   void match(lexer.TokenSet expected) {
      if (lookahead(expected)) {
         lexer.Token next = scanner.nextToken();
         MATCH();
         current = next;
      } else
         error(expected);
   }

   void error(lexer.TokenSet expected) {
      String err = "<SYNTAX_ERROR " +
                   "pos=\"" + current.getPosition() + "\" " +
                   "\n   expected=\"" + expected.toString(false) + "\" " +
                   "\n   found=\"" + lexer.Token.token2stringPrint(current.getType()) + "\"/>";
      closeTraceFile();
      System.out.println(err);
      System.exit(0);
   }

   /********************************************************/
   /*                FIRST and FOLLOW sets                 */
   /********************************************************/
   lexer.TokenSet FIRST_decl = new lexer.TokenSet(new int[]{
      lexer.Token.TYPE, lexer.Token.VAR, lexer.Token.CONST, lexer.Token.PROCEDURE
   });

   lexer.TokenSet FIRST_stat = new lexer.TokenSet(new int[]{
      lexer.Token.IDENT, lexer.Token.READ, lexer.Token.WRITE, lexer.Token.WRITELN,
      lexer.Token.IF, lexer.Token.WHILE, lexer.Token.REPEAT, lexer.Token.LOOP,
      lexer.Token.EXIT
   });

   // FIRST(expression) = FIRST(unaryExpr) = {(, -, NOT, TRUNC, FLOAT, intlit, reallit, stringlit, charlit, ident}
   lexer.TokenSet FIRST_expr = new lexer.TokenSet(new int[]{
      lexer.Token.LPAREN, lexer.Token.MINUS, lexer.Token.NOT, lexer.Token.TRUNC,
      lexer.Token.FLOAT, lexer.Token.INTLIT, lexer.Token.REALLIT,
      lexer.Token.STRINGLIT, lexer.Token.CHARLIT, lexer.Token.IDENT
   });

   // FIRST(primary) = {(, intlit, reallit, stringlit, charlit, ident}
   lexer.TokenSet FIRST_primary = new lexer.TokenSet(new int[]{
      lexer.Token.LPAREN, lexer.Token.INTLIT, lexer.Token.REALLIT,
      lexer.Token.STRINGLIT, lexer.Token.CHARLIT, lexer.Token.IDENT
   });

   lexer.TokenSet FIRST_typeSpec = new lexer.TokenSet(new int[]{
      lexer.Token.RECORD, lexer.Token.ARRAY
   });

   lexer.TokenSet REL_OPS = new lexer.TokenSet(new int[]{
      lexer.Token.EQ, lexer.Token.NE, lexer.Token.LT, lexer.Token.LE,
      lexer.Token.GT, lexer.Token.GE
   });

   /********************************************************/
   /*                      Program                         */
   /********************************************************/
   public void program() {
      ENTER("program");
      match(lexer.Token.PROGRAM);
      match(lexer.Token.IDENT);
      match(lexer.Token.SEMICOLON);
      declList();
      match(lexer.Token.BEGIN);
      statList();
      match(lexer.Token.END);
      match(lexer.Token.PERIOD);
      EXIT("program");
      closeTraceFile();
   }

   /********************************************************/
   /*                     Declarations                     */
   /********************************************************/
   void declList() {
      ENTER("declList");
      while (lookahead(FIRST_decl)) {
         decl();
      }
      EXIT("declList");
   }

   void decl() {
      ENTER("decl");
      if (lookahead(lexer.Token.TYPE)) {
         typeDecl();
      } else if (lookahead(lexer.Token.VAR)) {
         varDecl();
      } else if (lookahead(lexer.Token.CONST)) {
         constDecl();
      } else {
         procDecl();
      }
      EXIT("decl");
   }

   void typeDecl() {
      ENTER("typeDecl");
      match(lexer.Token.TYPE);
      match(lexer.Token.IDENT);
      match(lexer.Token.EQ);
      typeSpec();
      match(lexer.Token.SEMICOLON);
      EXIT("typeDecl");
   }

   void typeSpec() {
      ENTER("typeSpec");
      if (lookahead(lexer.Token.RECORD)) {
         match(lexer.Token.RECORD);
         match(lexer.Token.LBRACK);
         fieldList();
         match(lexer.Token.RBRACK);
      } else if (lookahead(lexer.Token.ARRAY)) {
         match(lexer.Token.ARRAY);
         expression();
         match(lexer.Token.OF);
         match(lexer.Token.IDENT);
      } else {
         match(FIRST_typeSpec);  // force error: expected ARRAY,RECORD
      }
      EXIT("typeSpec");
   }

   void fieldList() {
      ENTER("fieldList");
      if (lookahead(lexer.Token.IDENT)) {
         match(lexer.Token.IDENT);
         match(lexer.Token.COLON);
         match(lexer.Token.IDENT);
         fieldListTail();
      }
      EXIT("fieldList");
   }

   void fieldListTail() {
      ENTER("fieldListTail");
      while (lookahead(lexer.Token.SEMICOLON)) {
         match(lexer.Token.SEMICOLON);
         match(lexer.Token.IDENT);
         match(lexer.Token.COLON);
         match(lexer.Token.IDENT);
      }
      EXIT("fieldListTail");
   }

   void varDecl() {
      ENTER("varDecl");
      match(lexer.Token.VAR);
      match(lexer.Token.IDENT);
      match(lexer.Token.COLON);
      match(lexer.Token.IDENT);
      match(lexer.Token.SEMICOLON);
      EXIT("varDecl");
   }

   void constDecl() {
      ENTER("constDecl");
      match(lexer.Token.CONST);
      match(lexer.Token.IDENT);
      match(lexer.Token.COLON);
      match(lexer.Token.IDENT);
      match(lexer.Token.EQ);
      expression();
      match(lexer.Token.SEMICOLON);
      EXIT("constDecl");
   }

   void procDecl() {
      ENTER("procDecl");
      match(lexer.Token.PROCEDURE);
      match(lexer.Token.IDENT);
      match(lexer.Token.LPAREN);
      formalList();
      match(lexer.Token.RPAREN);
      match(lexer.Token.SEMICOLON);
      declList();
      match(lexer.Token.BEGIN);
      statList();
      match(lexer.Token.END);
      match(lexer.Token.SEMICOLON);
      EXIT("procDecl");
   }

   void formalList() {
      ENTER("formalList");
      if (lookahead(lexer.Token.VAR) || lookahead(lexer.Token.IDENT)) {
         formal();
         formalListTail();
      }
      EXIT("formalList");
   }

   void formalListTail() {
      ENTER("formalListTail");
      while (lookahead(lexer.Token.SEMICOLON)) {
         match(lexer.Token.SEMICOLON);
         formal();
      }
      EXIT("formalListTail");
   }

   void formal() {
      ENTER("formal");
      if (lookahead(lexer.Token.VAR)) {
         match(lexer.Token.VAR);
      }
      match(lexer.Token.IDENT);
      match(lexer.Token.COLON);
      match(lexer.Token.IDENT);
      EXIT("formal");
   }

   /********************************************************/
   /*                      Statements                      */
   /********************************************************/
   void statList() {
      ENTER("statList");
      while (lookahead(FIRST_stat)) {
         stat();
         match(lexer.Token.SEMICOLON);
      }
      EXIT("statList");
   }

   void stat() {
      ENTER("stat");
      if (lookahead(lexer.Token.IDENT)) {
         designator();
         statTail();
      } else if (lookahead(lexer.Token.READ)) {
         match(lexer.Token.READ);
         designator();
      } else if (lookahead(lexer.Token.WRITE)) {
         match(lexer.Token.WRITE);
         expression();
      } else if (lookahead(lexer.Token.WRITELN)) {
         match(lexer.Token.WRITELN);
      } else if (lookahead(lexer.Token.IF)) {
         match(lexer.Token.IF);
         expression();
         match(lexer.Token.THEN);
         statList();
         ifTail();
         match(lexer.Token.ENDIF);
      } else if (lookahead(lexer.Token.WHILE)) {
         match(lexer.Token.WHILE);
         expression();
         match(lexer.Token.DO);
         statList();
         match(lexer.Token.ENDDO);
      } else if (lookahead(lexer.Token.REPEAT)) {
         match(lexer.Token.REPEAT);
         statList();
         match(lexer.Token.UNTIL);
         expression();
      } else if (lookahead(lexer.Token.LOOP)) {
         match(lexer.Token.LOOP);
         statList();
         match(lexer.Token.ENDLOOP);
      } else {
         match(lexer.Token.EXIT);
      }
      EXIT("stat");
   }

   void statTail() {
      ENTER("statTail");
      if (lookahead(lexer.Token.COLONEQ)) {
         match(lexer.Token.COLONEQ);
         expression();
      } else if (lookahead(lexer.Token.LPAREN)) {
         match(lexer.Token.LPAREN);
         actualList();
         match(lexer.Token.RPAREN);
      } else {
         match(new lexer.TokenSet(lexer.Token.COLONEQ, lexer.Token.LPAREN));
      }
      EXIT("statTail");
   }

   void ifTail() {
      ENTER("ifTail");
      if (lookahead(lexer.Token.ELSE)) {
         match(lexer.Token.ELSE);
         statList();
      }
      EXIT("ifTail");
   }

   /********************************************************/
   /*                     Designator                       */
   /********************************************************/
   void designator() {
      ENTER("designator");
      match(lexer.Token.IDENT);
      designatorTail();
      EXIT("designator");
   }

   void designatorTail() {
      ENTER("designatorTail");
      while (lookahead(lexer.Token.PERIOD) || lookahead(lexer.Token.LBRACK)) {
         if (lookahead(lexer.Token.PERIOD)) {
            match(lexer.Token.PERIOD);
            match(lexer.Token.IDENT);
         } else {
            match(lexer.Token.LBRACK);
            expression();
            match(lexer.Token.RBRACK);
         }
      }
      EXIT("designatorTail");
   }

   /********************************************************/
   /*                     Expressions                      */
   /********************************************************/
   void actualList() {
      ENTER("actualList");
      if (lookahead(FIRST_expr)) {
         expression();
         actualListTail();
      }
      EXIT("actualList");
   }

   void actualListTail() {
      ENTER("actualListTail");
      while (lookahead(lexer.Token.COMMA)) {
         match(lexer.Token.COMMA);
         expression();
      }
      EXIT("actualListTail");
   }

   void expression() {
      ENTER("expression");
      andExpr();
      while (lookahead(lexer.Token.OR)) {
         match(lexer.Token.OR);
         andExpr();
      }
      EXIT("expression");
   }

   void andExpr() {
      ENTER("andExpr");
      relExpr();
      while (lookahead(lexer.Token.AND)) {
         match(lexer.Token.AND);
         relExpr();
      }
      EXIT("andExpr");
   }

   void relExpr() {
      ENTER("relExpr");
      addExpr();
      if (lookahead(REL_OPS)) {
         match(REL_OPS);
         addExpr();
      }
      EXIT("relExpr");
   }

   void addExpr() {
      ENTER("addExpr");
      mulExpr();
      while (lookahead(lexer.Token.PLUS) || lookahead(lexer.Token.MINUS)) {
         if (lookahead(lexer.Token.PLUS)) {
            match(lexer.Token.PLUS);
         } else {
            match(lexer.Token.MINUS);
         }
         mulExpr();
      }
      EXIT("addExpr");
   }

   void mulExpr() {
      ENTER("mulExpr");
      unaryExpr();
      while (lookahead(lexer.Token.STAR) || lookahead(lexer.Token.SLASH) || lookahead(lexer.Token.PERCENT)) {
         if (lookahead(lexer.Token.STAR)) {
            match(lexer.Token.STAR);
         } else if (lookahead(lexer.Token.SLASH)) {
            match(lexer.Token.SLASH);
         } else {
            match(lexer.Token.PERCENT);
         }
         unaryExpr();
      }
      EXIT("mulExpr");
   }

   void unaryExpr() {
      ENTER("unaryExpr");
      if (lookahead(lexer.Token.NOT)) {
         match(lexer.Token.NOT);
         unaryExpr();
      } else if (lookahead(lexer.Token.MINUS)) {
         match(lexer.Token.MINUS);
         unaryExpr();
      } else if (lookahead(lexer.Token.TRUNC)) {
         match(lexer.Token.TRUNC);
         unaryExpr();
      } else if (lookahead(lexer.Token.FLOAT)) {
         match(lexer.Token.FLOAT);
         unaryExpr();
      } else if (lookahead(FIRST_primary)) {
         primary();
      } else {
         match(FIRST_expr);  // force error with correct expected set
      }
      EXIT("unaryExpr");
   }

   void primary() {
      ENTER("primary");
      if (lookahead(lexer.Token.INTLIT)) {
         match(lexer.Token.INTLIT);
      } else if (lookahead(lexer.Token.REALLIT)) {
         match(lexer.Token.REALLIT);
      } else if (lookahead(lexer.Token.STRINGLIT)) {
         match(lexer.Token.STRINGLIT);
      } else if (lookahead(lexer.Token.CHARLIT)) {
         match(lexer.Token.CHARLIT);
      } else if (lookahead(lexer.Token.LPAREN)) {
         match(lexer.Token.LPAREN);
         expression();
         match(lexer.Token.RPAREN);
      } else {
         designator();
      }
      EXIT("primary");
   }

   /********************************************************/
   /*                     Main                             */
   /********************************************************/

   public static void main (String args[]) throws IOException {
      if (args.length == 0 || args[0] == null) {
         System.err.println("Usage: luca_parse <input.luc> [tracefile]");
         System.exit(1);
      }
      // If a second argument is given, write trace to that file;
      // otherwise trace goes to stdout (default in openTraceFile).
      String traceFile = (args.length > 1 && args[1] != null) ? args[1] : null;
      lexer.Lex scanner = new lexer.Lex(args[0]);
      parser.Parse parser = new parser.Parse(scanner, traceFile);
      parser.program();
   }
}
