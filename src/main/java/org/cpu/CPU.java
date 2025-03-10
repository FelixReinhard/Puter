package org.cpu;

import org.cpu.instructions.Instruction;
import org.cpu.instructions.helper.InstructionsHelper;
import org.memory.Address;
import org.memory.Memory;

/**
 * This class executes the instructions.
 */
public class CPU {

    private final Registers registers;

    private final Memory<Integer> mainMemory;

    public CPU(Memory<Integer> mainMemory) {
        this.registers = new Registers();
        this.mainMemory = mainMemory;
    }

    /**
     * Run the CPU
     */
    public void run() {
        Instruction instruction;
        while ((instruction = getNextInstruction()) != null) {
            instruction.execute(this);
        }
    }

    /**
     * Receive the next instruction with the $pc
     * @return
     */
    Instruction getNextInstruction() {
        try {
            byte optCode = (byte)((mainMemory.getAt(new Address(registers.getProgramCounter())) & 0xff000000) >>> 24);

            int len = InstructionsHelper.getInstructionSize(optCode); // len is in bytes
            Instruction ret = InstructionsHelper.from(mainMemory.getRangeAt(new Address(registers.getProgramCounter()), len / 4 + 1), len);

            registers.incProgramCounter(len);
            return ret;
        } catch (NullPointerException e) {
            // Return null if we dont find a value there
            return null;
        }
    }


    public Registers getRegisters() {
        return registers;
    }

    public Memory<Integer> getMainMemory() {
        return mainMemory;
    }
}
