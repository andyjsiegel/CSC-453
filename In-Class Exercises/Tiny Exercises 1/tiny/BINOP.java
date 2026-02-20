/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class BINOP extends EXPR {
    public int OP;
    public EXPR left;
    public EXPR right;

    public BINOP(int OP, EXPR left, EXPR right) {
       this.OP = OP; this.left = left; this.right = right;
    }

    public String toString() {
      //  String op = (OP == Token.PLUS) ? "+" : (OP == Token.TIMES) ? "+" :"";
       String op = "";
       if(OP == Token.PLUS) {
         op = "+";
       } else if(OP == Token.TIMES) {
         op = "*";
       } else {
         // keep it as empty string
       }
       return "(" + op + ", " + left.toString() + ", " + right.toString() + ")";
    }
}
