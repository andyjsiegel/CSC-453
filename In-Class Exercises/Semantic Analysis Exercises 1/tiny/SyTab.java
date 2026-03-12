/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

import java.util.*;

public class SyTab {
    Hashtable sytab = new Hashtable();
    int currentID = 0;

    // Insert ident into the symbol table, unless it's
    // already there. Assign a new number to the identifier.
    public void insert(Symbol sy) {
       if (!sytab.containsKey(sy.ident))
          sytab.put(sy.ident, sy);
    }

    // Return the number of 'ident'. If 'ident' is not in the
    // symbol table, return null.
    public Symbol lookup(String ident) {
      if (sytab.containsKey(ident))
         return ((Symbol)sytab.get(ident));
      else
         return null;
    }

    // Return the number of identifiers in the table.
    public int size() {
       return sytab.size(); 
    }

   public String toString() {
      String s = "[";
      Collection<Symbol> values = sytab.values();
      for (Symbol value : values) {
         String v = value.toString();
         if (s.equals("[")) {
            s = s + v;
         } else {
            s = s + "\n" + v;
         }
      }
      s = s + "]";
      return s;
   }

}
