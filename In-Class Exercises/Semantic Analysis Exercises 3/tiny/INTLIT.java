/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class INTLIT extends EXPR {
    public int val;

    public INTLIT(int val, int pos) {
       super();
       this.pos = pos;
       this.val = val;
       this.type = Standard.IntType;
    }

    public String toString() {return "(INTLIT " + val + ")";}

    public int toGraphviz() {
       String [][] attributes = {
         {"pos", pos+""},
         {"env", (env!=null)?env.toString():""},
         {"val",val+""},
         {"isConst", isConst+""}, 
         {"type", type.toString()}, 
         {"ivalue",ivalue+""}
       };
       int n = Graphviz.addNode("INTLIT", Graphviz.exprShape,Graphviz.exprColor,attributes);
       return n;
    }
}
