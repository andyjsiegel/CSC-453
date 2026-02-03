package lexer;

import java.lang.*;
import java.io.*;

public class Lex {
   Input input; 

   public Lex(String filename) throws IOException {
      input = new Input(filename);
   }

   public Token nextToken () {
   }

   public static void main (String args[]) throws IOException{
      Lex scanner = new Lex(args[0]);
      System.out.println("<block>");
      while(true) {
         Token token = scanner.nextToken();
         System.out.println("   " + token.toString());
         if (token.kind == Token.EOF) break;
      }
      System.out.println("</block>");
   }

}
