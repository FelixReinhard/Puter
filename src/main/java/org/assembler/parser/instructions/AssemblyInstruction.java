package org.assembler.parser.instructions;

import org.assembler.lexer.Token;
import org.cpu.instructions.Instruction;

import java.util.Optional;

public class AssemblyInstruction {
    protected final Token firstToken;

    public AssemblyInstruction(Token tk) {
        this.firstToken = tk;
    }

    public Optional<Instruction> getInstruction() {
        return Optional.empty();
    }
}
