package org.cpu.instructions;

import org.cpu.CPU;
import org.memory.Address;
import org.utils.ByteUtils;

public class LwInstruction implements Instruction {
    private byte destRegister;
    private byte addressRegister;
    private short offset;

    public LwInstruction(byte destRegister, byte addressRegister, short offset) {
        this.destRegister = destRegister;
        this.addressRegister = addressRegister;
        this.offset = offset;
    }


    @Override
    public void execute(CPU cpu) {
        int address = cpu.getRegisters().getRegister(addressRegister);
        int value = ByteUtils.getNumberFromArray(cpu.getMainMemory().getRangeAt(new Address(address + offset), 4), 0, 4);
        cpu.getRegisters().setRegister(destRegister, value);
    }
}
