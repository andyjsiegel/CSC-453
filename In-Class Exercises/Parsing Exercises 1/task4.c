#include <stdio.h>

int x = 10;

int f1() {
   x++;
   return x;
}

int f2() {
   return x;
}

int main() {
   int a = f1() + f2();
   printf("a=%i\n", a);
   return 0;
}
