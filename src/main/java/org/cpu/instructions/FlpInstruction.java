package org.cpu.instructions;

import org.cpu.CPU;

public class FlpInstruction implements Instruction {
    private final byte register;

    public FlpInstruction(byte register) {
        this.register = register;
    }

    @Override
    public void execute(CPU cpu) {
        int v = cpu.getRegisters().getRegister(register);
        cpu.getRegisters().setRegister(register, ~v);
    }

    @Override
    public int getInstruction() {
        return (0x60 << 24) | (register << 20);
    }
}
