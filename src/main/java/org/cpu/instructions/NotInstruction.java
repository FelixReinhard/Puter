package org.cpu.instructions;

import org.cpu.CPU;

public class NotInstruction implements Instruction {
    private byte register;

    public NotInstruction(byte register) {
        this.register = register;
    }

    @Override
    public void execute(CPU cpu) {
        int v = cpu.getRegisters().getRegister(register);
        cpu.getRegisters().setRegister(register, (v > 0) ? 1 : 0);
    }

    @Override
    public int getInstruction() {
        return 0;
    }
}
