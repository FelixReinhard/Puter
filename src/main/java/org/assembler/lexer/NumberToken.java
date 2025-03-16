package org.assembler.lexer;

public class NumberToken extends Token {
    private final int number;
    public NumberToken(int number) {
        super(TokenType.NUMBER);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
