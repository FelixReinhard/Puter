package org.cpu.instructions;

import org.cpu.CPU;

import java.util.Stack;

public class DbgInstruction implements Instruction {
    // Used for tests
    public static final Stack<Integer> outputs = new Stack<>();

    private final byte register;

    public DbgInstruction(byte register) {
        this.register = register;
    }

    @Override
    public void execute(CPU cpu) {
        int x = cpu.getRegisters().getRegister(register);
        System.out.printf("%d (%c)%n", x, (char)x);
        outputs.push(x);
    }

    @Override
    public int getInstruction() {
        return (0x10 << 24) | (register << 20);
    }
}
