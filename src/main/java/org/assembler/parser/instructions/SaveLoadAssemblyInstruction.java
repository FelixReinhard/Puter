package org.assembler.parser.instructions;

import org.assembler.lexer.Token;

public class SaveLoadAssemblyInstruction extends AssemblyInstruction {
    private final byte destRegister;
    private final String label;
    private final int offset;
    private final String instruction; // lw lhw lb

    public SaveLoadAssemblyInstruction(Token tk, String instruction, byte destRegister, String label, int offset) {
        super(tk);
        this.instruction = instruction;
        this.destRegister= destRegister;
        this.label = label;
        this.offset = offset;
    }

    public String getLabel() {
        return label;
    }

    public byte getDestRegister() {
        return destRegister;
    }

    public int getOffset() {
        return offset;
    }
}
