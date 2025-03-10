package org.main;

import org.cpu.CPU;
import org.memory.MainMemory;
import org.memory.Memory;

public class Main {
    public static void main(String[] args) {
        Memory<Integer> ram = new MainMemory(16);
        CPU cpu = new CPU(ram);
    }
}