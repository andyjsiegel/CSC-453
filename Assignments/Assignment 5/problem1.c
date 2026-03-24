void foo(char x) {
   float y;
   {
    int y;
   };
}
int main() {
   foo(99);
}
       
