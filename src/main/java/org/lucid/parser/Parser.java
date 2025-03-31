package org.lucid.parser;

import org.lucid.lexer.Token;
import org.lucid.parser.statements.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    private final List<Statement> statements;

    private final DeclarationParser declarationParser;

    private String errorMessage;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.statements = new ArrayList<>();

        this.declarationParser = new DeclarationParser();
    }

    /**
     * Parses the tokens to the AST
     * @return AST in form of a list of statements
     */
    public List<Statement> parse() {
        while (current < tokens.size()) {
            declarationParser.parseDeclaration(this);
        }
    }

    public void addStatement(Statement s) {
        this.statements.add(s);
    }

    /**
     * Returns the next token and advances
     * @return If possible the next Token
     */
    public Optional<Token> next() {
        if (current >= tokens.size()) return Optional.empty();
        return Optional.of(tokens.get(current++));
    }


    /**
     * Returns the next token but doesn't advance
     * @return If possible the peeked Token
     */
    public Optional<Token> peek() {
        if (current >= tokens.size()) return Optional.empty();
        return Optional.of(tokens.get(current));
    }

    /**
     * Report and error with the token to get the line information in the source code
     * @param message The message to give
     * @param tk The token at which the error appeared
     */
    public void reportError(String message, Token tk) {
        this.errorMessage = String.format("%s at l.%d", message, tk.getLine());
    }

}
