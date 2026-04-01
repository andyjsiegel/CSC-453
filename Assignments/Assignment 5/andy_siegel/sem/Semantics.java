package sem;
import java.lang.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
   sym.Env env = sym.Standard.env();
   sym.SyTab sytab = DECLS(E.decls, env);
   sym.Env newEnv = env.cons(sytab);
   STATS(E.stats, newEnv, false);
   EXIT(E);
}

/********************************************************/
/*                   Declaration Analysis               */
/********************************************************/

public static sym.SyTab VARDECL(ast.VARDECL E, sym.Env env) {
   ENTER(E);
   sym.SyTab result = new sym.SyTab();
   sym.Symbol typeSy = env.locateByName(E.typeName);
   if (typeSy == null) {
      aux.Error.SemId(E.position, "Identifier not declared", E.typeName);
   } else if (!(typeSy instanceof sym.TypeSy)) {
      aux.Error.SemId(E.position, "Type identifier expected", E.typeName);
   } else {
      sym.VariableSy varSy = new sym.VariableSy(E.ident, (sym.TypeSy) typeSy, E.position, 0);
      result = result.insert(varSy);
   }
   EXIT(E);
   return result;
}

public static sym.SyTab FIELDDECL(ast.FIELDDECL E, sym.Env env) {
   ENTER(E);
   sym.SyTab result = new sym.SyTab();
   sym.Symbol typeSy = env.locateByName(E.typeName);
   if (typeSy == null) {
      aux.Error.SemId(E.position, "Identifier not declared", E.typeName);
   } else if (!(typeSy instanceof sym.TypeSy)) {
      aux.Error.SemId(E.position, "Type identifier expected", E.typeName);
   } else {
      sym.FieldSy fieldSy = new sym.FieldSy(E.ident, E.position, 0);
      fieldSy.SetType((sym.TypeSy) typeSy);
      result = result.insert(fieldSy);
   }
   EXIT(E);
   return result;
}

public static sym.SyTab FORMALDECL(ast.FORMALDECL E, sym.Env env) {
   ENTER(E);
   sym.SyTab result = new sym.SyTab();
   sym.Symbol typeSy = env.locateByName(E.typeName);
   if (typeSy == null) {
      aux.Error.SemId(E.position, "Identifier not declared", E.typeName);
   } else if (!(typeSy instanceof sym.TypeSy)) {
      aux.Error.SemId(E.position, "Type identifier expected", E.typeName);
   } else {
      sym.FormalSy formalSy = new sym.FormalSy(E.ident, E.position, 0);
      formalSy.SetType((sym.TypeSy) typeSy);
      formalSy.SetFormalMode(E.mode);
      result = result.insert(formalSy);
   }
   EXIT(E);
   return result;
}

public static sym.SyTab CONSTDECL(ast.CONSTDECL E, sym.Env env) {
   ENTER(E);
   sym.SyTab result = new sym.SyTab();
   sym.Symbol typeSy = env.locateByName(E.typeName);
   if (typeSy == null) {
      aux.Error.SemId(E.position, "Identifier not declared", E.typeName);
      // Still check constant-ness below
      if (!isConstantExpr(E.expr, env)) {
         aux.Error.Sem(E.position, "Constant expression expected");
      }
      EXIT(E);
      return result;
   }
   if (!(typeSy instanceof sym.TypeSy)) {
      aux.Error.SemId(E.position, "Type identifier expected", E.typeName);
      if (!isConstantExpr(E.expr, env)) {
         aux.Error.Sem(E.position, "Constant expression expected");
      }
      EXIT(E);
      return result;
   }
   sym.TypeSy declType = (sym.TypeSy) typeSy;
   boolean isScalar = (declType == sym.Standard.IntType  || declType == sym.Standard.RealType ||
                       declType == sym.Standard.CharType || declType == sym.Standard.BoolType);
   if (!isScalar) {
      aux.Error.Sem(E.position, "Scalar type expected");
   } else {
      sym.TypeSy exprType = EXPRESSION(E.expr, env);
      if (exprType != sym.Standard.NoType && exprType != declType) {
         aux.Error.Sem(E.position, "Wrong expression type");
      }
      // Only check constant-ness when expression didn't already produce a reference error
      if (exprType != sym.Standard.NoType && !isConstantExpr(E.expr, env)) {
         aux.Error.Sem(E.position, "Constant expression expected");
      }
   }
   sym.ConstSy constSy = new sym.ConstSy(E.ident, E.position, 0);
   constSy.SetType(declType);
   result = result.insert(constSy);
   EXIT(E);
   return result;
}

