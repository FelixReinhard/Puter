package org.cpu.instructions;

import org.cpu.CPU;

public class LandInstruction extends BinaryInstruction {
    public LandInstruction(byte register1, byte register2) {
        super(register1, register2);
    }

    @Override
    public void execute(CPU cpu) {
        int v1 = cpu.getRegisters().getRegister(register1);
        int v2 = cpu.getRegisters().getRegister(register2);

        cpu.getRegisters().setRegister(register1, ((v1 > 0) && (v2 > 0)) ? 1 : 0);
    }
}
