/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class IDENT extends EXPR {
    public String ident;

    public IDENT(String ident, int pos) {
       this.pos = pos;
       this.ident = ident;
    }

    public String toString() {return "(IDENT " + ident + ")";}

    public int toGraphviz() {
       String [][] attributes = {
         {"pos", pos+""},
         {"env", (env!=null)?env.toString():""},
         {"ident",ident},
         {"type",type},
         {"isConst", isConst+""}, 
         {"ivalue",ivalue+""},
         {"fvalue",fvalue+""}
       };
       int n = Graphviz.addNode("IDENT", Graphviz.exprShape,Graphviz.exprColor,attributes);
       return n;
    }
}
