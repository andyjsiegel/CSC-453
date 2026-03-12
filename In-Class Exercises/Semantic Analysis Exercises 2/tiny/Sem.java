/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

import java.io.*;
import java.util.*;

public class Sem {
    AST ast;

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

       /* BEGIN TASK_1 and TASK_2 */
       /* END TASK_1 and TASK_2 */
       decls(n.decls);
       System.out.println("Number of declarations: " + n.decls.countOut);
       System.out.println("Collected symbols: " + n.decls.idsOut.toString());

       /* BEGIN TASK_3 */
       /* END TASK_3 */

       stats(n.stats);
   }

    /******************************************************************************/
    /* Recursively walk a sequence a declarations. DECLNULL indicates the end of  */
    /* the sequence.                                                              */
    /******************************************************************************/
    void decls(DECLSEQ n) {
       if (n instanceof DECLNULL) {
          /* BEGIN TASK_1 and TASK_2 */
          /* END TASK_1 and TASK_2 */
       } else {
          n.decl.env = n.next.env = n.env;

          n.decl.idsIn = n.idsIn;
          Symbol sy = decl(n.decl);
  
          /* BEGIN TASK_1 and TASK_2 */
          /* END TASK_1 and TASK_2 */
          decls(n.next);
          /* BEGIN TASK_1 and TASK_2 */
          /* END TASK_1 and TASK_2 */
       }
    }

    /******************************************************************************/
    /* Walk one variable or constant declaration                                  */
    /* Context conditions:                                                        */
    /*    1) id should not be already declared                                    */
    /*    2) type should be one of "int" or "flt"                                 */
    /* Return:                                                                    */
    /*    1) the new symbol                                                       */
    /******************************************************************************/
    Symbol decl(DECL n) {
       if (n.idsIn.lookup(n.ident) != null) {
          ERROR("Identifier already declared: " + n.ident); 
       }

       if (!((n.type.equals("int")) || (n.type.equals("flt")))) {
            ERROR("No such type: " + n.type); 
       };

       Symbol sy =  null;
       n.env = Env.create().cons(n.idsIn);
       if (n instanceof VAR) { 
          sy = varDecl((VAR)n);
       } else if (n instanceof CONST) {
          sy = constDecl((CONST)n);
       };
       return sy;
    }

    /******************************************************************************/
    /* Process a variable declarations:                                           */
    /*    VAR id : type;                                                          */
    /* Return:                                                                    */
    /*    1) the new Symbol                                                       */
    /******************************************************************************/
    Symbol varDecl(VAR n) {
       Symbol sy =  new Variable(n.ident, n.type);
       return sy;
    }

    /******************************************************************************/
    /* Process a constant declaration:                                            */
    /*    CONST id : type = expr;                                                 */
    /* Context conditions:                                                        */
    /*    1) expr should be a constant expression                                 */
    /*    2) the type of expr (int or flt) should be the same same as type        */
    /* Return:                                                                    */
    /*    1) the new Symbol                                                       */
    /******************************************************************************/
    Symbol constDecl(CONST n) {
       n.expr.env = n.env;
       expr(n.expr);

       if (! n.expr.isConst) {
          ERROR("Constant declaration value not constant: " + n.ident); 
       };

       if (!n.expr.type.equals(n.type)) {
          ERROR("Constant declaration wrong type: " + n.ident + "; " + n.type + ", " + n.expr.type); 
       };

       Symbol sy = null;
       if (n.type.equals("int")) {
            sy = new Constant(n.ident, n.type, n.expr.ivalue);
        } else if (n.type.equals("flt")) {
            sy = new Constant(n.ident, n.type, n.expr.fvalue);
        } else {
            ERROR("No such type: " + n.type); 
        };
        return sy;
    }

    /******************************************************************************/
    /* Recursively walk a sequence of statements. STMTNULL indicates the end of   */
    /* the sequence.                                                              */
    /******************************************************************************/
    void stats(STATSEQ n) {
       if (n instanceof STATNULL) return;

       n.stat.env = n.next.env = n.env;

       stat(n.stat);
       stats(n.next);
    }

    /******************************************************************************/
    /* Proccess statement nodes.                                                  */
    /******************************************************************************/
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
    /* Process an assignment statement:                                           */
    /*    id = expr;                                                              */
    /* Context conditions:                                                        */
    /*    1) id should not have been declared                                     */
    /*    2) id should be declared to be a variable, not a constant               */
    /*    3) id and expr should be of the same type                               */
    /******************************************************************************/
    void assignStat(ASSIGN n) {
       n.expr.env = n.env;
       Symbol sy = n.env.lookup(n.ident);
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
    /* Process a print statement.                                                 */
    /*    PRINT expr;                                                             */
    /* Context conditions:                                                        */
    /******************************************************************************/
    void printStat(PRINT n) {
       n.expr.env = n.env;
       expr(n.expr);
    }

    /******************************************************************************/
    /* Process a label statement:                                                 */
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
    /* Process an IF statement:                                                   */
    /*    IF expr GOTO label;                                                     */
    /* Context conditions:                                                        */
    /*    1) label should be declared somewhere in the program                    */
    /*    2) expr should be a "bool" type                                         */
    /******************************************************************************/
    void ifStat(IF n) {
       n.expr.env = n.env;
       expr(n.expr);
/*       if (!n.expr.type.equals("bool")) {
          ERROR("Wrong IF type: " + n.expr.type);
       }
*/
    }

    /******************************************************************************/
    /* Process a GOTO statement:                                                  */
    /*    GOTO label;                                                             */
    /* Context conditions:                                                        */
    /*    1) label should be declared somewhere in the program                    */
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
    /* Process an identifier in an expression:                                    */
    /*    id = ... id ...;                                                        */
    /*    PRINT ... id ...;                                                       */
    /* The identifier will either be a variable or a constant.                    */
    /* Context conditions:                                                        */
    /*    1) id should be declared                                                */
    /* Actions:                                                                   */
    /*    1) look up the identifier in the current environment                    */
    /*    2) set n.type                                                           */
    /*    3) set n.isConst                                                        */
    /*    4) if id is declared to be a constant, set n.ivalue or n.fvalue         */
    /*       (depending on id's type) to id's value.                              */
    /******************************************************************************/
    void ident(IDENT n) {
       Symbol sy = n.env.lookup(n.ident);
       if (sy == null) {
          ERROR("Identifier not declared: " + n.ident);
       } {
         n.isConst = false;
         n.type = sy.type;

         if (sy instanceof Constant) {
            n.isConst = true;
            if (sy.type.equals("int")) {
               n.ivalue = ((Constant)sy).ivalue;
            } else {
               n.fvalue = ((Constant)sy).fvalue;
            }
          }
       }
    }

    /******************************************************************************/
    /* Process an integer literal in an expression:                               */
    /*    id = ... int ...;                                                       */
    /*    PRINT ... int ...;                                                      */
    /* Context conditions:                                                        */
    /* Actions:                                                                   */
    /*    1) Set n.type.                                                          */
    /*    2) Set n.ivalue and n.isConst.                                          */
    /******************************************************************************/
    void intlit(INTLIT n) {
       n.type = "int";

       n.ivalue = n.val;
       n.isConst = true;
    }

    /******************************************************************************/
    /* Process a float literal in an expression:                                  */
    /*    id = ... flt ...;                                                       */
    /*    PRINT ... flt ...;                                                      */
    /* Context conditions:                                                        */
    /* Actions:                                                                   */
    /*    1) Set n.type.                                                          */
    /*    2) Set n.fvalue and n.isConst.                                          */
    /******************************************************************************/
    void fltlit(FLTLIT n) {
       n.type = "flt";

       n.fvalue = n.val;
       n.isConst = true;
    }

    /******************************************************************************/
    /* Recursively walk a binary arithmetic subexpression:                        */
    /*    id = ... x op y ...;                                                    */
    /*    PRINT ... x op y ...;                                                   */
    /* Context conditions:                                                        */
    /*    1) x and y should be the same type, either int or flt                   */
    /* Actions:                                                                   */
    /*    1) Set n.type.                                                          */
    /*    2) Set n.ivalue and n.isConst.                                          */
    /******************************************************************************/
    void binop(BINOP n) {
       n.left.env = n.right.env = n.env;

       expr(n.left);
       expr(n.right);

       if (!n.left.type.equals(n.right.type)) {
          ERROR("Wrong binary type: " + n.left.type + "; " + n.right.type);
       }
       n.type = n.left.type;

       if ((n.left.isConst) && (n.right.isConst)) {
          n.isConst = true;
          if (n.left.type.equals("int")) {
             int ival = 0;
             switch (n.OP) {
                case Token.PLUS      : ival = n.left.ivalue + n.right.ivalue; break;
                case Token.MINUS     : ival = n.left.ivalue - n.right.ivalue; break;
                case Token.MULT      : ival = n.left.ivalue * n.right.ivalue; break;
                case Token.DIV       : ival = n.left.ivalue / n.right.ivalue; break;
                case Token.LT        : ival = (n.left.ivalue < n.right.ivalue)?1:0; break;
                default        : 
             };
             n.ivalue = ival;
          } else {
             float fval = 0;
             switch (n.OP) {
                case Token.PLUS      : fval = n.left.fvalue + n.right.fvalue; break;
                case Token.MINUS     : fval = n.left.fvalue - n.right.fvalue; break;
                case Token.MULT      : fval = n.left.fvalue * n.right.fvalue; break;
                case Token.DIV       : fval = n.left.fvalue / n.right.fvalue; break;
                case Token.LT        : fval = (n.left.fvalue < n.right.fvalue)?1:0; break;
                default        : 
             };
             n.fvalue = fval;
          }
       } else {
          n.isConst = false;
       }
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
