package org.cpu.instructions;

import org.cpu.CPU;
import org.cpu.Registers;

public class DivInstruction extends BinaryInstruction {

    public DivInstruction(byte register1, byte register2) {
        super(register1, register2);
    }

    @Override
    public void execute(CPU cpu) {
        int v1 = cpu.getRegisters().getRegister(register1);
        int v2 = cpu.getRegisters().getRegister(register2);

        int div = v1 / v2;
        int mod = v1 % v2;

        cpu.getRegisters().setRegister(Registers.HI, div);
        cpu.getRegisters().setRegister(Registers.LO, mod);
    }
}
