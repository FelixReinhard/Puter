package org.assembler.parser.instructions;

import org.assembler.lexer.Token;

public class SectionAssemblyInstruction extends AssemblyInstruction {

    private final String sectionName;

    public SectionAssemblyInstruction(Token tk, String name) {
        super(tk);
        this.sectionName = name;
    }

    public String getSectionName() {
        return sectionName;
    }
}
