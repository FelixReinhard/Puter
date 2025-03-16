package org.assembler.lexer;

public class LabelToken extends Token {
    private final String labelName;

    public LabelToken(String labelName) {
        super(TokenType.LABEL);
        this.labelName = labelName;
    }
}
