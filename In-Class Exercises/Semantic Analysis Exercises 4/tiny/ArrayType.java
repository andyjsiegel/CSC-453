public class ArrayType extends TypeSy {

   public int Count = 0;
   public TypeSy ElementType = null;

   public ArrayType (String Name, TypeSy ElementType, int Count) {
        super(Name, 0, 0);
        this.ElementType = ElementType;
        this.Count = Count;
   }

   public String toString() {
       return "TYPE " + ident + " = ARRAY [" + Count + "] OF " + ElementType.ident;
   }
}
