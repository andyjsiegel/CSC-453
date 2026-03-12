/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class DECLSEQ extends AST {
    public DECL decl;
    public DECLSEQ next;

    public int countIn;
    public int countOut;

    public SyTab idsIn;
    public SyTab idsOut;

    public DECLSEQ() {}

    public DECLSEQ(DECL decl, DECLSEQ next, int pos) {
      this.pos = pos;
      this.decl = decl;
      this.next = next;
    }

    public String toString(String indent) {
       return indent + 
               "(DECLSEQ\n" + 
                indent + "   " + decl.toString() + "\n" +
                next.toString(indent + "   ") + 
                "\n" + indent + ")";
    }

    public int toGraphviz() {
       String [][] attributes = {
          {"pos", pos+""},
          {"env", (env!=null)?env.toString():""},
          {"countIn", countIn+""},
          {"countOut", countOut+""},
          {"idsIn", (idsIn!=null)?idsIn.toString():"?"},
          {"idsOut", (idsOut!=null)?idsOut.toString():"?"},
       };
       int n = Graphviz.addNode("DECLSEQ", Graphviz.declShape,Graphviz.declColor,attributes);
       int d = decl.toGraphviz();
       int x = next.toGraphviz();
       Graphviz.addEdge(n, d);
       Graphviz.addEdge(n, x);
       return n;
    }
}
