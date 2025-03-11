package org.assembler.lexer;

public class RegisterToken extends Token {
    private final byte register;

    public RegisterToken(int register) {
        this.register = (byte)register;
    }

    public byte getRegister() {
        return register;
    }
}
