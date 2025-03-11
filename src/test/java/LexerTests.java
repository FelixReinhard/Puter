import org.assembler.lexer.Lexer;
import org.assembler.lexer.RegisterToken;
import org.junit.Assert;
import org.junit.Test;

public class LexerTests {

    @Test
    public void testRegister() {
        Lexer l = new Lexer("$3");

        var res = l.getTokens();
        Assert.assertEquals(1, res.size());
        Assert.assertTrue(res.get(0) instanceof RegisterToken);
    }


    @Test
    public void test2Register() {
        Lexer l = new Lexer("$3 $3");

        var res = l.getTokens();
        Assert.assertEquals(2, res.size());
        Assert.assertTrue(res.get(0) instanceof RegisterToken);
        Assert.assertTrue(res.get(1) instanceof RegisterToken);
    }
}
