class Shape {
   int x, y;
   void draw() { }
   void move(int X, int Y) { }
}
class Square extends Shape {
   int side;
   void draw() { }
}
class Circle extends Shape {
   int radius;
   void draw() { }
   int area() { return 1; }
}

void main (String args[]) {
   Shape  s = new Square();
   Shape  t = new Circle();
   Square q = new Square();
   Circle c = new Circle();
   s = c;
   s = q;
   c = q;
   c = (Circle)s;
   q = c;
   q = (Square)s;
   q = (Square)t;
}
