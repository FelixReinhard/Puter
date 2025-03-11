package org.assembler.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Lexer {

    private final String input;
    private final List<Token> tokens;
    private int current;

    private int currentLine = 0;
    private int currentPos = 0; // line4:10 e.g

    private boolean hasError;
    private String errorMessage;

    public Lexer(String input) {
        this.input = input;
        this.tokens = new ArrayList<>();
    }


    public List<Token> getTokens() {

        char c;

        while (!hasError) {
            Optional<Character> nc = next();
            if (nc.isEmpty()) {
                break;
            }
            c = nc.get();

            switch (c) {
                case '\n':
                case '\t':
                    currentLine++;
                    currentPos = 0;
                    break;
                case '$':
                    register();
                    break;
                case ':':
                    label();
                    break;
            }
        }

        if (hasError) {
            System.out.println(errorMessage);
        }

        return tokens;
    }



    private void register() {

        /*

         */


        if (peek().isEmpty()) {
            reportError("Register construct must be $ followed by a number.");
            return;
        }

        String c = getUntilSpace();
        int n = 0;

        try {
            n = Integer.parseInt(c);
        } catch (NumberFormatException ignored) {
            // Is a named one
            c = c.toLowerCase(Locale.ROOT);
            switch (c) {
                case "pc":
                    break;
            }
        }

        if (n < 0 || n > 16) {
            reportError("When using");
        }

        tokens.add(new RegisterToken(n));
    }
    private void label() {

    }

    /*
    Helper methods

     */

    private void reportError(String message) {
        hasError = true;
        errorMessage = String.format("Lexing Error l.%d:%d: %s", currentLine, currentPos, message);
    }

    private Optional<Character> next() {
        if (current  < input.length()) {
            currentPos++;
            return Optional.of(input.charAt(current++));
        }
        return Optional.empty();
    }

    private Optional<Character> peek() {
        if (current < input.length())
            return Optional.of(input.charAt(current));
        return Optional.empty();
    }

    private Optional<Character> peek(int offset) {
        if (current + offset < input.length())
            return Optional.of(input.charAt(current + offset));
        return Optional.empty();
    }

    /**
     * Get the next characters in a string until a space or newline is found
     * @return
     */
    private String getUntilSpace() {
        StringBuilder ret = new StringBuilder();

        while (true) {
            Optional<Character> o = peek();
            if (o.isEmpty() || o.get() == ' ') {
                return ret.toString();
            } else if (o.get() == '\n') {
                currentLine++;
                currentPos = 0;
                return ret.toString();
            }
            else {
                 ret.append(next().get());
            }
        }

    }

    /**
     * Consumes all following spaces
     */
    private void consumeSpaces() {
        while (true) {
            Optional<Character> o = peek();
            if (o.isEmpty()) {
                return;
            }
            if (o.get() == ' ') {
                next();
            } else {
                return;
            }
        }
    }
}
