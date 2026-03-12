/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

import java.io.*;
import java.util.*;

public class Sem {
    AST ast;

    /* REMOVE THIS FOR TASK_3 */
    public SyTab sytab = new SyTab();

    java.util.HashSet labelsSeen = new java.util.HashSet();

    public Sem (AST ast) {
       this.ast = ast;
       program((PROGRAM) ast);
    }

    public void ERROR (String e) {
       System.out.println("[Semantic Error] " + e); 
       // System.exit(-1);
    }

    /******************************************************************************/
    /* Main entry point for the treewalker. Start walking the AST at the root,    */
    /* PROGRAM, node.                                                             */
    /******************************************************************************/
    void program(PROGRAM n) {
       /* BEGIN TASK_3 */
       /* END TASK_3 */

       decls(n.decls);
       stats(n.stats);
   }

    /******************************************************************************/
    /* Recursively walk a sequence a declarations. DECLNULL indicates the end of  */
    /* the sequence.                                                              */
    /******************************************************************************/
    void decls(DECLSEQ n) {
       if (n instanceof DECLNULL) return;

       /* BEGIN TASK_3 */
       /* END TASK_3 */

       decl(n.decl);
       decls(n.next);
    }

    /* Walk variable or constant declarations                                     */
    void decl(DECL n) {
       if (n instanceof VAR) 
          varDecl((VAR)n);
       else if (n instanceof CONST) 
          constDecl((CONST)n);
    }

    /******************************************************************************/
    /* Recursively walk a variable declarations:                                  */
    /*    VAR id : type;                                                          */
    /* Context conditions:                                                        */
    /*    1) id should not already be declared                                    */
    /*    2) type should be one of "int" or "flt"                                 */
    /* Actions:                                                                   */
    /*    1) insert id as a Variable node in the symbol table                     */
    /******************************************************************************/
    void varDecl(VAR n) {
       /* BEGIN TASK_3 */
       /* END TASK_3 */
       if (sytab.lookup(n.ident) != null) {
          ERROR("Identifier already declared: " + n.ident); 
       } else {
          Symbol sy = new Variable(n.ident, n.type);
          sytab.insert(sy);
       }
    }

    /******************************************************************************/
    /* Recursively walk a variable declarations:                                  */
    /*    CONST id : type = expr;                                                 */
    /* Context conditions:                                                        */
    /*    1) id is already declared                                               */
    /*    2) type is not one of "int" or "flt"                                    */
    /*    3) expr is not a constant expression                                    */
    /*    4) the type of expr (int or flt) is not the same as type (int or flt)   */
    /* Actions:                                                                   */
    /*    1) insert id as a Constant node in the symbol table                     */
    /******************************************************************************/
    void constDecl(CONST n) {
       /* BEGIN TASK_3 */
       /* END TASK_3 */
       if (sytab.lookup(n.ident) != null) {
          ERROR("Identifier already declared: " + n.ident); 
       } else {
          expr(n.expr);

          /* BEGIN TASK_2 */
         /* END TASK_2 */

         /* BEGIN TASK_1 */
         /* END TASK_1 */

         Symbol sy = null;
         if (n.type.equals("int")) {
              sy = new Constant(n.ident, n.type, n.expr.ivalue);
          } else if (n.type.equals("flt")) {
              sy = new Constant(n.ident, n.type, n.expr.fvalue);
          } else {
              ERROR("No such type: " + n.type); 
          };
          sytab.insert(sy);
       }
    }

    /******************************************************************************/
    /* Recursively walk a sequence a statements. STMTNULL indicates the end of    */
    /* the sequence.                                                              */
    /******************************************************************************/
    void stats(STATSEQ n) {
       if (n instanceof STATNULL) return;

       /* BEGIN TASK_3 */
       /* END TASK_3 */

       stat(n.stat);
       stats(n.next);
    }

    // Walk statement nodes
    void stat(STAT n) {
       if (n instanceof ASSIGN) 
          assignStat((ASSIGN)n);
       else if (n instanceof IF) 
          ifStat((IF)n);
       else if (n instanceof GOTO) 
          gotoStat((GOTO)n);
       else if (n instanceof LABEL) 
          labelStat((LABEL)n);
       else if (n instanceof PRINT) 
          printStat((PRINT)n);
    }

    /******************************************************************************/
    /* Recursively walk an assignment statement:                                  */
    /*    id = expr;                                                              */
    /* Context conditions:                                                        */
    /*    1) id should not have been declared                                     */
    /*    2) id should be declared to be a variable, not a constant               */
    /*    3) id and expr should be of the same type                               */
    /******************************************************************************/
    void assignStat(ASSIGN n) {
       /* BEGIN TASK_3 */
       /* END TASK_3 */
       Symbol sy = sytab.lookup(n.ident);
       if (sy == null) {
          ERROR("Identifier not declared: " + n.ident);
       } else if (sy instanceof Constant) {
          ERROR("Cannot assign to a constant: " + n.ident);
       } else {
          expr(n.expr);
          if (!n.expr.type.equals(sy.type)) {
             ERROR("Wrong assignment type: " + sy.type + "; " + n.expr.type);
          }
       }
    }

    /******************************************************************************/
    /* Recursively walk an assignment statement:                                  */
    /*    PRINT expr;                                                             */
    /* Context conditions:                                                        */
    /******************************************************************************/
    void printStat(PRINT n) {
       /* BEGIN TASK_3 */
       /* END TASK_3 */
       expr(n.expr);
    }

