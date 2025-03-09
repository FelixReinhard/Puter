package org.cpu;

import org.cpu.instructions.Instruction;

/**
 * This class executes the instructions.
 */
public class CPU {

    private Registers registers;

    public CPU() {
        this.registers = new Registers();
    }

    /**
     * Run the CPU
     */
    public void run() {
        Instruction instruction;
        while (instruction = getNextInstruction()) {

        }

    }

    Instruction getNextInstruction() {

    }

}
