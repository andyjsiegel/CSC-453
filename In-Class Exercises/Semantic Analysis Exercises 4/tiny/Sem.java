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
     //  System.exit(-1);
    }

   /******************************************************************************/
   /* resultType(op,L,R) returns the resulting type when we apply the            */
   /* operator op to the types L and R.                                          */
   /******************************************************************************/
   TypeSy resultType (int op, TypeSy L, TypeSy R) {
      TypeSy result = Standard.NoType;
      if (op == Token.LT) {
         result = Standard.BoolType;
      } else if ((L == Standard.IntType) && (R == Standard.IntType)) {
         result = Standard.IntType;
      } else {
         result = Standard.RealType;
      }
      return result;
   }

   /******************************************************************************/
   /* ︎isArithmeticType(T) returns true if T is a type you can                    */
   /* perform arithmetic on.                                                     */
   /******************************************************************************/
   boolean isArithmeticType (TypeSy T) {
     return (T == Standard.IntType) || (T == Standard.RealType);
   }

   /******************************************************************************/
   /* ︎areCompatible(OP,L,R) returns true if you can apply the binary operator    */
   /* OP to variables of type L and R.                                           */
   /******************************************************************************/
   boolean areCompatible(int op, TypeSy L, TypeSy R){
      if ((op == Token.LT) && (isArithmeticType(L)) && (isArithmeticType(R))) {
         return true;
      } else {
        return L == R;
      }
   }

   /******************************************************************************/
   /* ︎areAssignmentCompatible(L,R) returns true if a value of type R             */
   /* can be assigned to a variable of type L.                                   */
   /******************************************************************************/
   boolean areAssignmentCompatible(TypeSy L, TypeSy R){
      boolean result = true;
      result = ((L == Standard.IntType) && (R == Standard.IntType)) ||
               ((L == Standard.RealType) && ((R == Standard.IntType) || (R == Standard.RealType)));
      return result;
   }

    /******************************************************************************/
    /* Main entry point for the treewalker. Start walking the AST at the root,    */
    /* PROGRAM, node.                                                             */
    /******************************************************************************/
    void program(PROGRAM n) {

       n.decls.idsIn = SyTab.create();
       Env env = Env.create().cons(Standard.SyTab());
       // System.out.println("program: env= " + env.toString());
       n.decls.env = env;
       decls(n.decls);
       // System.out.println("Collected symbols: " + n.decls.idsOut.toString());

       Env env1 = env.cons(n.decls.idsOut);
       n.stats.env = env1; 

       stats(n.stats);
   }

    /******************************************************************************/
    /* Recursively walk a sequence a declarations. DECLNULL indicates the end of  */
    /* the sequence.                                                              */
    /******************************************************************************/
    void decls(DECLSEQ n) {
       if (n instanceof DECLNULL) {
          n.idsOut = n.idsIn;
       } else {
          n.decl.env = n.next.env = n.env;

          n.decl.idsIn = n.idsIn;
          Symbol sy = decl(n.decl);
  
          n.next.idsIn = (sy!=null)?n.idsIn.add(sy):n.idsIn;
          n.next.env = n.env;
          decls(n.next);
          n.idsOut = n.next.idsOut;
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
/*
       if (!((n.type.equals("int")) || (n.type.equals("flt")))) {
            ERROR("No such type: " + n.type); 
       };
*/
       // System.out.println("decl: " + n.type);
       // System.out.println("env: "  + n.env.toString());
       Env env1 = n.env.cons(n.idsIn);
       Symbol tp = env1.lookup(n.type);
       if (tp == null) {
          ERROR("Type not declared: " + n.type); 
       };

       Symbol sy =  null;
       n.env = n.env.cons(n.idsIn);
       // System.out.println("decl: env=" + n.env.toString());
       if (n instanceof VAR) { 
          sy = varDecl((VAR)n);
       } else if (n instanceof CONST) {
          sy = constDecl((CONST)n);
       } else if (n instanceof ARRAY) {
          sy = arrayDecl((ARRAY)n);
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
       Symbol sy = null;

       Symbol tp = n.env.lookup(n.type);
       if (tp == null) {
          ERROR("No such type: " + n.type); 
       };

       if (! (tp instanceof TypeSy)) {
          ERROR("Type expected: " + n.type); 
       } else {
          sy = new Variable(n.ident, (TypeSy)tp);
       };

       return sy;
    }

    /******************************************************************************/
    /* Process an array declaration:                                              */
    /*    TYPE id = ARRAY count OF type;                                          */
    /* Return:                                                                    */
    /*    1) the new Symbol                                                       */
    /******************************************************************************/
    Symbol arrayDecl(ARRAY n) {
       Symbol elementType = n.env.lookup(n.type);
       if (!(elementType instanceof TypeSy)) {
          ERROR("Array element type expected: " + n.ident + "; " + n.type); 
       };

       n.expr.env = n.env;
       expr(n.expr);
       if (! n.expr.isConst) {
          ERROR("ARRAY size not constant: " + n.ident); 
       };
       int count = n.expr.ivalue;
       
       Symbol sy = new ArrayType(n.ident, (TypeSy)elementType, count);
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

       // System.out.println("constDecl: env=" + n.env.toString());
       Symbol tp = n.env.lookup(n.type);
       if (!(tp instanceof TypeSy)) {
          ERROR("Constant declaration type expected: " + n.ident + "; " + n.type); 
       };

       TypeSy type = (TypeSy) tp;

       if (n.expr.type != type) {
          ERROR("Constant declaration wrong type: " + n.ident + "; " + n.type + ", " + n.expr.type); 
       };

       Symbol sy = null;
       if (n.type.equals("int")) {
            sy = new Constant(n.ident, Standard.IntType, n.expr.ivalue);
        } else if (n.type.equals("flt")) {
            sy = new Constant(n.ident, Standard.RealType, n.expr.fvalue);
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
/*    void assignStatOLD(ASSIGN n) {
       n.expr.env = n.env;
       Symbol sy = n.env.lookup(n.ident);
       if (sy == null) {
          ERROR("Identifier not declared: " + n.ident);
       } else if (sy instanceof Constant) {
          ERROR("Cannot assign to a constant: " + n.ident);
       } else {
          expr(n.expr);
          
          if (! (areAssignmentCompatible(sy.type, n.expr.type))) {
             ERROR("Wrong assignment type: " + sy.type + "; " + n.expr.type);
          }
       }
    }
*/
    void assignStat(ASSIGN n) {
       n.left.env = n.right.env = n.env;

       expr(n.left);
       expr(n.right);

       if (n.left.isConst) {
          ERROR("Cannot assign to a constant: " + n.ident);
       };

       if (! (areAssignmentCompatible(n.left.type, n.right.type))) {
          ERROR("Wrong assignment type: " + n.left.type.ident + " = " + n.right.type.ident);
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
       else if (n instanceof INDEX)
           index((INDEX) n);
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
    /* Process an index expression:                                               */
    /*    TYPE T = ARRAY 10 OF int;                                               */
    /*    VAR A:T;                                                                */
    /*    PRINT ... A[4+x] ...;                                                   */
    /*    PRINT ... A[4+x] ...;                                                   */
    /*    A[4+x] = A[2] + A[i];                                                   */
    /* Context conditions:                                                        */
    /*    1) A should be declared                                                 */
    /*    2) A should be a variable                                               */
    /*    3) The type of A should be an array type T (i.e. class ArrayType)       */
    /*    4) The index expression should be an integer                            */
    /* Actions:                                                                   */
    /*    1) look up the identifier in the current environment                    */
    /*    2) typecheck the index expression                                       */
    /*    2) set n.type to be the element type of T                               */
    /*    3) set n.isConst to false                                               */
    /******************************************************************************/
    void index(INDEX n) {
       n.type = Standard.NoType;
       Symbol sy = n.env.lookup(n.ident);
       if (sy == null) {
          ERROR("Identifier not declared: " + n.ident);
       } else {
         if (!(sy instanceof Variable)) {
           ERROR("Variable expected: " + n.ident);
         } else {
            TypeSy arrType = sy.type;

            if (!(arrType instanceof ArrayType)) {
              ERROR("Array type expected: " + n.ident);
            } else {

               n.index.env = n.env;
               expr(n.index);

               if (n.index.type != Standard.IntType) {
                 ERROR("Array index type should be an integer: " + n.index.type.ident);
               } else {
                  TypeSy elementType = ((ArrayType) arrType).ElementType;
                  n.isConst = false;
                  n.type = elementType;
              }
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
       n.type = Standard.IntType;

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
       n.type = Standard.RealType;

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
    /*    2) Set n.ivalue/n.fvalue and n.isConst.                                 */
    /******************************************************************************/
    void binop(BINOP n) {
       n.left.env = n.right.env = n.env;

       expr(n.left);
       expr(n.right);

       if (! (isArithmeticType(n.left.type))) {
          ERROR("Arithmetic type expected: " + n.left.type);
       };

       if (! (isArithmeticType(n.right.type))) {
          ERROR("Arithmetic type expected: " + n.right.type);
       };

       if (! (areCompatible(n.OP, n.left.type, n.right.type))) {
          ERROR("Wrong binary type: " + n.left.type + " " + Token.op2string(n.OP) + " " + n.right.type);
       }
       n.type = resultType(n.OP,n.left.type,n.right.type);

       if ((n.left.isConst) && (n.right.isConst)) {
          n.isConst = true;
          if (n.left.type == Standard.IntType) {
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
          } else if (n.left.type == Standard.RealType) {
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
          } else {
            ERROR("Arithmetic type expected: " + n.left.type.toString());
          }
       } else {
          n.isConst = false;
       }
    }

    /******************************************************************************/
    /* Main entrypoint for the semantic analyzer.                                 */
    /******************************************************************************/
    public static void main (String args[]) throws IOException{
        Standard.init();

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
