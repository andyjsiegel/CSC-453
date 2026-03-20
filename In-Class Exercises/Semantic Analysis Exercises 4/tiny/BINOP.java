/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class BINOP extends EXPR {
    public int OP;
    public EXPR left;
    public EXPR right;

    public BINOP(int OP, EXPR left, EXPR right, int pos) {
       this.pos = pos;
       this.OP = OP; 
       this.left = left; 
       this.right = right;
    }

    public String toString() {
       String op = Token.op2string(OP);
       return "(" + op + ", " + left.toString() + ", " + right.toString() + ")";
    }

    public int toGraphviz() {
       String op = Token.op2string(OP);
       String [][] attributes = {
          {"pos", pos+""},
          {"env", (env!=null)?env.toString():""},
          {"OP",op},
          {"type", type.toString()},
          {"isConst",isConst+""},
          {"ivalue",ivalue+""},
          {"fvalue",fvalue+""}
       };
       int n = Graphviz.addNode("BINOP", Graphviz.exprShape,Graphviz.exprColor,attributes);
       int l = left.toGraphviz();
       int r = right.toGraphviz();
       Graphviz.addEdge(n, l);
       Graphviz.addEdge(n, r);
       return n;
    }
}
