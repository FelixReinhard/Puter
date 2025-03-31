package org.lucid.lexer;

import java.util.Objects;

public class KeyWordToken extends Token {

    public final String word;

    public KeyWordToken(String word, int line) {
        super(TokenType.KEYWORD, line);
        this.word = word;
    }


    @Override
    public boolean equals(Object obj) {
        return (obj instanceof KeyWordToken) && ((Token)obj).type == this.type && Objects.equals(((KeyWordToken) obj).word, word);
    }
}
