package org.assembler.lexer;

public class WordToken extends Token {

    private final String word;

    public WordToken(String word) {
        super(TokenType.WORD);
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
