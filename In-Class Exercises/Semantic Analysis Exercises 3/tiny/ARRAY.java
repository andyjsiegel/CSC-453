/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class ARRAY extends DECL {

    EXPR expr;

    public ARRAY(String ident, String type, EXPR e, int pos) {
      this.ident = ident; 
      this.type = type; 
      this.expr = e; 
      this.pos = pos;
    }

    public String toString() {return "(ARRAY " + ident + ", " + type + "," + expr.toString() + ")"; }

    public int toGraphviz() {
       String [][] attributes = {
           {"pos", pos+""},
           {"env", (env!=null)?env.toString():""},
           {"ident",ident}, 
           {"type",type}
       };
       int n = Graphviz.addNode("ARRAY", Graphviz.declShape,Graphviz.declColor,attributes);
       int e = expr.toGraphviz();
       Graphviz.addEdge(n, e);
       return n;
    }
}
