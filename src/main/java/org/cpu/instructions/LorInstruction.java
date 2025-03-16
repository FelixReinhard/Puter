package org.cpu.instructions;

import org.cpu.CPU;

public class LorInstruction extends BinaryInstruction {
    public LorInstruction(byte register1, byte register2) {
        super(register1, register2, 0x5c000000);
    }

    @Override
    public void execute(CPU cpu) {
        int v1 = cpu.getRegisters().getRegister(register1);
        int v2 = cpu.getRegisters().getRegister(register2);

        cpu.getRegisters().setRegister(register1, ((v1 > 0) || (v2 > 0)) ? 1 : 0);
    }

    @Override
    public int getInstruction() {
        return 0;
    }
}
