package org.cpu.instructions;

import org.cpu.CPU;
import org.cpu.Registers;

public class CmpInstruction extends BinaryInstruction {

    public CmpInstruction(byte register1, byte register2) {
        super(register1, register2, 0x11);
    }

    @Override
    public void execute(CPU cpu) {
        var v1 = cpu.getRegisters().getRegister(register1);
        var v2 = cpu.getRegisters().getRegister(register2);

        int value = 0;
        if (v1 == v2) {
            value = 0;
        } else if (v1 > v2) {
            value = 1;
        } else {// if (v1 < v2)
            value = 2;
        }

        cpu.getRegisters().setRegister(Registers.CP, value);
    }
}
