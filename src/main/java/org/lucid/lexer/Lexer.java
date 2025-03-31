package org.lucid.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Lexer for the Language Lucid
 */
public class Lexer {
    private final String input;
    private int current = 0;

    private boolean hasError = false;
    private String errorMessage;

    private int currentLine = 0;
    private int currentCollumn = 0;


    public Lexer(String input) {
        this.input = input;
    }

    /**
     * Lex the input
     * @return List of tokens
     */
    public List<Token> lex() {
        // As long as there is input
        // get the next token and afterward return this list.

        List<Token> tokens = new ArrayList<>();

        while (current < input.length()) {
            var res = nextToken();
            if (res.isEmpty()) {
                break;
            }
            tokens.add(res.get());
        }

        if (hasError) {
            System.out.printf("Lexing Error: %s\n", errorMessage);
            return null;
        }

        return tokens;
    }


    private Optional<Token> nextToken() {
        var nextCharOptional = next();
        if (nextCharOptional.isEmpty()) {
            reportError("Problem getting next token");
            return Optional.empty();
        }
        // Checked optional char
        char nextChar = nextCharOptional.get();
        while (current <= input.length()) {
            switch (nextChar) {
                case ' ':

                    nextCharOptional = next();
                    if (nextCharOptional.isEmpty()) {
                        reportError("Problem getting next token");
                        return Optional.empty();
                    }
                    nextChar = nextCharOptional.get();
                    break;
                case '\n':
                case '\t':
                case '\r':
                    currentLine++;
                    return Optional.of(new Token(TokenType.NEWLINE, currentLine));
                case '{':
                    return Optional.of(new Token(TokenType.CURLY_OPEN, currentLine));
                case '%':
                    return Optional.of(new Token(TokenType.MODULO, currentLine));
                case '}':
                    return Optional.of(new Token(TokenType.CURLY_CLOSE, currentLine));
                case '(':
                    return Optional.of(new Token(TokenType.BRACKET_OPEN, currentLine));
                case ')':
                    return Optional.of(new Token(TokenType.BRACKET_CLOSE, currentLine));
                case '[':
                    return Optional.of(new Token(TokenType.BRACE_OPEN, currentLine));
                case ']':
                    return Optional.of(new Token(TokenType.BRACE_CLOSE, currentLine));
                case ':':
                    return Optional.of(new Token(TokenType.DOUBLE_POINT, currentLine));
                case '.':
                    return Optional.of(new Token(TokenType.POINT, currentLine));
                case ',':
                    return Optional.of(new Token(TokenType.COMMA, currentLine));
                case '*':
                    return doubleChar('=', TokenType.STAR, TokenType.STAR_EQUALS);
                case '=':
                    return doubleChar('=', TokenType.EQUALS, TokenType.EQ);
                case '&':
                    return doubleChar('&', TokenType.AND, TokenType.LAND);
                case '|':
                    return doubleChar('|', TokenType.OR, TokenType.LOR);
                case '+':
                    return doubleChar('=', TokenType.PLUS, TokenType.PLUS_EQUALS);
                case '-':
                    return minus();
                case '/':
                    return slash();
                case '<':
                    return doubleChar('=', TokenType.LESS, TokenType.LEQ);
                case '>':
                    return doubleChar('=', TokenType.GREATER, TokenType.GEQ);
                case '!':
                    return doubleChar('=', TokenType.NOT, TokenType.NEQ);
                case '"':
                    return string();
                default:
                    if (Character.isDigit(nextChar)) {
                        return number(nextChar);
                    } else {
                        return keywordOrWord(nextChar);
                    }
            }
        }
        return Optional.empty();
    }

    private Optional<Token> number(char first) {
        StringBuilder builder = new StringBuilder();
        builder.append(first);

        while (true) {

            var nextCharOptional = peek();
            if (nextCharOptional.isEmpty()) {
                break;
            }

            char nextChar = nextCharOptional.get();
            if (nextChar == '\n' || nextChar == ' ' || nextChar == '\t' || nextChar == '\r') {
                break;
            }
            next();
            builder.append(nextChar);
        }
        // hex
        if (builder.length() > 2 && builder.charAt(1) == 'x') {
            builder.deleteCharAt(0);
            builder.deleteCharAt(0);

            try {
                int res = Integer.parseInt(builder.toString(), 16);
                return Optional.of(new NumberToken(res, currentLine));
            } catch (Exception ignored) {
                reportError("Expected a number but had error doing so.");
                return Optional.empty();
            }
        // bin
        } else if (builder.length() > 2 && builder.charAt(1) == 'b') {
            builder.deleteCharAt(0);
            builder.deleteCharAt(0);

            try {
                int res = Integer.parseInt(builder.toString(), 2);
                return Optional.of(new NumberToken(res, currentLine));
            } catch (Exception ignored) {
                reportError("Expected a number but had error doing so.");
                return Optional.empty();
            }
        } else {
            // Normal number
            try {
                int res = Integer.parseInt(builder.toString());
                return Optional.of(new NumberToken(res, currentLine));
            } catch (Exception ignored) {
                reportError("Expected a number but had error doing so.");
                return Optional.empty();
            }

        }

    }

