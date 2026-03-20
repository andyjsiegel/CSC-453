public class Variable extends Symbol {

   public Variable (
      String ident,
      TypeSy type) {
      super(ident, 0, 0);
      this.type = type;
      this.id = Symbol.nextID();
   }

   public String toString () {
      return "VAR:" + ident + ":" + type.ident;
   }
}
