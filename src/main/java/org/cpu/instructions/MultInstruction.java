package org.cpu.instructions;

import org.cpu.CPU;
import org.cpu.Registers;

public class MultInstruction extends BinaryInstruction {

    public MultInstruction(byte register1, byte register2) {
        super(register1, register2);
    }

    @Override
    public void execute(CPU cpu) {
        int v1 = cpu.getRegisters().getRegister(register1);
        int v2 = cpu.getRegisters().getRegister(register2);
        long res = Math.multiplyFull(v1, v2);

        int high = (int) (res >>> 32);
        int low = (int) (res & Integer.MAX_VALUE);

        cpu.getRegisters().setRegister(Registers.HI, high);
        cpu.getRegisters().setRegister(Registers.LO, low);
    }
}
