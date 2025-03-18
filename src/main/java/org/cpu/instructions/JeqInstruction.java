package org.cpu.instructions;

import org.cpu.CPU;
import org.cpu.Registers;

public class JeqInstruction extends JmpInstruction {

    public JeqInstruction(byte register, int address) {
        super(register, address);
    }

    @Override
    public void execute(CPU cpu) {
        if (cpu.getRegisters().getRegister(Registers.CP) == 0) {
            int v = cpu.getRegisters().getRegister(register);
            cpu.getRegisters().setProgramCounter(v + offset);
        }
    }

    @Override
    public int getInstruction() {
        return 0;
    }
}
