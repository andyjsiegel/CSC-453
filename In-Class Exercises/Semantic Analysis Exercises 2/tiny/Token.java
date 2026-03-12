/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class Token {
    public final static int ILLEGAL    =  0;
    public final static int PLUS       =  1;
    public final static int MINUS      =  2;
    public final static int LT         =  3;
    public final static int INTLIT     =  4;
    public final static int IDENT      =  5;
    public final static int SEMICOLON  =  6;
    public final static int COLON      =  7;
    public final static int EQUAL      =  8;
    public final static int BEGIN      =  9;
    public final static int END        = 10;
    public final static int PRINT      = 11;
    public final static int IF         = 12;
    public final static int GOTO       = 13;
    public final static int VAR        = 14;
    public final static int CONST      = 15;
    public final static int MULT       = 16;
    public final static int DIV        = 17;
    public final static int FLTLIT     = 18;
    public final static int EOF        = 19;
 
    public int kind;
    public String ident;
    public int ivalue;
    public float fvalue;
    public int position;

    public Token(int kind, int position) {
       this.kind = kind; this.position = position;
    }

    public Token(int kind, int position, String ident) {
      this.kind = kind; this.ident = ident; this.position = position;
    }

    public Token(int kind, int position, int value) {
      this.kind = kind; this.ivalue = value; this.position = position;
    }

    public Token(int kind, int position, float value) {
      this.kind = kind; this.fvalue = value; this.position = position;
    }

    public String toString() {
       String s = "@" + position + " ";
       switch (kind) {
          case ILLEGAL   : return s + "ILLEGAL"; 
          case PRINT     : return s + "PRINT"; 
          case PLUS      : return s + "PLUS"; 
          case MINUS     : return s + "MINUS"; 
          case MULT      : return s + "MULT"; 
          case DIV       : return s + "DIV"; 
          case LT        : return s + "LT"; 
          case INTLIT    : return s + "INTLIT" + ": " + ivalue; 
          case FLTLIT    : return s + "FLTLIT" + ": " + fvalue; 
          case IDENT     : return s + "IDENT" + ": " + ident; 
          case SEMICOLON : return s + "SEMICOLON"; 
          case COLON     : return s + "COLON"; 
          case EQUAL     : return s + "EQUAL"; 
          case BEGIN     : return s + "BEGIN"; 
          case END       : return s + "END"; 
          case IF        : return s + "IF"; 
          case GOTO      : return s + "GOTO"; 
          case VAR       : return s + "VAR"; 
          case CONST     : return s + "CONST"; 
          case EOF       : return s + "EOF"; 
          default        : return "";
       }
    }

    public static String op2string (int op) {
       switch (op) {
          case PLUS      : return "PLUS"; 
          case MINUS     : return "MINUS"; 
          case MULT      : return "MULT"; 
          case DIV       : return "DIV"; 
          case LT        : return "LT"; 
          default        : return "";
       }
    }

}
