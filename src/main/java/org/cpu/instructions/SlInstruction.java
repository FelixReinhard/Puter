package org.cpu.instructions;

import org.cpu.CPU;

public class SlInstruction extends BinaryInstruction {
    public SlInstruction(byte register1, byte register2) {
        super(register1, register2, 0x59000000);
    }

    @Override
    public void execute(CPU cpu) {
        int v1 = cpu.getRegisters().getRegister(register1);
        int v2 = cpu.getRegisters().getRegister(register2);

        cpu.getRegisters().setRegister(register1, v1 << v2);
    }

    @Override
    public int getInstruction() {
        return 0;
    }
}