public static sym.SyTab ARRAYDECL(ast.ARRAYDECL E, sym.Env env) {
   ENTER(E);
   sym.SyTab result = new sym.SyTab();
   sym.Symbol typeSy = env.locateByName(E.elementTypeName);
   boolean typeOk = true;
   if (typeSy == null) {
      aux.Error.SemId(E.position, "Identifier not declared", E.elementTypeName);
      typeOk = false;
   } else if (!(typeSy instanceof sym.TypeSy)) {
      aux.Error.SemId(E.position, "Type identifier expected", E.elementTypeName);
      typeOk = false;
   }
   sym.TypeSy countType = EXPRESSION(E.count, env);
   if (countType != sym.Standard.IntType && countType != sym.Standard.NoType) {
      aux.Error.Sem(E.position, "Integer expression expected");
   }
   if (!isConstantExpr(E.count, env)) {
      aux.Error.Sem(E.position, "Constant expression expected");
   }
   if (typeOk) {
      sym.ArrayType arrType = new sym.ArrayType(E.ident, E.position, 0);
      arrType.SetArrayElementType((sym.TypeSy) typeSy);
      result = result.insert(arrType);
   }
   EXIT(E);
   return result;
}

public static sym.SyTab RECORDDECL(ast.RECORDDECL E, sym.Env env) {
   ENTER(E);
   sym.SyTab fields = DECLS(E.fields, env);
   sym.RecordType recType = new sym.RecordType(E.ident, E.position, 0);
   recType.SetFields(fields);
   sym.SyTab result = new sym.SyTab();
   result = result.insert(recType);
   EXIT(E);
   return result;
}

public static sym.SyTab PROCDECL(ast.PROCDECL E, sym.Env env) {
   ENTER(E);
   sym.SyTab formalTab = DECLS(E.formals, env);
   // Number the formals left-to-right so parameter order is preserved
   assignFormalNumbers(E.formals, formalTab, new int[]{0});
   sym.Env innerEnv = env.cons(formalTab);
   sym.SyTab localTab = DECLS(E.decls, innerEnv);
   // Formals and locals share one scope — detect name conflicts between them
   java.util.Iterator localIt = localTab.iterator();
   while (localIt.hasNext()) {
      sym.Symbol localSy = (sym.Symbol) localIt.next();
      if (formalTab.locateByName(localSy.GetName()) != null) {
         aux.Error.SemId(localSy.GetPos(), "Multiple declaration", localSy.GetName());
      }
   }
   sym.SyTab bodyTab = formalTab.merge(localTab);
   sym.Env bodyEnv = env.cons(bodyTab);
   STATS(E.stats, bodyEnv, false);
   sym.ProcedureSy procSy = new sym.ProcedureSy(E.ident, E.position, 0);
   procSy.SetProcFormals(formalTab);
   procSy.SetProcLocals(localTab);
   sym.SyTab result = new sym.SyTab();
   result = result.insert(procSy);
   EXIT(E);
   return result;
}

public static sym.SyTab REFDECL(ast.REFDECL E, sym.Env env) {
   return new sym.SyTab();
}

// Walk DECLS left-to-right assigning sequential formal numbers
private static void assignFormalNumbers(ast.DECLS E, sym.SyTab formalTab, int[] counter) {
   if (E instanceof ast.DECLNULL) return;
   if (E.left instanceof ast.FORMALDECL) {
      sym.Symbol sy = formalTab.locateByName(E.left.ident);
      if (sy instanceof sym.FormalSy) {
         ((sym.FormalSy) sy).SetFormalNumber(counter[0]++);
      }
   }
   assignFormalNumbers(E.right, formalTab, counter);
}

