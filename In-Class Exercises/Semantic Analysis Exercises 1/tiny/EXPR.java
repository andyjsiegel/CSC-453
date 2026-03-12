/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public abstract class EXPR extends AST {
    public boolean isConst = false;
    public int    ivalue   = -1;
    public float  fvalue   = -1.0f;
    public String type     = "";

    public String[][] attributes = {
       {"pos", pos+""},
       {"env", (env!=null)?env.toString():""},
       {"isConst", isConst+""}, 
       {"type",type},
       {"ivalue",ivalue+""}, 
       {"fvalue",fvalue+""}
     };
}