    private Optional<Token> string() {
        StringBuilder builder = new StringBuilder();
        while (true) {
            var nextCharOptional = peek();
            if (nextCharOptional.isEmpty()) {
                reportError("Problem ");
                return Optional.of(new Token(TokenType.SLASH, currentLine));
            }
            // Checked optional char
            char nextChar = nextCharOptional.get();

            if (nextChar == '"') {
                return Optional.of(new StringToken(builder.toString(), currentLine));
            }

            builder.append(nextChar);
        }
    }
    private Optional<Token> slash() {
        var nextCharOptional = peek();
        if (nextCharOptional.isEmpty()) {
            return Optional.of(new Token(TokenType.SLASH, currentLine));
        }
        // Checked optional char
        char nextChar = nextCharOptional.get();

        if (nextChar == '/') {
            next();
            // A comment
            while (true) {

                var nextCharOptional2 = peek();

                if (nextCharOptional2.isEmpty()) {
                    return Optional.of(new Token(TokenType.EMPTY, currentLine));
                }
                // Checked optional char
                char nextChar2 = nextCharOptional2.get();
                next();
                if (nextChar2 == '\n' || nextChar2 == '\t' || nextChar2 == '\r') {
                    return Optional.of(new Token(TokenType.EMPTY, currentLine));
                }
            }


        } else if (nextChar == '=') {
            next();
            return Optional.of(new Token(TokenType.SLASH_EQUALS, currentLine));
        }

        return Optional.of(new Token(TokenType.SLASH, currentLine));
    }

    private Optional<Token> doubleChar(char c, TokenType single, TokenType doubly) {

        var nextCharOptional = peek();
        if (nextCharOptional.isEmpty()) {
            return Optional.of(new Token(single, currentLine));
        }
        // Checked optional char
        var nextChar = nextCharOptional.get();

        if (nextChar == c) {
            next();
            return Optional.of(new Token(doubly, currentLine));
        }

        return Optional.of(new Token(single, currentLine));
    }

    private Optional<Token> minus() {

        var nextCharOptional = peek();
        if (nextCharOptional.isEmpty()) {
            return Optional.of(new Token(TokenType.MINUS, currentLine));
        }
        // Checked optional char
        var nextChar = nextCharOptional.get();

        if (nextChar == '>') {
            next();
            return Optional.of(new Token(TokenType.ARROW_RIGHT, currentLine));
        } else if (nextChar == '=') {
            next();
            return Optional.of(new Token(TokenType.MINUS_EQUALS, currentLine));
        }

        return Optional.of(new Token(TokenType.MINUS, currentLine));
    }

    /**
     * A Keyword or word else e.g. i32 hello _pen
     * @return Word or KeyWord Token
     */
    private Optional<Token> keywordOrWord(char firstChar) {
        StringBuilder word = new StringBuilder();
        word.append(firstChar);

        while (true) {
            var nextCharOptional = peek();
            if (nextCharOptional.isEmpty()) {
                break;
            }

            // Checked optional char
            char c = nextCharOptional.get();
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                break;
            }
            if (!isWordCharacter(c)) {
                break;
            }
            next();
            word.append(c);
        }

        return Optional.of(switch (word.toString()) {
            case "import", "var", "from", "pub", "class", "fn", "for", "foreach", "while", "asm", "in" -> new KeyWordToken(word.toString(), currentLine);
            case "or" -> new Token(TokenType.LOR, currentLine);
            case "and" -> new Token(TokenType.LAND, currentLine);
            default -> new WordToken(word.toString(), currentLine);
        });
    }

    private boolean isWordCharacter(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c);
    }


    private void reportError(String message) {
        hasError = true;
        errorMessage = String.format("l.%d:%d %s", currentLine, currentCollumn, message);
    }

    /**
     * Advances the input
     * @return the char
     */
    protected Optional<Character> next() {
        if (current >= input.length()) {
            return Optional.empty();
        }
        return Optional.of(input.charAt(current++));
    }

    protected Optional<Character> peek() {
        if (current >= input.length()) {
            return Optional.empty();
        }
        return Optional.of(input.charAt(current));
    }


    public boolean hasError() {
        return hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
