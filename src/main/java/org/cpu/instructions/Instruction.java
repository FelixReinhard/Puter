package org.cpu.instructions;

import org.cpu.CPU;

/**
 * Represents a CPU instruction
 */
public interface Instruction {
    /**
     * Gives the byte representation. Is used for the assembler feature.
     * @return 4 byte array
     */
    byte[] getBytes();

    /**
     * E.g. lw [$1] $2
     * @return
     */
    String toString();

    /**
     * Execute this instruction on the cpu.
     * @param cpu
     */
    void execute(CPU cpu);

}
