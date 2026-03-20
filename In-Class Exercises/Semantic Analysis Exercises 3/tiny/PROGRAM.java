/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class PROGRAM extends AST {
    public DECLSEQ decls;
    public STATSEQ stats;

    public PROGRAM (DECLSEQ decls, STATSEQ stats, int pos) {
       this.pos = pos;
       this.decls = decls; 
       this.stats = stats;
    }

    public String toString() {
       return "(PROGRAM\n" + 
                   "   " + decls.toString("   ") + "\n" +
                   "   " + stats.toString("   ") + " " +
              "\n)";
    }

    public int toGraphviz() {
       String [][] attributes = {
          {"pos", pos+""},
          {"env", (env!=null)?env.toString():""},
       };
       int n = Graphviz.addNode("PROGRAM", Graphviz.declShape,Graphviz.declColor,attributes);
       int d = decls.toGraphviz();
       int s = stats.toGraphviz();
       Graphviz.addEdge(n, d);
       Graphviz.addEdge(n, s);
       return n;
    }
}
