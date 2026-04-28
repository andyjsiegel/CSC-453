
class T {}
class S extends T {}
class A extends S {}
class B extends S {}
class C extends B {}
class U extends T {}
class V extends U {}
class Y extends U {}
class Z extends Y {}

void main(String[] args) {
   C c = new C();
   T s = new S();
   if (c instanceof T) System.out.println("C");
   if (c instanceof S) System.out.println("S");
   if (s instanceof U) System.out.println("U");
}

