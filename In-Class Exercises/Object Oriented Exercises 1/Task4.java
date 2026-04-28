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
   Square q;
   q = new Square();
   q.x = 1; q.y = 3; q.side = 15;
   q.draw();
   q.move(20,30);
}
