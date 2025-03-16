package org.cpu.instructions;

import org.cpu.CPU;
import org.cpu.Registers;

public class JgeInstruction implements Instruction {


    private final byte register;
    private final int offset;

    public JgeInstruction(byte register, int offset) {
        this.register = register;
        this.offset = offset;
    }

    @Override
    public void execute(CPU cpu) {
        if (cpu.getRegisters().getRegister(Registers.CP) == 1) {
            int v = cpu.getRegisters().getRegister(register);
            cpu.getRegisters().setProgramCounter(v + offset);
        }
    }

    @Override
    public int getInstruction() {
        return 0;
    }
}