public static sym.SyTab DECLS(ast.DECLS E, sym.Env env) {
   if (E instanceof ast.DECLNULL) {
      return new sym.SyTab();
   }
   // Process left first so later declarations in same scope can see earlier ones
   sym.SyTab leftTab;
   if (E.left instanceof ast.CONSTDECL) {
      leftTab = CONSTDECL((ast.CONSTDECL) E.left, env);
   } else if (E.left instanceof ast.ARRAYDECL) {
      leftTab = ARRAYDECL((ast.ARRAYDECL) E.left, env);
   } else if (E.left instanceof ast.FIELDDECL) {
      leftTab = FIELDDECL((ast.FIELDDECL) E.left, env);
   } else if (E.left instanceof ast.FORMALDECL) {
      leftTab = FORMALDECL((ast.FORMALDECL) E.left, env);
   } else if (E.left instanceof ast.PROCDECL) {
      leftTab = PROCDECL((ast.PROCDECL) E.left, env);
   } else if (E.left instanceof ast.RECORDDECL) {
      leftTab = RECORDDECL((ast.RECORDDECL) E.left, env);
   } else if (E.left instanceof ast.REFDECL) {
      leftTab = REFDECL((ast.REFDECL) E.left, env);
   } else if (E.left instanceof ast.VARDECL) {
      leftTab = VARDECL((ast.VARDECL) E.left, env);
   } else {
      leftTab = new sym.SyTab();
   }
   // Add left to env so right can see it
   sym.Env envWithLeft = env.cons(leftTab);
   sym.SyTab rightTab = DECLS(E.right, envWithLeft);
   if (rightTab.locateByName(E.left.ident) != null) {
      aux.Error.SemId(E.left.position, "Multiple declaration", E.left.ident);
      return rightTab;
   }
   return leftTab.merge(rightTab);
}

/********************************************************/
/*                   Expression Analysis                */
/********************************************************/

// Returns true if expression has no variable references (is constant-valued)
private static boolean isConstantExpr(ast.EXPRESSION E, sym.Env env) {
   if (E instanceof ast.INTLIT || E instanceof ast.REALLIT ||
       E instanceof ast.CHARLIT || E instanceof ast.STRINGLIT) return true;
   if (E instanceof ast.VARREF) {
      ast.VARREF vr = (ast.VARREF) E;
      sym.Symbol sy = env.locateByName(vr.ident);
      return (sy instanceof sym.ConstSy || sy instanceof sym.EnumSy);
   }
   if (E instanceof ast.UNARY) {
      return isConstantExpr(((ast.UNARY) E).left, env);
   }
   if (E instanceof ast.BINARY) {
      return isConstantExpr(((ast.BINARY) E).left, env) &&
             isConstantExpr(((ast.BINARY) E).right, env);
   }
   return false;
}

public static sym.TypeSy EXPRESSION(ast.EXPRESSION E, sym.Env env) {
   if      (E instanceof ast.INTLIT)   return sym.Standard.IntType; // if its a constant expr, we dont need a nested function
   else if (E instanceof ast.REALLIT)  return sym.Standard.RealType;
   else if (E instanceof ast.CHARLIT)  return sym.Standard.CharType;
   else if (E instanceof ast.STRINGLIT)return sym.Standard.StringType;
   else if (E instanceof ast.BINARY)   return BINARY((ast.BINARY) E, env);
   else if (E instanceof ast.UNARY)    return UNARY((ast.UNARY) E, env);
   else if (E instanceof ast.VARREF)   return VARREF((ast.VARREF) E, env);
   else {
      aux.Error.Internal("EXPRESSION", "Unknown node: " + E.getClass().getName());
      return sym.Standard.NoType;
   }
}

