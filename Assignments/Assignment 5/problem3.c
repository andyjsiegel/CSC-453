struct S {
   int a;
   float b;
};
void foo(char x) {
   struct S z;
}
int main() {
   foo(99);
}
       
