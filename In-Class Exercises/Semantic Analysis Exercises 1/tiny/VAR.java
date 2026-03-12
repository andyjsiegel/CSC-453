/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class VAR extends DECL {
    public String ident;
    public String type;

    public VAR(String ident, String type, int pos) {
      this.ident = ident; 
      this.type = type; 
      this.pos = pos;
    }

    public String toString() {return "(VAR " + ident + ", " + type + ")"; }

    public int toGraphviz() {
       String [][] attributes = {
           {"pos", pos+""},
           {"env", (env!=null)?env.toString():""},
           {"ident",ident}, 
           {"type",type}
       };
       int n = Graphviz.addNode("VAR", Graphviz.declShape,Graphviz.declColor,attributes);
       return n;
    }
}
