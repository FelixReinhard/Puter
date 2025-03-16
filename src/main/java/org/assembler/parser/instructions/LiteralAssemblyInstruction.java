package org.assembler.parser.instructions;

import org.assembler.lexer.Token;

public class LiteralAssemblyInstruction extends AssemblyInstruction {
    private final String name;
    private final Integer[] words;

    public LiteralAssemblyInstruction(Token tk, String name, Integer[] words) {
        super(tk);
        this.name = name;
        this.words = words;
    }

    public Integer[] getWords() {
        return words;
    }

    public String getName() {
        return name;
    }
}
