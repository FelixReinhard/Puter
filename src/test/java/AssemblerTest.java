import org.assembler.Assembler;
import org.config.Config;
import org.cpu.CPU;
import org.cpu.instructions.DbgInstruction;
import org.cpu.instructions.helper.InstructionsHelper;
import org.junit.Assert;
import org.junit.Test;
import org.memory.Address;
import org.memory.MainMemory;

public class AssemblerTest {

    private CPU flashWith(Integer[] values) {
        MainMemory memory = new MainMemory(2);
        memory.setRangeAt(values, new Address(Config.START_PC_ADDRESS));
        return new CPU(memory);
    }

    private CPU flashWith(String input) {
        var assembler = new Assembler();
        var res = assembler.assemble(input);

        return flashWith(res.toArray(new Integer[0]));
    }

    @Test
    public void testAssemlberSimple() {
        var cpu = flashWith("#main li $a 99 dbg $a");
        cpu.run();

        Assert.assertEquals(99, (int)DbgInstruction.outputs.pop());
    }

    @Test
    public void testAssemlberSimple2() {
    }
}
