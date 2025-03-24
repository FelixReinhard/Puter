package org.assembler.parser.instructions;

import org.assembler.lexer.Token;
import org.cpu.Registers;
import org.cpu.instructions.Instruction;
import org.cpu.instructions.LaInstruction;

import java.util.HashMap;
import java.util.Optional;

public class SaveLoadAssemblyInstruction extends AssemblyInstruction {
    private final byte destRegister;
    private final String label;
    private final int offset;
    private final Class<? extends Instruction> instruction; // lw lhw lb

    // la $destRegister [.label + offset] => la $destRegister [$bp + (offset + labelOffset]
    public SaveLoadAssemblyInstruction(Token tk, Class<? extends Instruction> instruction, byte destRegister, String label, int offset) {
        super(tk);
        this.instruction = instruction;
        this.destRegister= destRegister;
        this.label = label;
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public Optional<Instruction> instantiate(HashMap<String, Integer> labelOffsets) {
        try {
            var constructor = instruction.getConstructor(byte.class, byte.class, short.class);
            int labelOffset = labelOffsets.get(label);
            return Optional.of(constructor.newInstance(destRegister, Registers.BP, (short) (offset + labelOffset)));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }
}
