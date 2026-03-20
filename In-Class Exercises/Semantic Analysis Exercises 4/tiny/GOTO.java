/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class GOTO extends STAT {
    public int label;

    public GOTO(int label, int pos) {
       this.pos = pos;
       this.label = label;
    }

    public String toString() {return "(GOTO " + label + ")"; }

    public int toGraphviz() {
       String [][] attributes = {
          {"pos", pos+""},
          {"label",label+""}
       };
       int n = Graphviz.addNode("GOTO", Graphviz.stmtShape,Graphviz.stmtColor,attributes);
       return n;
    }
}
