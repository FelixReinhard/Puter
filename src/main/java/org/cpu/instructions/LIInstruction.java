package org.cpu.instructions;

import org.cpu.CPU;

public class LIInstruction implements Instruction {

    private final byte register;
    private final int number;

    public LIInstruction(byte register, int number) {
        this.register = register;
        this.number = number;
    }

    @Override
    public void execute(CPU cpu) {
        cpu.getRegisters().setRegister(register, number);
    }

    @Override
    public int getInstruction() {
        return (0x01 << 24) | (register << 20) | (number & 0xfffff);
    }
}
