package sem;
import java.io.BufferedWriter;
import java.lang.*;
import java.io.*;

public class Semantics {

   /********************************************************/
   /*                        Tracing                       */
   /********************************************************/
   static int level = 0;
   static BufferedWriter traceFile;

   static void openTraceFile (String traceFileName) {
      if (traceFileName != null) {
         try {
            File file = new File(traceFileName);
            traceFile = new BufferedWriter(new FileWriter(file));
         } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
         }
      }
   }

   static void closeTraceFile () {
      if (traceFile != null) {
         try {
            traceFile.close();
         } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
         }
      }
   }

   static void TRACE(String x, ast.AST e, String y, boolean args) {
      if (traceFile != null) {
         try {
            for(int i=0; i<level; i++) {
               traceFile.write("   ");
            };
            traceFile.write(x + e.getClass().getSimpleName()); 
            traceFile.write(y); 
            traceFile.newLine();
         } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
         }
      }
   }

   static void ENTER(ast.AST e) {
      TRACE("<", e, ">", true);
      level++;
   }

   static void EXIT(ast.AST e) {
      level--;
      TRACE("</", e, ">", false);
   }

/********************************************************/
/*                        Semantics                     */
/********************************************************/

public Semantics(String traceFileName) {
    openTraceFile(traceFileName);
}

public static void SemanticAnalysis(ast.AST E) {
    if (E instanceof ast.PROGRAM) {
       PROGRAM((ast.PROGRAM) E); 
    } else {
       closeTraceFile();
       aux.Error.Internal("SemanticAnalysis", "Node " + E.getClass().getName() + " unexpected.");
    };
}

public static void PROGRAM(ast.PROGRAM E) {
   ENTER(E);
   EXIT(E);
}

   public static void main (String args[]) throws IOException{
      if (args[0] == null) {
          throw new IOException("Missing input file");
      };

      String traceFile = null;
      if (args.length >= 2) {
          traceFile = args[1];
      };

      lexer.Lex scanner = new lexer.Lex(args[0]);
      parser.Parse parser = new parser.Parse(scanner,null);
      ast.PROGRAM ast = parser.program();
      sem.Semantics sem = new sem.Semantics(traceFile);
      sem.SemanticAnalysis(ast);

   }

}  
