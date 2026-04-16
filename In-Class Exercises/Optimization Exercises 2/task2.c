#include<stdio.h>

void P (int x, int* y) {
   x = *y + 2;
   *y = *y - 1;
   int i = 0;
   while (i < x) {
      printf("Hi!\n");
      i++;
   };
}

int main() {
   int a = 3;
   int b = 5;
   P(a, & b);
   int i = 0;
   while (i < (a+b)) {
      printf("By!\n");
      i++;
   }   
}
 
