package org.cpu.instructions;

import org.cpu.CPU;

/**
 * Represents a CPU instruction.
 * Variable length between 1 byte and 5 bytes.
 */
public interface Instruction {
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
