package org.cpu.instructions;

import org.cpu.CPU;

public class DbgInstruction implements Instruction {
    private final byte register;

    public DbgInstruction(byte register) {
        this.register = register;
    }

    @Override
    public void execute(CPU cpu) {
        System.out.println(cpu.getRegisters().getRegister(register));
    }

    @Override
    public int getInstruction() {
        return (0x10 << 24) | (register << 20);
    }
}
