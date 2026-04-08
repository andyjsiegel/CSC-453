class Exc {

void main (String[] args) {
   int c = args.length;
   try {
     int i = c;
     while (i <= c) {
        int x = 42/i;
        i--;
     };
   } catch (java.lang.ArithmeticException e) {
     System.out.println("DIV-BY-ZERO");
   };
}

}
