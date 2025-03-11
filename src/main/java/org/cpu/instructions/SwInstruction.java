package org.cpu.instructions;

import org.cpu.CPU;
import org.memory.Address;

public class SwInstruction implements Instruction {

    private final byte valueRegister;
    private final byte addressRegister;
    private final short offset;

    public SwInstruction(byte valueRegister, byte addressRegister, short offset) {
        this.valueRegister = valueRegister;
        this.addressRegister = addressRegister;
        this.offset = offset;
    }

    @Override
    public void execute(CPU cpu) {
        int value = cpu.getRegisters().getRegister(valueRegister);
        cpu.getMainMemory().setAt(value, new Address(addressRegister + offset));
    }
}
