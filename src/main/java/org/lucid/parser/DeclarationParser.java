package org.lucid.parser;

import org.lucid.lexer.KeyWordToken;
import org.lucid.lexer.TokenType;

import java.util.Objects;

public class DeclarationParser {

    public void parseDeclaration(Parser p) {

        var next = p.next();
        if (next.isEmpty()) {
            // We are at the end of the declaration so no more declarations to parse.
            return;
        }

        if (next.get().getType() != TokenType.KEYWORD) {
            p.reportError("A declaration needs to start with a keyword", next.get());
            return;
        }

        var keyword = ((KeyWordToken)next.get()).word;
        switch (keyword) {
            case "import":
                parseImportDeclaration(p);
                break;
            case "from":
                parseFromImportDeclaration(p);
                break;
            case "var":
                parseGlobalVarDeclaration(p);
                break;
            case "class":
                parseClassDeclaration(p, false);
                break;
            case "fn":
                parseFunctionDeclaration(p, false);
                break;
            case "pub":
                // We need to also check the next one

                var next2 = p.next();
                if (next2.isEmpty()) {
                    p.reportError("'pub' must be followed by either 'fn' or 'class'", next.get());
                    return;
                }

                if (next2.get().getType() != TokenType.KEYWORD) {
                    p.reportError("'pub' must be followed by either 'fn' or 'class'", next.get());
                    return;
                }

                var fnOrClass = ((KeyWordToken)next2.get()).word;
                if (Objects.equals(fnOrClass, "class")) {
                    parseClassDeclaration(p, true);
                } else if (Objects.equals(fnOrClass, "fn")) {
                    parseFunctionDeclaration(p, true);
                } else {
                    p.reportError("'pub' must be followed by either 'fn' or 'class'", next.get());
                }
        }

    }
}
