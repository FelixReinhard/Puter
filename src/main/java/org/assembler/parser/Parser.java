package org.assembler.parser;

import org.assembler.lexer.*;
import org.assembler.parser.instructions.*;
import org.cpu.instructions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        }
        return Optional.empty();
    }


    private Optional<AssemblyInstruction> instruction(String name, Token tk) {
        // Special case for all jump, because there can be labels.
        // Same for loading as we can use things from the #data section.
        return switch (name) {
            case "li" -> li(tk);
            case "lw" -> saveLoadInstructions(tk, "lw", LwInstruction.class);
            case "lhw" -> saveLoadInstructions(tk, "lhw", LhwInstruction.class);
            case "lb" -> saveLoadInstructions(tk, "lb", LbInstruction.class);
            case "sw" -> saveLoadInstructions(tk, "sw", SwInstruction.class);
            case "shw" -> saveLoadInstructions(tk, "shw", ShwInstruction.class);
            case "sb" -> saveLoadInstructions(tk, "sb", SbInstruction.class);
            default -> null;
        };
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

            return Optional.of(new SaveLoadAssemblyInstruction(tk, name, destReg, label, offset));
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
}
