#include<stdio.h>

int main(int argc, char** argv) {
   int a = argc + 2;
   int A[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
   int i = 0;
   while (i < 8) {
      int x;
      if ((i%2)==0) {
         x = A[i] * A[2*i+1];
      } else {
         x = A[2*i+1] + 4;
         x = x + (a * 9);
      };
      printf("A %i\n", x);
      i++;
   }   
}
 
