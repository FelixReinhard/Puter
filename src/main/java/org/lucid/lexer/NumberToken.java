package org.lucid.lexer;

import java.util.Objects;

public class NumberToken extends Token {
    public final int number;
    public NumberToken(int number, int line) {
        super(TokenType.NUMBER, line);
        this.number = number;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof NumberToken) && ((NumberToken)obj).type == this.type && ((NumberToken)obj).number == this.number;
    }
}
