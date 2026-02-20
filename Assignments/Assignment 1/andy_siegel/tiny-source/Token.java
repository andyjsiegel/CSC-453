/* Copyright 2001, Christian Collberg, collberg@cs.arizona.edu. */

public class Token {
    public final static int ILLEGAL    = 0;
    public final static int PLUS       = 1;
    public final static int INTLIT     = 2;
    public final static int IDENT      = 3;
    public final static int SEMICOLON  = 4;
    public final static int EQUAL      = 5;
    public final static int BEGIN      = 6;
    public final static int END        = 7;
    public final static int PRINT      = 8;
    public final static int EOF        = 9;
    public final static int LABEL      = 10; //added
    public final static int IF         = 11; //added
    public final static int MINUS      = 12; //added
    public final static int LESSTHAN   = 13; //added
    public final static int GOTO       = 14; //added

 
    public int kind;
    public String ident;
    public int value;
    public int position;

    public Token(int kind, int position) {
       this.kind = kind; this.position = position;
    }

    public Token(int kind, int position, String ident) {
      this.kind = kind; this.ident = ident; this.position = position;
    }

    public Token(int kind, int position, int value) {
      this.kind = kind; this.value = value; this.position = position;
    }

    public String toString() {
       String s = "@" + position + " ";
       switch (kind) {
          case ILLEGAL   : return s + "ILLEGAL"; 
          case PRINT     : return s + "PRINT"; 
          case PLUS      : return s + "PLUS"; 
          case INTLIT    : return s + "INTLIT" + ": " + value; 
          case IDENT     : return s + "IDENT" + ": " + ident; 
          case SEMICOLON : return s + "SEMICOLON"; 
          case EQUAL     : return s + "EQUAL"; 
          case BEGIN     : return s + "BEGIN"; 
          case END       : return s + "END"; 
          case EOF       : return s + "EOF"; 
          case LABEL     : return s + "LABEL";    //added
          case IF        : return s + "IF";       //added
          case MINUS     : return s + "MINUS";    //added
          case LESSTHAN  : return s + "LESSTHAN"; //added
          case GOTO      : return s + "GOTO";     //added
          default        : return "";
       }
    }
}
