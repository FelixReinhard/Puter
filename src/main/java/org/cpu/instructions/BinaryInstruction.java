package org.cpu.instructions;

public abstract class BinaryInstruction implements Instruction {
    protected final byte register1;
    protected final byte register2;

    public BinaryInstruction(byte register1, byte register2) {
        this.register1 = register1;
        this.register2 = register2;
    }
}
