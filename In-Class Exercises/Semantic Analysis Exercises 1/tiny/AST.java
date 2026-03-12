/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

// The base class for all abstract syntax tree classes. 
public abstract class AST {
    public abstract int toGraphviz();

    public int pos = -1;

    /* BEGIN TASK_3 */
    public SyTab env = null;
    /* END TASK_3 */
}
