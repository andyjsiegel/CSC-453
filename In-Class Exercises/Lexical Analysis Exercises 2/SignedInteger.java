/* HARD CODING */

public class SignedInteger {
    public static final int DIGIT = 0;
    public static final int SIGN = 1;
    public static final int OTHER = 2;
    public static final int END = 3;

    static boolean DEBUG = true;

    static String input;
    static int current = -1;

    static int nextChar() {
        int ch;
        current++;
        if (current >= input.length()) return END;
        char c = input.charAt(current);
        if (DEBUG) System.out.println("current=" + current + "; char=" + c);
        if (c == '+' || c == '-')
           ch = SIGN;
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
                  if(ch == SIGN)
                     state = 1;
                  else if(ch == DIGIT)
                     state = 2;
                  else
                     return false;
                  break;
               case 1  :
                  if(ch == DIGIT)
                     state = 2;
                  else
                     return false;
                  break;
                case 2:
                    if(ch == DIGIT)
                        state = 2;
                    else if(ch == END)
                        return true;
                    else
                        return false;
                    break;
           }
        }
        
    }

    public static void main (String[] args) {
       input = args[0];
       boolean result = interpret();
       System.out.println(result);
    }
}
