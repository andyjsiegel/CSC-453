public abstract class Symbol {
   public String ident = "";
   int id = -1;
   int pos = -1;
   int level = -1;

   public TypeSy type = Standard.NoType;

   public static int currentID = -1;

   public static int nextID() {
      currentID++;
      return currentID;
   }

   public Symbol (String Name, int Pos, int Level) {
       this.ident = Name;
       this.pos = pos;
       this.level = level;
   }

   public String toString() {
      
      String tp = "";
      if (type == null) {
         tp = "";
      } else if (type == Standard.NoType) {
         tp = ":?";
      } else {
         tp = ":" + type.ident;
      };
      return ident + tp;
   }
}
