package org.cpu.instructions;

import org.cpu.CPU;
import org.memory.Address;
import org.utils.ByteUtils;

public class LhwInstruction implements Instruction {

    private final byte destRegister;
    private final byte addressRegister;
    private final short offset;

    public LhwInstruction(byte destRegister, byte addressRegister, short offset) {
        this.destRegister = destRegister;
        this.addressRegister = addressRegister;
        this.offset = offset;
    }

    @Override
    public void execute(CPU cpu) {
        int address = cpu.getRegisters().getRegister(addressRegister);
        int value = cpu.getMainMemory().getAt(new Address(address)) & 0x0000ffff;
        cpu.getRegisters().setRegister(destRegister, value);
    }
}
