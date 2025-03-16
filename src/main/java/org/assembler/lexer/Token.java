package org.assembler.lexer;

public class Token {
    protected final TokenType type;
    protected int line;
    protected int pos;

    public Token(TokenType type) {
        this.type = type;
    }

    public TokenType getType() {
        return type;
    }

    public void setPositionInformation(int line, int pos) {
        this.line = line;
        this.pos = pos;
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }
}
