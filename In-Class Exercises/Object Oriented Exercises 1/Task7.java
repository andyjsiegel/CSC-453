
class T {
   int x;
   void P(int x) {
      this.x = x;
   }
}

class U extends T {
   double x;
   void P(int x) {
      super.x = x;
   }
   void Q(double x) {
      this.x = x;
   }
}

void main(String[] args) {
   U u = new U();
   u.P(10);
   u.Q(10.0);
}

