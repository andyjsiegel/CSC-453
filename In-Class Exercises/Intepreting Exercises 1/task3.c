#include <stdio.h>

int result=0;

void foo() {
   result += 2;
}

void bar() {
   result += 1;
}

void baz() {
   result *= 10;
}

void zap() {
   printf("%d\n", result);
}

int main() {
   void(*calls[])() = {foo,foo,foo,baz,foo,foo,foo,bar,zap,0x0};

   int i=0;
   while (calls[i] != 0) {
      calls[i]();
      i++;
   }
}
