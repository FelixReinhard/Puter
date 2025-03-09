package org.cpu.instructions;

import org.cpu.CPU;

public class MoveInstruction implements Instruction {

    private final byte register1;
    private final byte register2;

    public MoveInstruction(byte register1, byte register2) {
        this.register1 = register1;
        this.register2 = register2;
    }

    @Override
    public void execute(CPU cpu) {
        int v2 = cpu.getRegisters().getRegister(register2);
        cpu.getRegisters().setRegister(register1, v2);
    }
}
