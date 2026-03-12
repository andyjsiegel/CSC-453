public class Variable extends Symbol {
   public Variable (
      String ident,
      String type) {
      this.ident = ident;
      this.type = type;
      this.id = Symbol.nextID();
   }

   public String toString () {
      return "VAR:" + ident + ":" + type;
   }
}
