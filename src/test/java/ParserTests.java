import org.assembler.lexer.Lexer;
import org.assembler.parser.Parser;
import org.assembler.parser.instructions.AssemblyInstruction;
import org.assembler.parser.instructions.LiteralAssemblyInstruction;
import org.assembler.parser.instructions.NormalInstruction;
import org.assembler.parser.instructions.SectionAssemblyInstruction;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTests {

    private List<AssemblyInstruction> getInstructions(String input) {
        Lexer l = new Lexer(input);
        var tokens = l.getTokens();
        if (l.isHasError()) {
            System.out.println(l.getErrorMessage());
            return null;
        }
        Parser p = new Parser(tokens);
        return p.getInstructions();
    }

    @Test
    public void testLiteral() {
        var instructions = getInstructions(".hello_world = {42, 43}");

        Assert.assertEquals(1, instructions.size());
        Assert.assertEquals("hello_world", ((LiteralAssemblyInstruction)instructions.get(0)).getName());
    }


    @Test
    public void testSection() {
        var instructions = getInstructions("#main");

        assert instructions != null;
        Assert.assertEquals(1, instructions.size());
        Assert.assertEquals("main", ((SectionAssemblyInstruction)instructions.get(0)).getSectionName());
    }

    @Test
    public void testLoad() {
        var instructions = getInstructions("lw $3 [0x001]\n lb $ra [.data + 4]");

        assert instructions != null;
        Assert.assertEquals(2, instructions.size());
    }


    @Test
    public void testJmp() {
        var instructions = getInstructions("#main jmp #main");

        assert instructions != null;
        Assert.assertEquals(2, instructions.size());
    }

    @Test
    public void binary() {
        var instructions = getInstructions("add $1 $2\n sub $4 $5\n" +
                "mov $1 $2\n swt $1 $2\n mult $1 $2 div $1 $2 and $1 $2 or $1 $2 xor $2 $5 sl $10 $2 sr $5 $9 land $2 $3 lor $7 $11");

        assert instructions != null;
        Assert.assertEquals(13, instructions.size());
        Assert.assertEquals(0x50 << 24, (instructions.get(0)).getInstruction().getInstruction() & 0xff000000);
        Assert.assertEquals(0x51 << 24, (instructions.get(1)).getInstruction().getInstruction() & 0xff000000);
    }
}
