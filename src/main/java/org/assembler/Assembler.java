package org.assembler;


import org.assembler.lexer.Lexer;
import org.assembler.parser.Parser;
import org.assembler.parser.instructions.*;
import org.cpu.CPU;
import org.cpu.Registers;
import org.cpu.instructions.JmpInstruction;

import java.util.*;

/**
 * Assembles string into an int[] array that the cpu can execute.
 */
public class Assembler {

    private List<AssemblyInstruction> instructions;
    private List<Integer> outputWords;

    public Assembler() {
        outputWords = new ArrayList<>();
    }

    public static CPU assembleIntoCPU(String input) {
        var values = new Assembler().assemble(input);
        return CPU.flashWith(values.toArray(new Integer[0]));
    }

    public List<Integer> assemble(String input) {
        Lexer l = new Lexer(input);
        var tkns = l.getTokens();
        if (l.isHasError()) {
            System.out.println(l.getErrorMessage());
            return null;
        }
        Parser p = new Parser(tkns);
        this.instructions = p.getInstructions();
        if (p.isHasError()) {
            System.out.println(p.getErrorMessage());
            return null;
        }

        make();

        return outputWords;
    }


    /**
     * - All .x = v are located at the top under the #data label by convention and here its enforced.
     * - Therefore the first instruction is always an unconditional jmp [$bp + offsetToMainSection] to
     * - the #main label. If another label is follwed first jump over it too.
     *
     * - In the second interation we
     */
    private void make() {
        var labelAddresses = new HashMap<String, Integer>();
        var literals = new HashMap<String, Integer[]>();

        int i = 0;
        // First get the labels and how many literals there are.
        // then put the literals into the top add a jmp instruction.
        for (AssemblyInstruction assemblyInstruction : instructions) {
            if (assemblyInstruction instanceof SectionAssemblyInstruction) {
                // A section like this #main dont increase address but add the current offset to $bp
                String name = ((SectionAssemblyInstruction)assemblyInstruction).getSectionName();
                labelAddresses.put(name, i);
            } else if (assemblyInstruction instanceof LiteralAssemblyInstruction) {
                // a literal like this .x = {42}
                var name = ((LiteralAssemblyInstruction)assemblyInstruction).getName();
                var words = ((LiteralAssemblyInstruction)assemblyInstruction).getWords();
                literals.put(name, words);
            } else {
                // Normal, Jump and SaveLoad. All take one instruction so increase address
                i++;
            }
        }

        // Now add a jmp instruction to the #main. Note that it jumps to the offset to main + len of all literals + 1(itself)
        int offset = labelAddresses.get("main") + getLiteralsLength(literals) + 1;
        int instruction = new JmpInstruction(Registers.BP, offset).getInstruction();
        outputWords.add(instruction);

        // Now add all literals and save the actual offset in this hashmap
        var literalOffsets = new HashMap<String, Integer>();

        for (Map.Entry<String, Integer[]> entry : literals.entrySet()) {
            int literalOffset = outputWords.size(); // -1 and + 1 for first jmp instruction.
            outputWords.addAll(Arrays.asList(entry.getValue()));
            literalOffsets.put(entry.getKey(), literalOffset);
        }

        var sectionOffsets = new HashMap<String, Integer>();
        int i1 = 1 + getLiteralsLength(literals); // Is one because of the first jmp instruction that jumps over the
        // literals
        // Now in the second pass get the offsets of the sections
        for (AssemblyInstruction assemblyInstruction : instructions) {
            if (assemblyInstruction instanceof SectionAssemblyInstruction) {
                var name = ((SectionAssemblyInstruction)assemblyInstruction).getSectionName();
                sectionOffsets.put(name, i1);
            } else if (!(assemblyInstruction instanceof LiteralAssemblyInstruction)) {
                i1++;
            }
        }

        // Now add the normal instructions and populate labesl and sections.
        for (AssemblyInstruction assemblyInstruction : instructions) {
            if (assemblyInstruction instanceof NormalInstruction) {
                outputWords.add(((NormalInstruction)assemblyInstruction).getInstruction().getInstruction());
            } else if (assemblyInstruction instanceof SaveLoadAssemblyInstruction) {
                var optional = ((SaveLoadAssemblyInstruction)assemblyInstruction).instantiate(literalOffsets);
                if (optional.isEmpty()) {
                    System.out.println("Error assembling shouldnt happen.");
                    return;
                }
                outputWords.add(optional.get().getInstruction());
            } else if (assemblyInstruction instanceof JmpAssemblyInstruction) {
                var optional = ((JmpAssemblyInstruction)assemblyInstruction).instantiate(sectionOffsets);

                if (optional.isEmpty()) {
                    System.out.println("Error assembling shouldnt happen.");
                    return;
                }
                outputWords.add(optional.get().getInstruction());
            }
        }


    }

    private int getLiteralsLength(HashMap<String, Integer[]> literals) {
        int res = 0;
        for (Integer[] i : literals.values()) {
            res += i.length;
        }
        return res;
    }

    private void insertIntoSections() {

    }

}
