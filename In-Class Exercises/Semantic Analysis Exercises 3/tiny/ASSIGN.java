/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class ASSIGN extends STAT {
    public String ident;
    public EXPR left;
    public EXPR right;

    public ASSIGN(EXPR left, EXPR right, int pos) {
      this.pos = pos;
      this.left = left;
      this.right = right;    }

    public String toString() {return "(ASSIGN " +  left.toString() + ", " + right.toString() + ")"; }

    public int toGraphviz() {
       String [][] attributes = {
          {"pos", pos+""},
          {"env", (env!=null)?env.toString():""},
          {"ident",""}
       };
       int n = Graphviz.addNode("ASSIGN", Graphviz.stmtShape,Graphviz.stmtColor,attributes);
       int l = left.toGraphviz();
       Graphviz.addEdge(n, l);
       int r = right.toGraphviz();
       Graphviz.addEdge(n, r);
       return n;
    }
}
