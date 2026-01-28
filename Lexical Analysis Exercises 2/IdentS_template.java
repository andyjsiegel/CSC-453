/*
   javac IdentS.java
   java IdentS 'a'
   java IdentS 'abc'
   java IdentS 'abc42'
   java IdentS '4a'
   
   The program prints 'true' if the string is a legal C
   identifer, 'false' otherwise.
*/
class IdentS {

    public static final int LETTER = 0;
    public static final int DIGIT  = 1;
    public static final int OTHER  = 2;
    public static final int END    = 3;

    static boolean DEBUG = false;

    static String input;
    static int current = -1;

    static int nextChar() {
        int ch;
        current++;
        if (current >= input.length()) return END;
        char c = input.charAt(current);
        if (DEBUG) System.out.println("current=" + current + "; char=" + c);
        if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')))
           ch = LETTER;
        else if ((c >= '0') && (c <= '9'))
           ch = DIGIT;
        else
           ch = OTHER;
        if (DEBUG) System.out.println("nextChar=" + ch);
        return ch;
    }

    public static boolean interpret () {
        int state = 0;
        while(true) {
           int ch = nextChar();
           if (DEBUG) System.out.println("state=" + state + "; char=" + ch);
           switch (state) {
               case -1 : 
                   return false;
               case 0  : 
                  if(ch == LETTER)
                     state = 1;
                  else
                     return false;
                  break;
               case 1  :
                  if(ch == OTHER)
                     return false;
                  else
                     state = 1;
                  break;
           }
           return true;
        }
        
    }

    public static void main (String[] args) {
       input = args[0];
       boolean result = interpret();
       System.out.println(result);
    }
}
