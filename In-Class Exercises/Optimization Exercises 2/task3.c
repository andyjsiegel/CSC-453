#include<stdio.h>

int main() {
   int a = 2;
   int b = 3;
   int i = 0;
   while (i < (a+b)) {
      if ((i%2)==0) {
         printf("A %i\n", i);
      };
      i++;
      if ((i%2)==0) {
         printf("B %i\n", i);
      };
      int x = (i < (a+b)) + 1;
      for(int j=0; j<x; j++){
         printf("C %i\n", i%2);
      }
   }   
}
 
