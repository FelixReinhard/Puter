package org.cpu.instructions;

import org.cpu.CPU;
import org.memory.Address;

public class LaInstruction implements Instruction {

    private final byte destRegister;
    private final byte addressRegister;
    private final short offset;

    public LaInstruction(byte destRegister, byte addressRegister, short offset) {
        this.destRegister = destRegister;
        this.addressRegister = addressRegister;
        this.offset = offset;
    }

    @Override
    public void execute(CPU cpu) {
        int address = cpu.getRegisters().getRegister(addressRegister);
        int value = address + offset;
        cpu.getRegisters().setRegister(destRegister, value);
    }

    @Override
    public int getInstruction() {
        return (0x08 << 24) | (destRegister << 20) | (addressRegister << 16) | (0xffff & offset);
    }
}
