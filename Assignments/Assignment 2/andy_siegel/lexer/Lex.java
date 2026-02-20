package lexer;

import java.lang.*;
import java.io.*;

public class Lex {
    public Lex(String filename) throws java.io.IOException {
        java.nio.file.Path filePath = java.nio.file.Paths.get(filename); // idk why my Lex sometimes wouldnt accept the file, but these fixed it. dunno what they do though
        LexTable.input = new String(java.nio.file.Files.readAllBytes(filePath));
        LexTable.current = -1;
        LexTable.line = 1;
    }

    public Token nextToken() {
        return LexTable.interpret();
    }

    public static void main(String[] args) throws java.io.IOException {
        if (args.length == 0) {
            System.err.println("Usage: java Lex <input>");
            return;
        }

        Lex lex = new Lex(args[0]);
        System.out.println("<block>");
        Token token;
        do {
            token = lex.nextToken();
            System.out.println("   " + token.toString());
        } while (token.type != LexTable.EOF);
        System.out.println("</block>");
    }
}
