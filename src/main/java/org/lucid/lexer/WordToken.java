package org.lucid.lexer;

import java.util.Objects;

public class WordToken extends Token {
    public final String word;

    public WordToken(String word, int line) {
        super(TokenType.WORD, line);
        this.word = word;
    }


    @Override
    public boolean equals(Object obj) {
        return (obj instanceof WordToken) &&
                ((WordToken)obj).type == this.type &&
                Objects.equals(((WordToken) obj).word, this.word);
    }
}
