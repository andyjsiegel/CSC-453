/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class INDEX extends EXPR {
    public String ident;
    public EXPR index;

    public INDEX(String ident, EXPR index, int pos) {
       this.pos = pos;
       this.ident = ident;
       this.index = index;
    }

    public String toString() {return "(IDENT " + ident + "," + index.toString() + ")";}

    public int toGraphviz() {
       String [][] attributes = {
         {"pos", pos+""},
         {"env", (env!=null)?env.toString():""},
         {"ident",ident},
         {"type",type.ident},
         {"isConst", isConst+""}, 
         {"ivalue",ivalue+""},
         {"fvalue",fvalue+""}
       };
       int n = Graphviz.addNode("INDEX", Graphviz.exprShape,Graphviz.exprColor,attributes);
       int e = index.toGraphviz();
       Graphviz.addEdge(n, e);
       return n;
    }
}
