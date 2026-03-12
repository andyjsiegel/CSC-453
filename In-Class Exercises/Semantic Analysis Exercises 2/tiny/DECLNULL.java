/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class DECLNULL extends DECLSEQ {

    public DECLNULL(int pos) {
      this.pos = pos;
    }

    public String toString(String indent) {
       return indent + "DECLNULL";
    }
    public int toGraphviz() {
       String [][] attributes = {
          {"countIn", countIn+""},
          {"countOut", countOut+""},
          {"idsIn", (idsIn!=null)?idsIn.toString():"?"},
          {"idsOut", (idsOut!=null)?idsOut.toString():"?"},
       };
       int n = Graphviz.addNode("NULL", Graphviz.declShape,Graphviz.declColor,attributes);
       return n;
    }
}
