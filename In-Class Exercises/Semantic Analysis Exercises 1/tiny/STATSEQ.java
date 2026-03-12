/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class STATSEQ extends AST {
    public STAT stat;
    public STATSEQ next;

    public STATSEQ() {}

    public STATSEQ(STAT stat, STATSEQ next, int pos) {
      this.stat = stat;
      this.next = next;
      this.pos = pos;
    }

    public String toString(String indent) {
       return indent + 
               "(STATSEQ\n" + 
                indent + "   " + stat.toString() + "\n" +
                next.toString(indent + "   ") + 
                "\n" + indent + ")";
    }

    public int toGraphviz() {
       String [][] attributes = {
          {"pos", pos+""},
          {"env", (env!=null)?env.toString():""},
       };
       int n = Graphviz.addNode("STMTSEQ", Graphviz.stmtShape,Graphviz.stmtColor,attributes);
       int s = stat.toGraphviz();
       int x = next.toGraphviz();
       Graphviz.addEdge(n, s);
       Graphviz.addEdge(n, x);
       return n;
    }
}
