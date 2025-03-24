package org.cpu.instructions;

import org.cpu.CPU;

public class JmpInstruction implements Instruction {

    protected final int offset;
    protected final byte register;
    private int optcode = 0x71;

    public JmpInstruction(byte register, int offset) {
        this.offset = offset;
        this.register = register;
    }

    @Override
    public void execute(CPU cpu) {
        int v = cpu.getRegisters().getRegister(register);
        cpu.getRegisters().setProgramCounter(v + offset);
    }

    @Override
    public int getInstruction() {
        return (optcode << 24) | (register << 20) | (offset & 0xfffff);
    }

    protected void setOptcode(int optcode) {
        this.optcode = optcode;
    }
}
