package lexer;

class LexTable {
   public static int[][] NEXTSTATE = new int[100][];
   public static boolean[] ACCEPT = new boolean[100];
   public static int[] TOKEN = new int[100];
   public static boolean[][] ADVANCE = new boolean[100][];

   static int tokenStart () {
      return position;
   }

   static String tokenValue() {
      return value.toString();
   }
}
