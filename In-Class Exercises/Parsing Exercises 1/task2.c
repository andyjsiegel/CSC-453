#include <stdio.h>
int main() {
   int a, x;
   x = 1 + (a = 10,20);
   printf("x=%i\n", x);
   return 0;
}
