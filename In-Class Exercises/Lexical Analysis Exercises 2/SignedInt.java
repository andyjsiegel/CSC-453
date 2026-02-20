/* SOFT CODING */
public class SignedInt {
    
    public static final int SIGN = 0;
    public static final int DIGIT = 1;
    public static final int OTHER = 2;
    public static final int END = 3;

    static boolean DEBUG = true;

    static int[][] NEXTSTATE = {
    //  SIGN   DIGIT   OTHER
        { 1,     2,      -1}, // State 1
        { -1,    2,      -1}, // State 2
        { -1,    2,      -1},  // State 3
    };

    static boolean[] ACCEPT = {
      false,  // State 1
      false, // State 2 
      true, };  // State 3

    static boolean[][] ADVANCE = {
    //  SIGN    DIGIT     OTHER
        {true,       true,        true}, // State 1
        {true,       true,        true}, // State 2
        {true,       false,        true}, // State 3
    };

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
        int c = nextChar();
        while ((c != END) && (state>=0) && !ACCEPT[state]) {
            if (DEBUG) System.out.println("state=" + state + "; char=" + c);
            int newstate = NEXTSTATE[state][c];
            if (DEBUG) System.out.println("newstate=" + newstate);
            if (ADVANCE[state][c])
               c = nextChar();
            state = newstate;
        }
        return (state>=0) && ACCEPT[state];
    }

    public static void main (String[] args) {
       input = args[0];
       boolean result = interpret();
       System.out.println(result);
    }
}
