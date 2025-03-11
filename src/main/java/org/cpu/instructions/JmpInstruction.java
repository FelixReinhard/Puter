package org.cpu.instructions;

import org.cpu.CPU;

public class JmpInstruction implements Instruction {

    private final int address;
    private final byte register;

    public JmpInstruction(byte register, int address) {
        this.address = address;
        this.register = register;
    }

    @Override
    public void execute(CPU cpu) {
        int v = cpu.getRegisters().getRegister(register);
        cpu.getRegisters().setProgramCounter(v + address);
    }
}
