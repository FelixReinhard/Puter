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
                case ' ':
                    currentPos++;
                    break;
                case '/':
                    consume('/');
                    consumeLine();
                    break;
                case '\n':
                case '\r':
                case '\t':
                    currentLine++;
                    currentPos = 0;
                    break;
                case '$':
                    addToken(currentLine, currentPos, register());
                    //tokens.add(register());
                    break;
                case '#':
                    addToken(currentLine, currentPos, label());
                    break;
                case '[':
                    addToken(currentLine, currentPos, address());
                    break;
                case '=':
                    addToken(currentLine, currentPos, new EqualsToken());
                    break;
                case '{':
                    addToken(currentLine, currentPos, literal());
                    break;
                case '\"':
                    addToken(currentLine, currentPos, stringLiteral());
                    break;
                default:
                    addToken(currentLine, currentPos, word(c));
                    break;
            }
        }

        if (hasError) {
            System.out.println(errorMessage);
        }
        return tokens;
    }


    private Token word(char c) {
        // Also handles numbers
        if (Character.isDigit(c)) {
            current--;
            int num = parseNumber();
            return new NumberToken(num);
        } else {
            String s = c + getUntilSpace();
            return new WordToken(s);
        }
    }

    private LiteralToken stringLiteral() {
        consumeSpaces();
        Optional<Character> peeked = peek();
        if (peeked.isEmpty()) {
            reportError("Literal must be non empty.");
            return null;
        }

        // Empty literal
        if (peeked.get() == '"')
            return new LiteralToken(new Integer[]{});

        List<Integer> words = new ArrayList<>();

        while (true) {
            int nextNumber = next().get();
            if (hasError) return null;
            words.add(nextNumber);

            Optional<Character> peeked2 = peek();
            if (peeked2.isEmpty()) {
                reportError("Literal must be closed.");
                return null;
            }

            if (peeked2.get() == '"') {
                next();
                Integer[] w = new Integer[words.size()];
                words.toArray(w);
                return new LiteralToken(w);
            }
        }

    }
    private LiteralToken literal() {
        consumeSpaces();
        Optional<Character> peeked = peek();
        if (peeked.isEmpty()) {
            reportError("Literal must be non empty.");
            return null;
        }

        // Empty literal
        if (peeked.get() == '}')
            return new LiteralToken(new Integer[]{});

        List<Integer> words = new ArrayList<>();

        while (true) {
            int nextNumber = parseNumber();
            if (hasError) return null;
            words.add(nextNumber);

            consumeSpaces();

            Optional<Character> peeked2 = peek();
            if (peeked2.isEmpty()) {
                reportError("Literal must be closed.");
                return null;
            }

            if (peeked2.get() == '}') {
                next();
                Integer[] w = new Integer[words.size()];
                words.toArray(w);
                return new LiteralToken(w);
            }
            consume(',');
            consumeSpaces();
        }

    }

    private Token address() {
        /*
        - [number]
        - [$register]
        - [$register + number]
        - [.label + number]
        - [.label]
         */

        Optional<Character> peeked = peek();
        if (peeked.isEmpty()) {
            reportError("Address must be non empty.");
            return null;
        }

        byte register = 0;
        boolean hasRegister = false;

        boolean hasLabel = false;
        String label = "";

        if (peeked.get() == '$') {
            consume('$');
            register = register().getRegister();
            consumeSpaces();

            hasRegister = true;
        } else if(peeked.get() == '.') {
            consume('.');
            label = getUntilSpace();
            hasLabel = true;
        }
        consumeSpaces();

        int offset = 0;
        peeked = peek();
        if (peeked.isEmpty()) {
            reportError("Address must closed with ].");
            return null;
        }

        if (peeked.get() == '+') {
            consume('+');
            consumeSpaces();
            // We have a digit.
            offset = parseNumber();
            consumeSpaces();
        } else if (peeked.get() == '-') {
            consume('-');
            consumeSpaces();
            // We have a digit.
            offset = -parseNumber();
            consumeSpaces();
        } else if (Character.isDigit(peeked.get()) && !hasRegister && !hasLabel) {
            // Only if both the first char is a digit and there was not register so
            // Something like this [0x001]
            offset = parseNumber();
        }

        consume(']');
        if (hasLabel)
            return new LabelAddressToken(label, offset);
        else
            return new AddressToken(register, offset);


    }

    private RegisterToken register() {

        /*
            A register beginning with $ then a number or one of the fixed strings ended by space or \n
         */


        if (peek().isEmpty()) {
            reportError("Register construct must be $ followed by a number.");
            return null;
        }

        String c = getUntilSpace();
        int n = 0;

        try {
            n = Integer.parseInt(c);
        } catch (NumberFormatException ignored) {
            // Is a named one
            c = c.toLowerCase(Locale.ROOT);
            n = switch (c) {
                case "pc" -> 1;
                case "ra" -> 2;
                case "sp" -> 3;
                case "bp" -> 4;
                case "a" -> 5;
                case "b" -> 6;
                case "c" -> 7;
                case "d" -> 8;
                case "e" -> 9;
                case "f" -> 10;
                case "h" -> 11;
                case "cp" -> 13;
                case "lo" -> 14;
                case "hi" -> 15;
                default -> n;
            };
        }

        if (n < 0 || n > 16) {
            reportError("When using");
        }

        return new RegisterToken(n);
    }
    private LabelToken label() {
        return new LabelToken(getUntilSpace());
    }

    /*
    Helper methods

     */

    private void addToken(int line, int pos, Token tk) {
        tk.setPositionInformation(line, pos);
        tokens.add(tk);
    }

    private int parseNumber() {
        // Also possible to have a char like 'a'

        var optional = peek();
        if (optional.isEmpty()) {
            reportError("A number should be parsed here.");
            return 0;
        }

        if (optional.get() == '\'') {
            // If we have a char literal
            next();

            var cOptional = next();
            if (cOptional.isEmpty()) {
                reportError("A char literal should not be empty.");
                return 0;
            }

            char c = cOptional.get();
            consume('\'');
            return c;
        }


        String s = getNumbers();
        if (s.length() < 3) {
            // Must be a normal number as
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ignored) {
                reportError(String.format("Couldn't parse number %s", s));
                return 0;
            }
        } else if (s.charAt(0) == '0' && s.charAt(1) == 'x') {
            // Hexadecimal
            StringBuilder sb = new StringBuilder(s);
            sb.deleteCharAt(0);
            sb.deleteCharAt(0);

            try {
                return Integer.parseInt(sb.toString(), 16);
            } catch (NumberFormatException ignored) {
                reportError(String.format("Couldn't parse number %s", s));
                return 0;
            }
        } else if (s.charAt(0) == '0' && s.charAt(1) == 'b') {
            // Hexadecimal
            StringBuilder sb = new StringBuilder(s);
            sb.deleteCharAt(0);
            sb.deleteCharAt(0);

            try {
                return Integer.parseInt(sb.toString(), 2);
            } catch (NumberFormatException ignored) {
                reportError(String.format("Couldn't parse number %s", s));
                return 0;
            }
        } else {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ignored) {
                reportError(String.format("Couldn't parse number %s", s));
                return 0;
            }
        }
    }

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
            if (o.isEmpty() || o.get() == ' ' || o.get() == ']') {
                return ret.toString();
            } else if (o.get() == '\n' || o.get() == '\r') {
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
     * Get the next characters in a string until a space or newline is found
     * @return
     */
    private String getNumbers() {
        StringBuilder ret = new StringBuilder();

        while (true) {
            Optional<Character> o = peek();
            if (o.isEmpty() || (!Character.isDigit(o.get()) && o.get() != 'x' && o.get() != 'b')) {
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
    private void consumeLine() {
        while (true) {
            Optional<Character> o = peek();
            if (o.isEmpty()) {
                reportError(String.format("Expected something, but nothing was found"));
                return;
            }
            if (o.get() == '\r' || o.get() == '\n' || o.get() == '\t') {
                next();
                return;
            }
            next();
        }
    }
    private void consume(char c) {
        Optional<Character> o = peek();
        if (o.isEmpty()) {
            reportError(String.format("Expected %c, but nothing was found", c));
            return;
        }
        if (o.get() != c) {
            reportError(String.format("Expected %c, but got %c", c, o.get()));
            return;
        }
        next();

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

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isHasError() {
        return hasError;
    }
}
