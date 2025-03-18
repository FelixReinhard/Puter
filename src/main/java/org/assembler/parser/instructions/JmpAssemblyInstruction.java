package org.assembler.parser.instructions;

import org.assembler.lexer.Token;

public class JmpAssemblyInstruction extends AssemblyInstruction {
    private final String name;
    private final String labelName;
    private final int offset;

    public JmpAssemblyInstruction(Token tk, String name, String labelName, int offset) {
        super(tk);
        this.name = name;
        this.labelName = labelName;
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public String getLabelName() {
        return labelName;
    }

    public int getOffset() {
        return offset;
    }
}
