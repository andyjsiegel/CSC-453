/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

import java.util.*;


/*************************************************************************/
/* This class implements a Cons-list of Symbol objects. It's essentially */
/* an immutable list to which you can only add elements to the front.    */
/*************************************************************************/
public class SyTab {
   public Symbol head;
   public SyTab tail;

    public SyTab () {
       head = null;
       tail = null;
    }

    public SyTab (Symbol head, SyTab tail) {
       this.head = head;
       this.tail = tail;
    }

   /*************************************************************************/
   /* Create a new empty symbol table.                                      */
   /*************************************************************************/
    public static SyTab create() {
       return new SyTabNull();
    }

   /*************************************************************************/
   /* Return a new empty symbol table with sy at the front.                 */
   /*************************************************************************/
    public SyTab add(Symbol sy) {
       return new SyTab(sy, this);
    }

   /*************************************************************************/
   /* Look up a symbol by name. Return null if it does not exist.           */
   /*************************************************************************/
    public Symbol lookup(String ident) {
       // System.out.println(ident + ", " + head.ident);
       if (head.ident.equals(ident)) {
          return head;
       } else if (tail instanceof  SyTabNull) {
          return null;
       } else {
         return tail.lookup(ident);
       }
   }

   private String toStringBody() {
      if (this instanceof SyTabNull) {
         return "";
      } else {
        return head.toString() + "," + tail.toString();
      }
   }

   public String toString() {
      return "{" + toStringBody() + "}";
   }

}

class SyTabNull extends SyTab {
   public SyTabNull() {
      head = null;
      tail = null;
   }
   public String toString() {
      return "{}";
   }

    public Symbol lookup(String ident) {
       return null;
   }
}
