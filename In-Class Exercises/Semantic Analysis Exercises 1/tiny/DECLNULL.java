/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class DECLNULL extends DECLSEQ {

    public DECLNULL(int pos) {
      this.pos = pos;
    }

    public String toString(String indent) {
       return indent + "DECLNULL";
    }
    public int toGraphviz() {
       String [][] attributes = {};
       int n = Graphviz.addNode("NULL", Graphviz.declShape,Graphviz.declColor,attributes);
       return n;
    }
}
