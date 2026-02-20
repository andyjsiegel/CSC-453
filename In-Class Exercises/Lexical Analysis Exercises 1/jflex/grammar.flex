%%
%public
%class Scanner
%standalone
%unicode
%%

<YYINITIAL> {
  "BEGIN" {System.out.println("BEGIN"); }
  "END"   {System.out.println("END"); }
  ","     {System.out.println("COMMA"); }
  ":"     {System.out.println("COLON"); }

  /* Ignore whitespace characters. */
  [\ \t\b\012]+ { }

  /* Identifiers. */
  [A-Za-z] ([A-Za-z] | [0-9])* {
      System.out.println("IDENT <" + yytext() + ">");
  }

  /* Integer literals. */
  [0-9][0-9]* {
      System.out.println("INTLIT <" + yytext() + ">");
  }
}

/* Ignore newlines. */
   \r|\n|\r\n { }

/* Handle all other characters. */
. {
  System.out.println("Illegal character: <" + yytext() + ">");
}
