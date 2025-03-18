import org.assembler.lexer.Lexer;
import org.assembler.parser.Parser;
import org.assembler.parser.instructions.AssemblyInstruction;
import org.assembler.parser.instructions.LiteralAssemblyInstruction;
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
        var instructions = getInstructions("lw $3 [0x001]\n lb $ra [.data]");

        assert instructions != null;
        Assert.assertEquals(2, instructions.size());
    }
}
