package lexer;

class LexTable {

      public final static int PLUS = 1;
      public final static int MINUS = 2;
      public final static int STAR = 3;
      public final static int SLASH = 4;
      public final static int PERCENT = 5;
      public final static int COLONEQ = 6;
      public final static int BANG = 7;
      public final static int COLON = 8;
      public final static int COMMA = 9;
      public final static int LBRACK = 10;
      public final static int RBRACK = 11;
      public final static int LPAREN = 12;
      public final static int RPAREN = 13;
      public final static int PERIOD = 14;
      public final static int SEMICOLON = 15;
      public final static int CARET = 16;
      public final static int ATCHAR = 17;
      public final static int BACKQUOTE = 18;
      public final static int EQ = 19;
      public final static int GE = 20;
      public final static int GT = 21;
      public final static int LT = 22;
      public final static int LE = 23;
      public final static int NE = 24;
      public final static int INTLIT = 25;
      public final static int REALLIT = 26;
      public final static int STRINGLIT = 27;
      public final static int IDENT = 28;
      public final static int AND = 29;
      public final static int OR = 30;
      public final static int ISA = 31;
      public final static int NARROW = 32;
      public final static int TRUNC = 33;
      public final static int FLOAT = 34;
      public final static int NOT = 35;
      public final static int PROGRAM = 36;
      public final static int PROCEDURE = 37;
      public final static int VAR = 38;
      public final static int BEGIN = 39;
      public final static int END = 40;
      public final static int FOR = 41;
      public final static int NEW = 42;
      public final static int TYPE = 43;
      public final static int WRITE = 44;
      public final static int READ = 45;
      public final static int WRITELN = 46;
      public final static int ENDFOR = 47;
      public final static int EXTENDS = 48;
      public final static int REF = 49;
      public final static int ENUM = 50;
      public final static int CONST = 51;
      public final static int ARRAY = 52;
      public final static int RECORD = 53;
      public final static int METHOD = 54;
      public final static int CLASS = 55;
      public final static int OF = 56;
      public final static int IN = 57;
      public final static int TO = 58;
      public final static int DO = 59;
      public final static int BY = 60;
      public final static int IF = 61;
      public final static int THEN = 62;
      public final static int ELSE = 63;
      public final static int ENDIF = 64;
      public final static int LOOP = 65;
      public final static int ENDLOOP = 66;
      public final static int EXIT = 67;
      public final static int WHILE = 68;
      public final static int REPEAT = 69;
      public final static int UNTIL = 70;
      public final static int ENDDO = 71;
      public final static int EOF = 72;
      public final static int CHARLIT = 73;
      public final static int ILLEGAL = 74;
      public final static int ERROR_UNTERMINATED_STRING = 75;
      public final static int ERROR_REALLIT = 76;
      public final static int ERROR_ILLEGAL_CHARACTER = 77;
      public final static int ERROR_UNTERMINATED_COMMENT = 78;
      public final static int ERROR_UNTERMINATED_CHAR = 79;
      public final static int ERROR_EMPTY_CHAR = 80;
      public final static int HASH = 81;

