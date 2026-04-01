#include <stdio.h>

int foo(int start) {
   void* jumps[] = {&&lab0, &&lab1, &&lab2, &&lab3, &&lab4};
   int jTab[] = {1,4,2,2,3,0};
   int i=0;
   int result=start;
   goto* jumps[jTab[i]];
      lab1:
           result *= 2; i++; goto* jumps[jTab[i]];
      lab2:
           result *= 3; i++; goto* jumps[jTab[i]];
      lab3:
           result -= 1; i++; goto* jumps[jTab[i]];
      lab4:
           result -= 1; i++; goto* jumps[jTab[i]];
      lab0:
           return result;
}

int main() {
   printf("%d\n", foo(2));
}
