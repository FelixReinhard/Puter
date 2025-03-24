package org.assembler.parser;

import org.assembler.lexer.*;
import org.assembler.parser.instructions.*;
import org.cpu.instructions.*;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("ALL")
public class Parser {

    private final List<Token> tokens;
    private int current;

    private boolean hasError = false;
    private String errorMessage;

    private Map<String, Integer> labelToAddress;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    public List<AssemblyInstruction> getInstructions() {

        List<AssemblyInstruction> instructions = new ArrayList<>();

        while (true) {
            var optional = parse();
            if (optional.isPresent())
                instructions.add(optional.get());
            else
                return instructions;
        }
    }

    private Optional<AssemblyInstruction> parse() {
        var next = next();
        if (next.isEmpty())
            return Optional.empty();

        Token tk = next.get();

        if (tk.getType() == TokenType.WORD) {
            String s = ((WordToken)tk).getWord();

            switch (s.charAt(0)) {
                case '#':
                    // we have #main or #data or something
                    var stringBuilder = new StringBuilder(s);
                    stringBuilder.deleteCharAt(0);
                    return Optional.of(new SectionAssemblyInstruction(tk, stringBuilder.toString()));
                case '.':
                    // We have .name = {2424}
                    var ss = new StringBuilder(s);
                    ss.deleteCharAt(0);
                    return literal(tk, ss.toString());
                default:
                    return instruction(s, tk);
            }

        } else if (tk.getType() == TokenType.LABEL) {
            String s = ((LabelToken)tk).getLabelName();
            return Optional.of(new SectionAssemblyInstruction(tk, s));
        }
        return Optional.empty();
    }

    private Optional<AssemblyInstruction> instruction(String name, Token tk) {
        // Special case for all jump, because there can be labels.
        // Same for loading as we can use things from the #data section.
        return switch (name) {
            case "li" -> li(tk);
            case "dbg" -> dbg(tk);
            case "la" -> saveLoadInstructions(tk, "la", LaInstruction.class);
            case "lw" -> saveLoadInstructions(tk, "lw", LwInstruction.class);
            case "lhw" -> saveLoadInstructions(tk, "lhw", LhwInstruction.class);
            case "lb" -> saveLoadInstructions(tk, "lb", LbInstruction.class);
            case "sw" -> saveLoadInstructions(tk, "sw", SwInstruction.class);
            case "shw" -> saveLoadInstructions(tk, "shw", ShwInstruction.class);
            case "sb" -> saveLoadInstructions(tk, "sb", SbInstruction.class);
            case "add" -> binary(tk, AddInstruction.class);
            case "sub" -> binary(tk, SubInstruction.class);
            case "mov" -> binary(tk, MoveInstruction.class);
            case "swt" -> binary(tk, SwitchInstruction.class);
            case "mult" -> binary(tk, MultInstruction.class);
            case "div" -> binary(tk, DivInstruction.class);
            case "and" -> binary(tk, AndInstruction.class);
            case "or" -> binary(tk, OrInstruction.class);
            case "xor" -> binary(tk, XorInstruction.class);
            case "sl" -> binary(tk, SlInstruction.class);
            case "sr" -> binary(tk, SrInstruction.class);
            case "land" -> binary(tk, LandInstruction.class);
            case "lor" -> binary(tk, LorInstruction.class);
            case "cmp" -> binary(tk, CmpInstruction.class);
            case "flp" -> flp(tk);
            case "jmp" -> jumping(tk, "jmp", JmpInstruction.class);
            case "jeq" -> jumping(tk, "jeq", JeqInstruction.class);
            case "jne" -> jumping(tk, "jne", JneInstruction.class);
            case "jls" -> jumping(tk, "jls", JlsInstruction.class);
            case "jge" -> jumping(tk, "jge", JgeInstruction.class);

            default -> null;
        };
    }

    private Optional<AssemblyInstruction> dbg(Token tk) {
        var register1 = get(TokenType.REGISTER);
        if (register1.isEmpty()) {
            reportError(tk, "dgb instructions must be followed by a register.");
            return Optional.empty();
        }

        return Optional.of(new NormalInstruction(tk, new DbgInstruction(((RegisterToken) register1.get()).getRegister())));
    }

    private Optional<AssemblyInstruction> jumping(Token tk, String name, Class<? extends JmpInstruction> instruction) {
        var token = next();
        if (token.isEmpty()) {
            reportError(tk, String.format("%s instructions must be followed address."));
            return Optional.empty();
        }

        if (token.get().getType() == TokenType.ADDRESS) {
            var register = ((AddressToken)token.get()).getRegister();
            var offset = ((AddressToken)token.get()).getOffset();
            try {
                var constructor = instruction.getConstructor(byte.class, int.class);
                return Optional.of(
                        new NormalInstruction(tk, constructor.newInstance(register, offset))
                );
            } catch (Exception e) {
                reportError(tk, "Cannot instantiate in jmp.");
                return Optional.empty();
            }

        } else if (token.get().getType() == TokenType.ADDRESS_LABEL) {
            // Maybe remove TODO
            var label = ((LabelAddressToken) token.get()).getLabel();
            var offset = ((LabelAddressToken) token.get()).getOffset();

            return Optional.of(new JmpAssemblyInstruction(tk, instruction, label, offset));
        }
        else if (token.get().getType() == TokenType.LABEL) {
            var label = ((LabelToken) token.get()).getLabelName();

            return Optional.of(new JmpAssemblyInstruction(tk, instruction, label, 0));

        } else {
            reportError(tk, String.format("%s instructions must be followed by an address.", name));
            return Optional.empty();
        }
    }