      public static int[][] NEXTSTATE = {
                  // STATE 0: START
                  { 0, 40, 41, 42, 43, 44, 6, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 21, 23, 45, 2, 3, 0, 31, 29, 2, 0, 0 },
                  // STATE 1: WHITESPACE
                  { 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1,
                              -1, -1, -1,
                              -1, -1 },
                  // STATE 2: IDENTIFIER
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, 2, -1,
                              -1, -1, 2,
                              -1, -1 },
                  // STATE 3: INTLIT. PERIOD(13)->4, DIGIT(23)->3, Ee(27)->34
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, 3, -1,
                              -1, -1, 34,
                              -1, -1 },
                  // STATE 4: after int+period (e.g. "34.") - accept REALLIT. DIGIT->35, Ee->34
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 35, -1,
                              -1, -1,
                              34, -1, -1 },
                  // STATE 5: SINGLE CHAR OP
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 6: COLON
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 7, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 7: COLONEQ
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 8: BANG
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 9: COMMA
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 10: LBRACK
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 11: RBRACK
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 12: LPAREN (on STAR, go to state 27 for struct comment)
                  { -1, -1, -1, 27, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 13: RPAREN
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 14: PERIOD (on DIGIT -> 35 for REALLIT like .12, on Ee -> 34 for .E5)
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 35, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 15: SEMICOLON
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 16: CARET
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 17: ATCHAR
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 18: BACKQUOTE
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 19: EQ
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 20: GE
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 21: GT
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 22: LE
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 23: LT
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 22, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 24: NE
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 25: line comment content (consume until newline goes back to state 0)
                  { 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 0,
                              25, 25,
                              25, 25, 25 },
                  // STATE 26: structured comment
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 27, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 27: inside structured comment (on STAR go to 28, consume everything else)
                  { 27, 27, 27, 28, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27,
                              27, 27,
                              27, 27, 27 },
                  // STATE 28: saw * inside struct comment. RPAREN->0 (done), STAR->28 (stay), else->27
                  { 27, 27, 27, 28, 27, 27, 27, 27, 27, 27, 27, 27, 0, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27,
                              27, 27,
                              27, 27, 27 },
                  // STATE 29: after opening '. SINGLEQUOTE->33 (empty), NEWLINE->-1 (unterminated), else->38
                  { 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, -1, 38, 33, 38, 38, 38 },
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
                  // STATE 31: inside string. DOUBLEQUOTE->32 (close). NEWLINE->-1 (unterminated). EOF handled in interpret.
                  { 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, -1,
                              32, 31,
                              31, 31, 31 },
                  // STATE 32: STRINGLIT error
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 33: empty char error
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, 29,
                              -1, -1, -1 },
                  // STATE 34: exponent start (after E). DIGIT->37, PLUS->36, DASH->36
                  { -1, 36, 36, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, -1,
                              -1, -1, -1,
                              -1, -1 },
                  // STATE 35: fractional digits (REALLIT accepting). DIGIT->37, Ee->34
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, -1,
                              -1, -1,
                              34, -1, -1 },
                  // STATE 36: REALLIT
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 37: REALLIT
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, -1,
                              -1, -1,
                              -1, -1, -1 },
                  // STATE 38: CHARLIT content (one char read). SINGLEQUOTE->39, NEWLINE->-1, else->30
                  { 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, -1, 30, 39, 30, 30, 30 },
                  // STATE 39: CHARLIT final
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1, -1, -1, -1 },
                  // STATE 40: PLUS
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1, -1, -1, -1 },
                  // STATE 41: MINUS (on second DASH, go to line comment state 25)
                  { -1, -1, 25, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1, -1, -1, -1 },
                  // STATE 42: STAR
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1, -1, -1, -1 },
                  // STATE 43: SLASH
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1, -1, -1, -1 },
                  // STATE 44: PERCENT
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1, -1, -1, -1 },
                  // STATE 45: HASH
                  { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                              -1, -1, -1, -1, -1 }
      };

      public static boolean[] ACCEPT = {
              //  0      1      2     3     4     5     6     7     8     9     10    11    12    13    14    15
                  false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
              //  16    17    18    19   20   21   22   23   24    25     26     27     28     29     30     31
                  true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false,
              //  32    33     34     35    36     37    38     39    40    41    42    43    44    45
                  true, false, false, true, false, true, false, true, true, true, true, true, true, true
      };

      public static int[] TOKEN = {
                  ILLEGAL, ILLEGAL, IDENT, INTLIT, REALLIT, ILLEGAL, COLON, COLONEQ, BANG, COMMA, LBRACK, RBRACK,
                  LPAREN,
                  RPAREN, PERIOD, SEMICOLON, CARET, ATCHAR, BACKQUOTE, EQ, GE, GT, LE, LT, NE, ILLEGAL, ILLEGAL,
                  ERROR_UNTERMINATED_COMMENT, ERROR_UNTERMINATED_COMMENT, ERROR_UNTERMINATED_CHAR, ERROR_UNTERMINATED_CHAR, ERROR_UNTERMINATED_STRING,
                  STRINGLIT,
                  ERROR_EMPTY_CHAR, ERROR_REALLIT, REALLIT, ERROR_REALLIT, REALLIT, ERROR_UNTERMINATED_CHAR, CHARLIT, PLUS, MINUS, STAR, SLASH, PERCENT, NE
      };

      public static boolean[][] ADVANCE = {
                  // STATE 0
                  { false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                              true, true,
                              true, true, true, true, true, true, true, true, true, true, true, true },
                  // STATE 1
                  { true, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, true, false, false, false,
                              false, false },
                  // STATE 2
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, true, true, false, false, false, true,
                              false, false },
                  // STATE 3: INTLIT (advance on PERIOD(13), DIGIT(23), Ee(27))
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, true,
                              false,
                              false, false, false, false, false, false, false, false, true, false, false, false, true,
                              false, false },
                  // STATE 4: after int+period (advance on DIGIT and Ee)
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, true, false, false, false, true,
                              false, false },
                  // STATE 5
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 6
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, true, false, false, false, false, false, false, false, false, false,
                              false, false },
                  // STATE 7
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 8
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, true, false, false, false, false, false, false, false, false, false,
                              false, false },
                  // STATE 9
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 10
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 11
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 12: LPAREN (advance on STAR to consume it for struct comment)
                  { false, false, false, true, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 13
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 14: PERIOD (advance on DIGIT for REALLIT)
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, true, false, false, false, false,
                              false,
                              false },
                  // STATE 15
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 16
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 17
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 18
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 19
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 20
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 21
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, true, false, false, false, false, false, false, false, false, false,
                              false, false },
                  // STATE 22
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 23
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, true, false, false, false, false, false, false, false, false, false,
                              false, false },
                  // STATE 24
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 25
                  { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                              true, true,
                              true, true, true, true, true, true, false, true, true, true, true, true },
                  // STATE 26
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, true, false, false, false, false, false, false,
                              false, false },
                  // STATE 27
                  { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                              true, true,
                              true, true, true, true, true, true, true, true, true, true, true, true },
                  // STATE 28: saw * in struct comment (advance on all chars)
                  { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                              true, true,
                              true, true, true, true, true, true, true, true, true, true, true, true },
                  // STATE 29
                  { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                              true, true,
                              true, true, true, true, true, true, true, true, true, true, true, true },
                  // STATE 30
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, true, false,
                              false, false },
                  // STATE 31
                  { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                              true, true,
                              true, true, true, true, true, true, true, true, true, true, true, true },
                  // STATE 32
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false },
                  // STATE 33
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, true, false,
                              false, false },
                  // STATE 34: exponent start (advance on PLUS, DASH, DIGIT)
                  { false, true, true, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, true, false, false, false, false,
                              false, false },
                  // STATE 35: fractional digits (advance on DIGIT and Ee)
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, true, false, false, false, true,
                              false, false },
                  // STATE 36
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, true, false, false, false, false,
                              false, false },
                  // STATE 37
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, true, false, false, false, false,
                              false, false },
                  // STATE 38: CHARLIT content
                  { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                              true, true,
                              true, true, true, true, true, true, true, true, true, true, true, true },
                  // STATE 39: CHARLIT final
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false, false },
                  // STATE 40: PLUS
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false, false },
                  // STATE 41: MINUS (advance on DASH to consume it for line comment)
                  { false, false, true, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false, false },
                  // STATE 42: STAR
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false, false },
                  // STATE 43: SLASH
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false, false },
                  // STATE 44: PERCENT
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false, false },
                  // STATE 45: HASH
                  { false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false,
                              false, false, false, false, false, false, false, false, false, false, false, false, false,
                              false, false }
      };

    static String input;
    static int current = -1;
    static int line = 1;
    static boolean DEBUG = false; // Set to true for debugging

    static int nextChar() {
        current++;
        if (current >= input.length()) {
            if (DEBUG) System.out.println("nextChar: EOF encountered.");
            return -1;
        }
        char c = input.charAt(current);
        int classified = Char.classify((int) c);
        if (DEBUG)
            System.out.println("nextChar: current=" + current + "; char='" + c + "'; charCode=" + (int) c + "; classified=" + classified);
        return classified;
    }

    public static Token interpret() {
        int state = 0;
        int start = current + 1;
        int startLine = line;

        while (true) {
            int c = nextChar();

            if (c == -1) {
                // EOF reached
                if (state == 0) {
                    return new Token(EOF, line, "EOF");
                }
                if (ACCEPT[state]) {
                    current--;
                    int tokenType = TOKEN[state];
                    String lexeme = input.substring(start, current + 1);
                    if (tokenType == IDENT) {
                        tokenType = Token.keyword(lexeme.toUpperCase());
                    }
                    return new Token(tokenType, startLine, lexeme);
                } else {
                    // Non-accepting state at EOF - produce error token
                    int tokenType = TOKEN[state];
                    if (tokenType == ILLEGAL) {
                        tokenType = EOF;
                    }
                    return new Token(tokenType, startLine, "");
                }
            }

            // Handle illegal characters directly in state 0
            if (state == 0 && c == Char.ILLEGAL) {
                return new Token(ERROR_ILLEGAL_CHARACTER, line, "");
            }

            // Track newlines for line counting
            if (c == Char.NEWLINE) {
                line++;
            }

            int nextState = NEXTSTATE[state][c];

            if (nextState == -1) {
                if (ACCEPT[state]) {
                    // Accepted token - back up if we didn't advance
                    if (!ADVANCE[state][c]) {
                        current--;
                        // Undo line increment if we backed up over a newline
                        if (c == Char.NEWLINE) {
                            line--;
                        }
                    }
                    int tokenType = TOKEN[state];
                    String lexeme = input.substring(start, current + 1);
                    if (tokenType == IDENT) {
                        tokenType = Token.keyword(lexeme.toUpperCase());
                    }
                    return new Token(tokenType, startLine, lexeme);
                } else {
                    // Error: no valid transition and not in accepting state
                    int tokenType = TOKEN[state];
                    return new Token(tokenType, startLine, "");
                }
            }

            // When transitioning to state 0 (whitespace, comment end, etc.), reset start
            if (nextState == 0) {
                start = current + 1;
                startLine = line;
            }
            // When we first leave state 0, lock in start position
            if (state == 0 && nextState != 0) {
                startLine = line;
            }

            state = nextState;
        }
    }

}