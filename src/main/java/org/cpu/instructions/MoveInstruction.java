package org.cpu.instructions;

import org.cpu.CPU;

public class MoveInstruction extends BinaryInstruction {

    public MoveInstruction(byte register1, byte register2) {
        super(register1, register2);
    }

    @Override
    public void execute(CPU cpu) {
        int v2 = cpu.getRegisters().getRegister(register2);
        cpu.getRegisters().setRegister(register1, v2);
    }
}
