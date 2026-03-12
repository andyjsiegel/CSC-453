public abstract class Symbol {
   public String ident = "";
   public String type  = "";
   int id = -1;

   public static int currentID = -1;

   public static int nextID() {
      currentID++;
      return currentID;
   }
}
