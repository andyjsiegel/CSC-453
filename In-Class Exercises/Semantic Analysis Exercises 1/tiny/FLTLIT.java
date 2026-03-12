/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class FLTLIT extends EXPR {
    public float val;

    public FLTLIT(float val, int pos) {
       this.val = val;
       this.pos = pos;
    }

    public String toString() {return "(FLTLIT " + val + ")";}

    public int toGraphviz() {
       String [][] attributes = {
         {"pos", pos+""},
         {"val",val+""},
         {"isConst", isConst+""}, 
         {"type", type}, 
         {"fvalue",fvalue+""}
       };
       int n = Graphviz.addNode("FLTLIT", Graphviz.exprShape,Graphviz.exprColor,attributes);
       return n;
    }
}
