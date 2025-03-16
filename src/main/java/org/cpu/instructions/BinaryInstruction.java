package org.cpu.instructions;

public abstract class BinaryInstruction implements Instruction {
    protected final byte register1;
    protected final byte register2;
    private final int optcode;

    public BinaryInstruction(byte register1, byte register2, int optcode) {
        this.register1 = register1;
        this.register2 = register2;
        this.optcode = optcode;
    }

    @Override
    public int getInstruction() {
        return optcode | (register1 << 20) | (register2 << 16);
    }

    public byte getRegister1() {
        return register1;
    }

    public byte getRegister2() {
        return register2;
    }
}
