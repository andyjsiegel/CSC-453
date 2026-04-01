#include <stdio.h>

int foo(int start) {
   void* jumps[] = {&&lab1, &&lab1, &&lab3, &&lab2, &&lab1, 0x0};

   int i=0;
   int result=start;
   while (jumps[i] != 0) {
      goto* jumps[i];
      lab1:
           result *= 2; goto end;
      lab2:
           result *= 3; goto end;
      lab3:
           result -= 1; goto end;
      end:
         i++;
   }
   return result;
}

int main() {
   printf("%d\n", foo(2));
}
