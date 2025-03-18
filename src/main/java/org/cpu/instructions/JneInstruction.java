package org.cpu.instructions;

import org.cpu.CPU;
import org.cpu.Registers;

public class JneInstruction extends JmpInstruction {

    public JneInstruction(byte register, int offset) {
        super(register, offset);
    }

    @Override
    public void execute(CPU cpu) {
        if (cpu.getRegisters().getRegister(Registers.CP) != 0) {
            int v = cpu.getRegisters().getRegister(register);
            cpu.getRegisters().setProgramCounter(v + offset);
        }
    }

    @Override
    public int getInstruction() {
        return 0;
    }
}
