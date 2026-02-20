/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class BINOP extends EXPR {
    public int OP;
    public EXPR left;
    public EXPR right;

    public BINOP(int OP, EXPR left, EXPR right) {
       this.OP = OP; this.left = left; this.right = right;
    }

    public String toString() {
       String op = (OP == Token.PLUS)?"+":"";
       return "(" + op + ", " + left.toString() + ", " + right.toString() + ")";
    }
}
