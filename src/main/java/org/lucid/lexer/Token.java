package org.lucid.lexer;

public class Token {

    protected final TokenType type;

    protected final int line;


    public Token(TokenType type, int line) {
        this.type = type;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Token) && ((Token)obj).type == this.type;
    }

    public int getLine() {
        return line;
    }
}
