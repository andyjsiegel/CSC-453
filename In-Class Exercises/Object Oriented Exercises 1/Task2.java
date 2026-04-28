class T {
   int v;
   char c;
   void P(int x) {}
   void Q(char x) {}
}

class U extends T {
   float x;
   int k;
   void R(int x) {}
   void Q(float x) {}
}

void main(String args[]) {
   T t;
   U u;
   t = new T();
   u = new U();
}
