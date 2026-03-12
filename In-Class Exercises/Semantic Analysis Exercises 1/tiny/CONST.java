/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class CONST extends DECL {
    public String ident;
    public String type = "";
    public EXPR expr;

    public CONST(String ident, String type, EXPR expr, int pos) {
      this.pos = pos;
      this.type = type; 
      this.ident = ident; 
      this.expr = expr;
    }

    public String toString() {return "(CONST " + ident + ", " + expr.toString() + ")"; }

    public int toGraphviz() {
       String [][] attributes = {
          {"pos", pos+""},
          {"env", (env!=null)?env.toString():""},
          {"ident",ident},
          {"type",type}
       };
       int n = Graphviz.addNode("CONST", Graphviz.declShape,Graphviz.declColor,attributes);
       int e = expr.toGraphviz();
       Graphviz.addEdge(n, e);
       return n;
    }
}