// Walk a designator chain starting from baseType
private static sym.TypeSy applyChain(ast.DESIGNATOR chain, sym.TypeSy baseType, sym.Env env) {
   if (chain instanceof ast.DESNULL) return baseType;
   if (chain instanceof ast.INDEX) {
      ast.INDEX idx = (ast.INDEX) chain;
      sym.TypeSy idxType = EXPRESSION(idx.index, env);
      if (idxType != sym.Standard.IntType && idxType != sym.Standard.NoType) {
         aux.Error.Sem(idx.position, "Integer type expected");
      }
      if (!(baseType instanceof sym.ArrayType)) {
         aux.Error.Sem(idx.position, "Array variable expected");
         return sym.Standard.NoType;
      }
      sym.TypeSy elemType = ((sym.ArrayType) baseType).GetArrayElementType();
      return applyChain(idx.next, elemType, env);
   }
   if (chain instanceof ast.FIELDREF) {
      ast.FIELDREF fr = (ast.FIELDREF) chain;
      if (!(baseType instanceof sym.RecordType)) {
         aux.Error.Sem(fr.position, "Record variable expected");
         return sym.Standard.NoType;
      }
      sym.RecordType recType = (sym.RecordType) baseType;
      sym.Symbol fieldSy = recType.GetFields().locateByName(fr.ident);
      if (fieldSy == null) {
         aux.Error.SemId(fr.position, "Field identifier not declared", fr.ident);
         return sym.Standard.NoType;
      }
      sym.TypeSy fieldType = ((sym.VariableSy) fieldSy).GetType();
      return applyChain(fr.next, fieldType, env);
   }
   return baseType;
}

public static sym.TypeSy VARREF(ast.VARREF E, sym.Env env) {
   sym.Symbol sy = env.locateByName(E.ident);
   if (sy == null) {
      aux.Error.SemId(E.position, "Identifier not declared", E.ident);
      return sym.Standard.NoType;
   }
   sym.TypeSy baseType;
   if (sy instanceof sym.VariableSy) {
      baseType = ((sym.VariableSy) sy).GetType();
   } else if (sy instanceof sym.ProcedureSy) {
      return sym.Standard.NoType;
   } else {
      aux.Error.SemId(E.position, "Variable expected", E.ident);
      return sym.Standard.NoType;
   }
   return applyChain(E.next, baseType, env);
}

public static sym.TypeSy BINARY(ast.BINARY E, sym.Env env) {
   sym.TypeSy leftType  = EXPRESSION(E.left, env);
   sym.TypeSy rightType = EXPRESSION(E.right, env);
   // Suppress cascading errors by returning no type
   if (leftType == sym.Standard.NoType || rightType == sym.Standard.NoType)
      return sym.Standard.NoType;

   switch (E.op) {
      case lexer.Token.PLUS: // all mathematical operators cases are the same, let them roll down
      case lexer.Token.MINUS:
      case lexer.Token.STAR:
      case lexer.Token.SLASH: {
         // Check both sides independently so both errors are reported
         boolean leftErr = false, rightErr = false;
         if (leftType != sym.Standard.IntType && leftType != sym.Standard.RealType) {
            aux.Error.Sem(E.position, "Numeric type expected");
            leftErr = true;
         }
         if (rightType != sym.Standard.IntType && rightType != sym.Standard.RealType) {
            aux.Error.Sem(E.position, "Numeric type expected");
            rightErr = true;
         }
         if (leftErr || rightErr) return sym.Standard.NoType;
         if (leftType != rightType) {
            aux.Error.Sem(E.position, "Type missmatch");
            return sym.Standard.NoType;
         }
         return leftType;
      }

      case lexer.Token.PERCENT: {
         boolean leftErr = false, rightErr = false;
         if (leftType != sym.Standard.IntType) {
            aux.Error.Sem(E.position, "Integer type expected");
            leftErr = true;
         }
         if (rightType != sym.Standard.IntType) {
            aux.Error.Sem(E.position, "Integer type expected");
            rightErr = true;
         }
         if (leftErr || rightErr) return sym.Standard.NoType;
         return sym.Standard.IntType;
      }

      case lexer.Token.AND: // and & or are bool ops, roll their cases together
      case lexer.Token.OR:
         if (leftType != sym.Standard.BoolType || rightType != sym.Standard.BoolType) {
            aux.Error.Sem(E.position, "Boolean type expected");
            return sym.Standard.NoType;
         }
         return sym.Standard.BoolType;

      case lexer.Token.LT: // lt, le, eq, ne, hash, ge, gt all need scalar & reference types, roll their cases together
      case lexer.Token.LE:
      case lexer.Token.EQ:
      case lexer.Token.NE:
      case lexer.Token.HASH:
      case lexer.Token.GE: 
      case lexer.Token.GT: {
         boolean leftErr = false, rightErr = false;
         if (!isScalar(leftType)) {
            aux.Error.Sem(E.position, "Scalar or reference type expected");
            leftErr = true;
         }
         if (!isScalar(rightType)) {
            aux.Error.Sem(E.position, "Scalar or reference type expected");
            rightErr = true;
         }
         if (leftErr || rightErr) return sym.Standard.NoType;
         if (leftType != rightType) {
            aux.Error.Sem(E.position, "Type missmatch");
            return sym.Standard.NoType;
         }
         return sym.Standard.BoolType;
      }

      default:
         aux.Error.Internal("BINARY", "Unknown operator");
         return sym.Standard.NoType;
   }
}

