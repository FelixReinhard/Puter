package org.assembler.lexer;

public class LiteralToken extends Token {
    private final Integer[] words;

    public LiteralToken(Integer[] words) {
        super(TokenType.LITERAL);
        this.words = words;
    }

    public Integer[] getWords() {
        return words;
    }
}
