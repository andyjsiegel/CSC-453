/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

import java.util.*;

public class SyTab {
    Hashtable<String, Integer> sytab = new Hashtable<>();
    int currentID = 0;

    // Insert ident into the symbol table, unless it's
    // already there. Assign a new number to the identifier.
    public void insert(String ident) {
        if (!sytab.containsKey(ident))
            sytab.put(ident, Integer.valueOf(currentID++));
    }

    // Return the number of 'ident'. If 'ident' is not in the
    // symbol table, return -1.
    public int lookup(String ident) {
        if (sytab.containsKey(ident))
            return sytab.get(ident);  // Auto-unboxing
        else
            return -1;
    }

    // Return the number of identifiers in the table.
    public int size() {
        return sytab.size();
    }
}
