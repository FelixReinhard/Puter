package org.cpu;

import org.config.Config;

/**
 * Represents the 32 registers of the CPU
 * Following register names and functions
 * 0 ($0)       => always 0
 * 1 ($pc)      => program counter
 *
 */
public class Registers {

    static final byte PC = 1;

    /**
     * array of 32 registers of size 32bit
     */
    private final int[] registers;

    public Registers() {
        this.registers = new int[32];
        this.setRegister(PC, Config.START_PC_ADDRESS);
    }

    public int getProgramCounter() {
        return registers[PC];
    }

    public void incProgramCounter(int len) {
        registers[PC]+= len;
    }

    public void setRegister(byte register, int value) {
        registers[register] = value;
    }
    public int getRegister(byte register) {
        return registers[register];
    }

}
