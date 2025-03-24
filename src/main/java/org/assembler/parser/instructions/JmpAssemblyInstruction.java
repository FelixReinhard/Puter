package org.assembler.parser.instructions;

import org.assembler.lexer.Token;
import org.cpu.Registers;
import org.cpu.instructions.Instruction;

import java.util.HashMap;
import java.util.Optional;

public class JmpAssemblyInstruction extends AssemblyInstruction {
    private final Class<? extends Instruction> instruction;
    private final String labelName;
    private final int offset;

    public JmpAssemblyInstruction(Token tk, Class<? extends Instruction> instruction, String labelName, int offset) {
        super(tk);
        this.instruction = instruction;
        this.labelName = labelName;
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public Optional<Instruction> instantiate(HashMap<String, Integer> labelOffsets) {
        //System.out.println("Jmp: " + labelName + " with offset: " + labelOffsets.get(labelName).toString());

        try {
            var constructor = instruction.getConstructor(byte.class, int.class);
            int labelOffset = labelOffsets.get(labelName);
            return Optional.of(constructor.newInstance(Registers.BP, (short) (offset + labelOffset)));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }
}
