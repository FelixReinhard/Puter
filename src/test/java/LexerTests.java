import org.assembler.lexer.*;
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
        Lexer l = new Lexer("$3\n$pc $ra $sp $cp $lo $HI");

        var res = l.getTokens();
        Assert.assertEquals(7, res.size());
        Assert.assertTrue(res.get(0) instanceof RegisterToken);
        Assert.assertTrue(res.get(1) instanceof RegisterToken);
    }

    @Test
    public void testAddress() {
        Lexer l = new Lexer("[$3 + 0x001]");
        var res = l.getTokens();

        Assert.assertEquals(1, res.size());
        Assert.assertTrue(res.get(0) instanceof AddressToken);
        Assert.assertEquals(3, ((AddressToken) (res.get(0))).getRegister());
        Assert.assertEquals(1, ((AddressToken) (res.get(0))).getOffset());
    }

    @Test
    public void testFull() {
        Lexer l = new Lexer(("lw $cp [$0]"));
        var res = l.getTokens();

        Assert.assertEquals(3, res.size());
        Assert.assertTrue(res.get(0) instanceof WordToken);
        Assert.assertTrue(res.get(1) instanceof RegisterToken);
        Assert.assertTrue(res.get(2) instanceof AddressToken);
        Assert.assertEquals(0, ((AddressToken) (res.get(2))).getRegister());
        Assert.assertEquals(0, ((AddressToken) (res.get(2))).getOffset());
    }

    @Test
    public void testLabel() {
        Lexer l = new Lexer(("#Label lw"));
        var res = l.getTokens();

        Assert.assertEquals(2, res.size());
        Assert.assertTrue(res.get(0) instanceof LabelToken);
        Assert.assertTrue(res.get(1) instanceof WordToken);
    }


    @Test
    public void test() {
        Lexer l = new Lexer(("add $3 $4 \n lw $ra [400]"));
        var res = l.getTokens();

        Assert.assertEquals(6, res.size());
    }

    @Test
    public void testNumber() {
        Lexer l = new Lexer(("43"));
        var res = l.getTokens();

        Assert.assertEquals(1, res.size());
        Assert.assertEquals(TokenType.NUMBER, res.get(0).getType());
    }


    @Test
    public void testLiteral() {
        Lexer l = new Lexer((".x = { 0x42, 32  }"));
        var res = l.getTokens();

        Assert.assertEquals(3, res.size());
    }
}
