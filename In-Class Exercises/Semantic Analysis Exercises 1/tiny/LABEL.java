/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class LABEL extends STAT {
    public int label;

    public LABEL(int label, int pos) {
       this.pos = pos;
       this.label = label;
    }

    public String toString() {return "(LABEL " + label + ")"; }

    public int toGraphviz() {
       String [][] attributes = {
         {"pos", pos+""},
         {"label",label+""}
       };
       int n = Graphviz.addNode("LABEL", Graphviz.stmtShape,Graphviz.stmtColor,attributes);
       return n;
    }
}
