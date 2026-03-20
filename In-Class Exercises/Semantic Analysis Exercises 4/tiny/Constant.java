public class Constant extends Symbol {
   public int   ivalue = -1;
   public float fvalue = -1.0f;

   public Constant (
      String ident,
      TypeSy type, 
      int value) {
      super(ident, 0, 0);
      this.ivalue = value;
      this.type = type;
      this.id = Symbol.nextID();
   }

   public Constant (
      String ident,
      TypeSy type, 
      float value) {
      super(ident, 0, 0);
      this.fvalue = value;
      this.type = type;
      this.id = Symbol.nextID();
   }

   public String toString () {
      return "CONST:" + ident + "," + type + "," + (type.equals("int")?ivalue+"":fvalue+"");
   }
}
