package org.cpu.instructions;

import org.cpu.CPU;
import org.cpu.Registers;

public class SyscallInstruction implements Instruction {
    @Override
    public void execute(CPU cpu) {
        cpu.getRegisters().setProgramCounter(Registers.SYS_ADDRESS);
    }

    @Override
    public int getInstruction() {
        return 0;
    }
}
