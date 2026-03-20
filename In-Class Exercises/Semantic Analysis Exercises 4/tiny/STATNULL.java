/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class STATNULL extends STATSEQ {

    public STATNULL(int pos) {}

    public String toString(String indent) {
       return indent + "STATNULL";
    }
    public int toGraphviz() {
       String [][] attributes = {};
       int n = Graphviz.addNode("NULL", Graphviz.stmtShape,Graphviz.stmtColor,attributes);
       return n;
    }
}