    private Optional<AssemblyInstruction> not(Token tk) {
        var register1 = get(TokenType.REGISTER);
        if (register1.isEmpty()) {
            reportError(tk, "not instructions must be followed by a register.");
            return Optional.empty();
        }

        return Optional.of(new NormalInstruction(tk, new NotInstruction(((RegisterToken) register1.get()).getRegister())));
    }
    private Optional<AssemblyInstruction> flp(Token tk) {
        var register1 = get(TokenType.REGISTER);
        if (register1.isEmpty()) {
            reportError(tk, "flp instructions must be followed by a register.");
            return Optional.empty();
        }

        return Optional.of(new NormalInstruction(tk, new FlpInstruction(((RegisterToken) register1.get()).getRegister())));
    }

    private Optional<AssemblyInstruction> binary(Token tk, Class<? extends BinaryInstruction> instruction) {
        var register1 = get(TokenType.REGISTER);
        if (register1.isEmpty()) {
            reportError(tk, "binary instructions must be followed by a register.");
            return Optional.empty();
        }


        var register2 = get(TokenType.REGISTER);
        if (register2.isEmpty()) {
            reportError(tk, "binary instructions must be followed by a register.");
            return Optional.empty();
        }

        try {
            var constructor = instruction.getConstructor(byte.class, byte.class);
            var instance = constructor.newInstance(
                    ((RegisterToken) register1.get()).getRegister(),
                    ((RegisterToken)(register2.get())).getRegister()
            );
            return Optional.of(new NormalInstruction(tk, instance));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<AssemblyInstruction> saveLoadInstructions(Token tk, String name, Class<? extends Instruction> instructionClass) {

        var register = get(TokenType.REGISTER);
        if (register.isEmpty()) {
            reportError(tk, String.format("%s instructions must be followed by a register.", name));
            return Optional.empty();
        }

        // Either AddressToken or LabelAddressToken
        var token = next();
        if (token.isEmpty()) {
            reportError(tk, String.format("%s instruction needs a address", name));
            return Optional.empty();
        }

        if (token.get().getType() == TokenType.ADDRESS) {
            try {

                var constructor = instructionClass.getConstructor(byte.class, byte.class, short.class);
                var instance = constructor.newInstance(
                        ((RegisterToken) register.get()).getRegister(),
                        ((AddressToken) token.get()).getRegister(),
                        (short) ((AddressToken) token.get()).getOffset()
                );
                return Optional.of(new NormalInstruction(tk, instance));
            } catch (Exception ignored) {
                reportError(tk, "Couldn't create instance");
                return Optional.empty();
            }
        } else if (token.get().getType() == TokenType.ADDRESS_LABEL) {
            byte destReg = ((RegisterToken)register.get()).getRegister();
            String label = ((LabelAddressToken)token.get()).getLabel();
            int offset = ((LabelAddressToken)token.get()).getOffset();

            return Optional.of(new SaveLoadAssemblyInstruction(tk, instructionClass, destReg, label, offset));
        } else {
            reportError(tk, String.format("%s must have a address.", name));
            return Optional.empty();
        }
    }

    private Optional<AssemblyInstruction> li(Token tk) {
        var register = get(TokenType.REGISTER);
        if (register.isEmpty()) {
            reportError(tk, "li instructions must be followed by a register.");
            return Optional.empty();
        }

        var immediate = get(TokenType.NUMBER);

        if (immediate.isEmpty()) {
            reportError(tk, "li instructions must have a immediate number.");
            return Optional.empty();
        }

        return Optional.of(new NormalInstruction(tk, new LIInstruction(
                ((RegisterToken)(register.get())).getRegister(),
                ((NumberToken)immediate.get()).getNumber()
        )));
    }

    private Optional<AssemblyInstruction> literal(Token tk, String name) {
       // Already have a .x now = then literal
       if (!consume(TokenType.EQUALS)) {
           reportError(tk, "Literal definition must have an '='");
           return Optional.empty();
       }

       var literal = get(TokenType.LITERAL);
       if (literal.isEmpty()) {
           reportError(tk, "Literal must have a literal with {value, value...}");
           return Optional.empty();
       }

        return Optional.of(new LiteralAssemblyInstruction(tk, name, ((LiteralToken)literal.get()).getWords()));
    }

    /*
    Helper methods
     */

    private void reportError(Token tk, String message) {
        hasError = true;
        errorMessage = String.format("Lexing Error l.%d:%d: %s", tk.getLine(), tk.getPos(), message);
    }

    private Optional<Token> peek() {
        if (tokens.size() > current)
            return Optional.of(tokens.get(current));
        else
            return Optional.empty();
    }

    private Optional<Token> next() {
        if (tokens.size() > current)
            return Optional.of(tokens.get(current++));
        else
            return Optional.empty();
    }

    private boolean consume(TokenType type) {
        var optional = peek();
        if (optional.isEmpty()) return false;
        if (optional.get().getType() != type) return false;
        next();
        return true;
    }

    private Optional<Token> get(TokenType type) {
        var optional = peek();
        if (optional.isEmpty()) return Optional.empty();
        if (optional.get().getType() != type) return Optional.empty();
        return next();
    }

    public boolean isHasError() {
        return hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