    /******************************************************************************/
    /* Recursively walk an assignment statement:                                  */
    /*    int:;                                                                   */
    /* Context conditions:                                                        */
    /*    1) label should not already be declared                                 */
    /******************************************************************************/
    void labelStat(LABEL n) {
       java.lang.Integer lab = java.lang.Integer.valueOf(n.label);
       if (labelsSeen.contains(lab)) {
          System.err.println("Multiply declared label: " + n.label);
       };
       labelsSeen.add(lab);
    }

    /******************************************************************************/
    /* Recursively walk an IF statement:                                          */
    /*    IF expr GOTO label;                                                     */
    /* Context conditions:                                                        */
    /*    1) label should not already be declared                                 */
    /*    2) expr should be a "bool" type                                         */
    /******************************************************************************/
    void ifStat(IF n) {
       /* BEGIN TASK_3 */
       /* END TASK_3 */
       expr(n.expr);
/*       if (!n.expr.type.equals("bool")) {
          ERROR("Wrong IF type: " + n.expr.type);
       }
*/
    }

    /******************************************************************************/
    /* Recursively walk a GOTO statement:                                         */
    /*    GOTO label;                                                             */
    /* Context conditions:                                                        */
    /*    1) label should be declared                                             */
    /******************************************************************************/
    void gotoStat(GOTO n) {
    }

    /******************************************************************************/
    /* Recursively walk an expression.                                            */
    /******************************************************************************/
    void expr(EXPR n) {
       if (n instanceof IDENT)
           ident((IDENT) n);
       else if (n instanceof INTLIT)
           intlit((INTLIT) n);
       else if (n instanceof FLTLIT)
           fltlit((FLTLIT) n);
       else if (n instanceof BINOP)
           binop((BINOP) n);
    }

    /******************************************************************************/
    /* Recursively walk an identifier in an expression:                           */
    /*    id = ... id ...;                                                        */
    /*    PRINT ... id ...;                                                       */
    /* Context conditions:                                                        */
    /*    1) id should be declared                                                */
    /* Actions:                                                                   */
    /*    1) look up the identifier in the symbol table                           */
    /*    2) set n.type                                                           */
    /*    3) set n.isConst                                                        */
    /*    4) if id is declared to be a constant, set n.ivalue or n.fvalue         */
    /*       (depending on id's type) to id's value.                              */
    /******************************************************************************/
    void ident(IDENT n) {
       /* BEGIN TASK_3 */
       /* END TASK_3 */
       Symbol sy = sytab.lookup(n.ident);
       if (sy == null) {
          ERROR("Identifier not declared: " + n.ident);
       } {
         /* BEGIN TASK_1 */
         /* END TASK_1 */

         if (sy instanceof Constant) {
            /* BEGIN TASK_2 */
            /* END TASK_2 */
          }
       }
    }

    /******************************************************************************/
    /* Recursively walk an integer literal in an expression:                      */
    /*    id = ... int ...;                                                       */
    /*    PRINT ... int ...;                                                      */
    /* Context conditions:                                                        */
    /* Actions:                                                                   */
    /*    1) Set n.type.                                                          */
    /*    2) Set n.ivalue and n.isConst.                                          */
    /******************************************************************************/
    void intlit(INTLIT n) {
       /* BEGIN TASK_1 */
       /* END TASK_1 */

       /* BEGIN TASK_2 */
       /* END TASK_2 */
    }

    /******************************************************************************/
    /* Recursively walk a float literal in an expression:                         */
    /*    id = ... flt ...;                                                       */
    /*    PRINT ... flt ...;                                                      */
    /* Context conditions:                                                        */
    /* Actions:                                                                   */
    /*    1) Set n.type.                                                          */
    /*    2) Set n.fvalue and n.isConst.                                          */
    /******************************************************************************/
    void fltlit(FLTLIT n) {
       /* BEGIN TASK_1 */
       /* END TASK_1 */

       /* BEGIN TASK_2 */
       /* END TASK_2 */
    }

    /******************************************************************************/
    /* Recursively walk a binary arithmetic operator in an expression:            */
    /*    id = ... x op y ...;                                                    */
    /*    PRINT ... x op y ...;                                                   */
    /* Context conditions:                                                        */
    /*    1) x and y should be the same type                                      */
    /* Actions:                                                                   */
    /*    1) Set n.type.                                                          */
    /*    2) Set n.ivalue and n.isConst.                                          */
    /******************************************************************************/
    void binop(BINOP n) {
       /* BEGIN TASK_3 */
       /* END TASK_3 */
       expr(n.left);
       expr(n.right);

       /* BEGIN TASK_1 */
       /* END TASK_1 */

       /* BEGIN TASK_2 */
       /* END TASK_2 */
    }

    /******************************************************************************/
    /* Main entrypoint for the semantic analyzer.                                 */
    /******************************************************************************/
    public static void main (String args[]) throws IOException{
        Lex scanner = new Lex(args[0]);
 
        // Parse and create an AST, parse.ast.
        Parse parser = new Parse(scanner);
        
        // Create a drawing of the AST in parse_ast.gv
        // To create the image, do
        //   dot -Tpng parse_ast.gv > parse_ast.png 
        Graphviz.clear();
        parser.ast.toGraphviz();
        Graphviz.toFile("AST","parse_ast.gv");

        // Run the semantic analyzer over the AST.
        Sem sem = new Sem(parser.ast);

        // Create a drawing of the AST in sem_ast.gv
        // To create the image, do
        //   dot -Tpng sem_ast.gv > sem_ast.png 
        Graphviz.clear();
        sem.ast.toGraphviz();
        Graphviz.toFile("AST","sem_ast.gv");
    }
}
