/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class ASSIGN extends STAT {
    public String ident;
    public EXPR expr;

    public ASSIGN(String ident, EXPR expr, int pos) {
      this.pos = pos;
      this.ident = ident;
      this.expr = expr;
    }

    public String toString() {return "(ASSIGN " + ident + ", " + expr.toString() + ")"; }

    public int toGraphviz() {
       String [][] attributes = {
          {"pos", pos+""},
          {"env", (env!=null)?env.toString():""},
          {"ident",ident}
       };
       int n = Graphviz.addNode("ASSIGN", Graphviz.stmtShape,Graphviz.stmtColor,attributes);
       int e = expr.toGraphviz();
       Graphviz.addEdge(n, e);
       return n;
    }
}
