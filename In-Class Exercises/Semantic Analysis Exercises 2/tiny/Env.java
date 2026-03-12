/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

import java.util.*;


/*************************************************************************/
/* This class implements a Cons-list of SyTab objects. It's essentially  */
/* an immutable list to which you can only add elements to the front.    */
/*************************************************************************/
public class Env {
   public SyTab head;
   public Env tail;

    public Env () {
       head = null;
       tail = null;
    }

    public Env (SyTab head, Env tail) {
       this.head = head;
       this.tail = tail;
    }

   /*************************************************************************/
   /* Create a new empty environment.                                       */
   /*************************************************************************/
    public static Env create() {
       return new EnvNull();
    }

   /*************************************************************************/
   /* Return a new empty environment with sy at the front.                  */
   /*************************************************************************/
    public Env cons(SyTab sytab) {
       return new Env(sytab, this);
    }

   /*************************************************************************/
   /* Look up a symbol by name. Return null if it does not exist.           */
   /*************************************************************************/
    public Symbol lookup(String ident) {
       Symbol sy = head.lookup(ident);
       if (sy != null) {
          return sy;
       } else if (tail instanceof EnvNull) {
          return null;
       } else {
         return tail.lookup(ident);
       }
   }

   private String toStringBody() {
      String s = head.toString();
      if (tail instanceof EnvNull) {
         return s;
      } else {
        return s + "," + tail.toString();
      }
   }

   public String toString() {
      return "[" + toStringBody() + "]";
   }

}

class EnvNull extends Env {
   public EnvNull() {
      head = null;
      tail = null;
   }
   public String toString() {
      return "[]";
   }

    public Symbol lookup(String ident) {
       return null;
   }
}
