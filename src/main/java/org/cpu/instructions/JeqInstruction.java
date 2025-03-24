package org.cpu.instructions;

import org.cpu.CPU;
import org.cpu.Registers;

public class JeqInstruction extends JmpInstruction {

    public JeqInstruction(byte register, int offset) {
        super(register, offset);
        setOptcode(0x72);
    }

    @Override
    public void execute(CPU cpu) {
        if (cpu.getRegisters().getRegister(Registers.CP) == 0) {
            int v = cpu.getRegisters().getRegister(register);
            cpu.getRegisters().setProgramCounter(v + offset);
        }
    }
}