public static sym.TypeSy UNARY(ast.UNARY E, sym.Env env) {
   sym.TypeSy leftType = EXPRESSION(E.left, env);
   if (leftType == sym.Standard.NoType) return sym.Standard.NoType;

   switch (E.op) {
      case lexer.Token.NOT:
         if (leftType != sym.Standard.BoolType) {
            aux.Error.Sem(E.position, "Boolean type expected");
            return sym.Standard.NoType;
         }
         return sym.Standard.BoolType;

      case lexer.Token.MINUS:
         if (leftType != sym.Standard.IntType && leftType != sym.Standard.RealType) {
            aux.Error.Sem(E.position, "Numeric type expected");
            return sym.Standard.NoType;
         }
         return leftType;

      case lexer.Token.TRUNC:
         if (leftType != sym.Standard.RealType) {
            aux.Error.Sem(E.position, "Real type expected");
            return sym.Standard.NoType;
         }
         return sym.Standard.IntType;

      case lexer.Token.FLOAT:
         if (leftType != sym.Standard.IntType) {
            aux.Error.Sem(E.position, "Integer type expected");
            return sym.Standard.NoType;
         }
         return sym.Standard.RealType;

      default:
         aux.Error.Internal("UNARY", "Unknown operator");
         return sym.Standard.NoType;
   }
}

/********************************************************/
/*                   Statement Analysis                 */
/********************************************************/

// inLoop, is true if this function was run from LOOP(). its threaded so pass it through all of the other ast nodes
public static void STATS(ast.STATS E, sym.Env env, boolean inLoop) {
   if (E instanceof ast.STATNULL) return;
   if (E.left instanceof ast.ASSIGN) ASSIGN((ast.ASSIGN) E.left, env, inLoop);
   // format document button :)
   else if (E.left instanceof ast.PROCCALL) PROCCALL((ast.PROCCALL) E.left, env, inLoop);
   else if (E.left instanceof ast.READ)     READ((ast.READ) E.left, env, inLoop);
   else if (E.left instanceof ast.WRITE)    WRITE((ast.WRITE) E.left, env, inLoop);
   else if (E.left instanceof ast.WRITELN)  WRITELN((ast.WRITELN) E.left, env, inLoop);
   else if (E.left instanceof ast.IF1)      IF1((ast.IF1) E.left, env, inLoop);
   else if (E.left instanceof ast.IF2)      IF2((ast.IF2) E.left, env, inLoop);
   else if (E.left instanceof ast.WHILE)    WHILE((ast.WHILE) E.left, env, inLoop);
   else if (E.left instanceof ast.REPEAT)   REPEAT((ast.REPEAT) E.left, env, inLoop);
   else if (E.left instanceof ast.LOOP)     LOOP((ast.LOOP) E.left, env);
   else if (E.left instanceof ast.EXIT)     EXIT_STMT((ast.EXIT) E.left, env, inLoop);
   else if (E.left instanceof ast.BLOCK)    BLOCK((ast.BLOCK) E.left, env, inLoop);
   STATS(E.right, env, inLoop);
}

