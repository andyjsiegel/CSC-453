class T {
   void P() {
      System.out.println("T");
   }
}
class S extends T {
   void P() {
      System.out.println("S");
   }
}
class U extends T {
   void P() {
      System.out.println("U");
   }
}

void main(String[] args) {
   T t;
   if (args[1].equals("S"))
      t = new S();
   else
      t = new U();
   t.P();
}
