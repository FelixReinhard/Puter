package org.assembler.parser.instructions;

import org.assembler.lexer.Token;
import org.cpu.instructions.Instruction;

import java.util.Optional;

public class NormalInstruction extends AssemblyInstruction {
    private final Instruction instruction;
    public NormalInstruction(Token tk, Instruction instruction) {
        super(tk);
        this.instruction = instruction;
    }

    @Override
    public Optional<Instruction> getInstruction() {
        return Optional.ofNullable(instruction);
    }
}
