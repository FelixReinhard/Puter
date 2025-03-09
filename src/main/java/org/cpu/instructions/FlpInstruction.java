package org.cpu.instructions;

import org.cpu.CPU;

public class FlpInstruction implements Instruction {
    private byte register;

    public FlpInstruction(byte register) {
        this.register = register;
    }

    @Override
    public void execute(CPU cpu) {
        int v = cpu.getRegisters().getRegister(register);
        cpu.getRegisters().setRegister(register, ~v);
    }
}
