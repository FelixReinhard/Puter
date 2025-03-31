package org.lucid.lexer;

public class StringToken extends Token {
    private final String s;
    public StringToken(String s, int line) {
        super(TokenType.STRING_LITERAL, line);
        this.s = s;
    }

    @Override
    public TokenType getType() {
        return super.getType();
    }
}
