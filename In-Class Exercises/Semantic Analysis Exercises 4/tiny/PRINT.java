/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class PRINT extends STAT {
    public EXPR expr;

    public PRINT(EXPR expr, int pos) {
      this.expr = expr;
      this.pos = pos;
    }

    public String toString() {return "(PRINT " + expr.toString() + ")";}

    public int toGraphviz() {
       String [][] attributes = {
          {"pos", pos+""},
          {"env", (env!=null)?env.toString():""},
       };
       int n = Graphviz.addNode("PRINT", Graphviz.stmtShape,Graphviz.stmtColor,attributes);
       int e = expr.toGraphviz();
       Graphviz.addEdge(n, e);
       return n;
    }
}