public static void ASSIGN(ast.ASSIGN E, sym.Env env, boolean inLoop) {
   if (E.left instanceof ast.VARREF) {
      ast.VARREF vr = (ast.VARREF) E.left;
      sym.Symbol sy = env.locateByName(vr.ident);
      if (sy instanceof sym.ConstSy || sy instanceof sym.EnumSy) {
         aux.Error.Sem(E.position, "Can't assign to a constant or expression.");
         return;
      }
   }
   sym.TypeSy leftType  = EXPRESSION(E.left, env);
   sym.TypeSy rightType = EXPRESSION(E.right, env);
   if (leftType == sym.Standard.NoType || rightType == sym.Standard.NoType) return;
   if (!isScalar(leftType)) {
      aux.Error.Sem(E.position, "Scalar type expected");
      return;
   }
   if (!isScalar(rightType)) {
      aux.Error.Sem(E.position, "Scalar type expected");
      return;
   }
   if (leftType != rightType) {
      aux.Error.Sem(E.position, "Type missmatch in assignment statement.");
   }
}

public static void PROCCALL(ast.PROCCALL E, sym.Env env, boolean inLoop) {
   if (!(E.des instanceof ast.VARREF)) { // if designator is not a variable reference, throw error
      aux.Error.Sem(E.position, "Identifier not declared");
      return;
   }
   ast.VARREF vr = (ast.VARREF) E.des; // cast designator to varref
   sym.Symbol sy = env.locateByName(vr.ident); // get designator ident from environment
   if (sy == null) {
      aux.Error.SemId(E.position, "Identifier not declared", vr.ident); // if its null, throw error
      return;
   }
   if (!(sy instanceof sym.ProcedureSy)) {
      aux.Error.Sem(E.position, "Procedure identifier expected"); // if its not a procedure, throw error
      return;
   }
   sym.ProcedureSy procSy = (sym.ProcedureSy) sy;
   sym.SyTab formals = procSy.GetProcFormals(); 
   int formalCount = formals.count();

   List<ast.EXPRESSION> actualsList = new ArrayList<>();
   ast.ACTUAL curr = E.actuals;
   while (!(curr instanceof ast.ACTUALNULL)) {
      actualsList.add(curr.expr);
      curr = curr.nextActual;
   }
   int actualsCount = actualsList.size();
   if (actualsCount > formalCount) {
      aux.Error.Sem(E.position, "Too many actual parameters.");
   } else if (actualsCount < formalCount) {
      aux.Error.Sem(E.position, "Too few actual parameters.");
   }
   // Check each actual against its corresponding formal by number
   for (int i = 0; i < actualsCount; i++) {
      sym.FormalSy formal = sym.ProcedureSy.GetFormalParam(formals, i);
      if (formal == null) continue;
      sym.TypeSy actualType = EXPRESSION(actualsList.get(i), env);
      if (actualType == sym.Standard.NoType) continue;
      if (actualType != formal.GetType()) {
         aux.Error.Sem(E.position, "Actual/formal parameter type missmatch.");
      }
      if (formal.GetFormalMode().equals("VAR")) {
         ast.EXPRESSION act = actualsList.get(i);
         boolean isLValue = (act instanceof ast.VARREF || act instanceof ast.INDEX ||
                             act instanceof ast.FIELDREF);
         if (!isLValue) {
            aux.Error.Sem(E.position, "VAR formal parameter requires variable actual.");
         }
      }
   }
}

