public class Constant extends Symbol {
   public int   ivalue = -1;
   public float fvalue = -1.0f;

   public Constant (
      String ident,
      String type, 
      int value) {
      this.ident = ident;
      this.ivalue = value;
      this.type = type;
      this.id = Symbol.nextID();
   }

   public Constant (
      String ident,
      String type, 
      float value) {
      this.ident = ident;
      this.fvalue = value;
      this.type = type;
      this.id = Symbol.nextID();
   }

   public String toString () {
      return "CONST:" + ident + "," + type + "," + (type.equals("int")?ivalue+"":fvalue+"");
   }
}
