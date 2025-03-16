package org.cpu.instructions;

import org.cpu.CPU;
import org.memory.Address;

public class ShwInstruction implements Instruction {

    private final byte valueRegister;
    private final byte addressRegister;
    private final short offset;

    public ShwInstruction(byte valueRegister, byte addressRegister, short offset) {
        this.valueRegister = valueRegister;
        this.addressRegister = addressRegister;
        this.offset = offset;
    }

    @Override
    public void execute(CPU cpu) {
        int value = cpu.getRegisters().getRegister(valueRegister);
        int oldValue = cpu.getMainMemory().getAt(new Address(addressRegister + offset));

        cpu.getMainMemory().setAt((oldValue & 0xffff0000) | (value & 0x0000ffff), new Address(addressRegister + offset));
    }

    @Override
    public int getInstruction() {
        return 0;
    }
}