public static void READ(ast.READ E, sym.Env env, boolean inLoop) {
   sym.TypeSy t = EXPRESSION(E.des, env);
   if (t == sym.Standard.NoType) return;
   if (t != sym.Standard.IntType && t != sym.Standard.RealType && t != sym.Standard.CharType) {
      aux.Error.Sem(E.position, "INTEGER, REAL, CHAR type expected");
   }
   
   if (E.des instanceof ast.VARREF) {
      ast.VARREF vr = (ast.VARREF) E.des;
      sym.Symbol sy = env.locateByName(vr.ident);
      if (sy instanceof sym.ConstSy || sy instanceof sym.EnumSy) {
         aux.Error.Sem(E.position, "Can't read to a constant.");
      }
   }
}

public static void WRITE(ast.WRITE E, sym.Env env, boolean inLoop) {
   sym.TypeSy t = EXPRESSION(E.expr, env);
   if (t == sym.Standard.NoType) return;
   if (t != sym.Standard.IntType && t != sym.Standard.RealType &&
       t != sym.Standard.CharType && t != sym.Standard.StringType) {
      aux.Error.Sem(E.position, "INTEGER, REAL, CHAR, STRING type expected");
   }
}

public static void WRITELN(ast.WRITELN E, sym.Env env, boolean inLoop) {
   // WRITELN has no args so no need to check any stuff
}

public static void IF1(ast.IF1 E, sym.Env env, boolean inLoop) {
   sym.TypeSy t = EXPRESSION(E.expr, env);
   if (t != sym.Standard.BoolType && t != sym.Standard.NoType) {
      aux.Error.Sem(E.expr.position, "Boolean type expected ");
   }
   STATS(E.then_, env, inLoop);
}

public static void IF2(ast.IF2 E, sym.Env env, boolean inLoop) {
   sym.TypeSy t = EXPRESSION(E.expr, env);
   if (t != sym.Standard.BoolType && t != sym.Standard.NoType) {
      aux.Error.Sem(E.expr.position, "Boolean type expected ");
   }
   STATS(E.then_, env, inLoop);
   STATS(E.else_, env, inLoop);
}

public static void WHILE(ast.WHILE E, sym.Env env, boolean inLoop) {
   sym.TypeSy t = EXPRESSION(E.expr, env);
   if (t != sym.Standard.BoolType && t != sym.Standard.NoType) {
      aux.Error.Sem(E.expr.position, "Boolean type expected ");
   }
   STATS(E.stats, env, inLoop); 
}

public static void REPEAT(ast.REPEAT E, sym.Env env, boolean inLoop) {
   sym.TypeSy t = EXPRESSION(E.expr, env);
   if (t != sym.Standard.BoolType && t != sym.Standard.NoType) {
      aux.Error.Sem(E.expr.position, "Boolean type expected ");
   }
   STATS(E.stats, env, inLoop);
}

public static void LOOP(ast.LOOP E, sym.Env env) {
   STATS(E.stats, env, true); // inLoop flag is true when inside of LOOP function
}

public static void EXIT_STMT(ast.EXIT E, sym.Env env, boolean inLoop) {
   if (!inLoop) {
      aux.Error.Sem(E.position, "EXIT only within LOOP ");
   }
}

public static void BLOCK(ast.BLOCK E, sym.Env env, boolean inLoop) {
   // BLOCK has no constructor so dont do anything
}

/********************************************************/
/*                        Helpers                       */
/********************************************************/

private static boolean isScalar(sym.TypeSy t) {
   return t == sym.Standard.IntType  || t == sym.Standard.RealType ||
          t == sym.Standard.CharType || t == sym.Standard.BoolType;
}

public static void main (String args[]) throws IOException {
   if (args[0] == null) {
         throw new IOException("Missing input file");
   };

   String traceFile = null;
   if (args.length >= 2) {
         traceFile = args[1];
   };

   lexer.Lex scanner = new lexer.Lex(args[0]);
   parser.Parse parser = new parser.Parse(scanner);
   ast.PROGRAM ast = parser.program();
   sem.Semantics sem = new sem.Semantics(traceFile);
   sem.SemanticAnalysis(ast);

   }

}
