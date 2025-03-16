package org.assembler.lexer;

public class LabelAddressToken extends Token {
    private final String label;
    private final int offset;
    public LabelAddressToken(String label, int offset) {
        super(TokenType.ADDRESS_LABEL);
        this.label = label;
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public String getLabel() {
        return label;
    }
}
