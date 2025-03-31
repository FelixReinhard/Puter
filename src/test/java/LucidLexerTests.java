import org.junit.Assert;
import org.junit.Test;
import org.lucid.lexer.*;

public class LucidLexerTests {
    @Test
    public void fullLexing() {
        var lexer = new Lexer("var x: i32 = 0x10");
        var tokens = lexer.lex();

        Assert.assertEquals(tokens.get(0), new KeyWordToken("var"));
        Assert.assertEquals(tokens.get(1), new WordToken("x"));
        Assert.assertEquals(tokens.get(2), new Token(TokenType.DOUBLE_POINT));
        Assert.assertEquals(tokens.get(3), new WordToken("i32"));
        Assert.assertEquals(tokens.get(4), new Token(TokenType.EQUALS));
        Assert.assertEquals(tokens.get(5), new NumberToken(0x10));
    }

    @Test
    public void classLexing() {
        var lexer = new Lexer("class S {" +
                "S() {}" +
                "}");
        var tokens = lexer.lex();


        Assert.assertEquals(tokens.get(0), new KeyWordToken("class"));
        Assert.assertEquals(tokens.get(1), new WordToken("S"));
        Assert.assertEquals(tokens.get(2), new Token(TokenType.CURLY_OPEN));
        Assert.assertEquals(tokens.get(3), new WordToken("S"));
        Assert.assertEquals(tokens.get(4), new Token(TokenType.BRACKET_OPEN));
        Assert.assertEquals(tokens.get(5), new Token(TokenType.BRACKET_CLOSE));
        Assert.assertEquals(tokens.get(6), new Token(TokenType.CURLY_OPEN));
        Assert.assertEquals(tokens.get(7), new Token(TokenType.CURLY_CLOSE));
        Assert.assertEquals(tokens.get(8), new Token(TokenType.CURLY_CLOSE));
    }
}
