/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class IF extends STAT {
    public int label;
    public EXPR expr;

    public IF(EXPR expr, int label, int pos) {
       this.label = label;
       this.expr = expr;
       this.pos = pos;
    }

    public String toString() {return "(IF " + label + ", " + expr.toString() + ")"; }

    public int toGraphviz() {
       String [][] attributes = {
          {"pos", pos+""},
          {"env", (env!=null)?env.toString():""},
          {"label",label+""}
       };
       int n = Graphviz.addNode("IF", Graphviz.stmtShape,Graphviz.stmtColor,attributes);
       int e = expr.toGraphviz();
       Graphviz.addEdge(n, e);
       return n;
    }
}
