package org.assembler.lexer;

public class AddressToken extends Token {
    private final byte register;
    private final int offset;

    public AddressToken(byte register, int offset) {
        super(TokenType.ADDRESS);
        this.register = register;
        this.offset = offset;
    }

    public byte getRegister() {
        return register;
    }

    public int getOffset() {
        return offset;
    }
}
