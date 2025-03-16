package org.assembler.lexer;

public class RegisterToken extends Token {
    private final byte register;

    public RegisterToken(int register) {
        super(TokenType.REGISTER);
        this.register = (byte)register;
    }

    public byte getRegister() {
        return register;
    }
}
