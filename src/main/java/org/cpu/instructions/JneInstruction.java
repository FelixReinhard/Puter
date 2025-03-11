package org.cpu.instructions;

import org.cpu.CPU;
import org.cpu.Registers;

public class JneInstruction implements Instruction {

    private final byte register;
    private final int offset;

    public JneInstruction(byte register, int offset) {
        this.register = register;
        this.offset = offset;
    }

    @Override
    public void execute(CPU cpu) {
        if (cpu.getRegisters().getRegister(Registers.CP) != 0) {
            int v = cpu.getRegisters().getRegister(register);
            cpu.getRegisters().setProgramCounter(v + offset);
        }
    }
}
