import org.assembler.Assembler;
import org.config.Config;
import org.cpu.CPU;
import org.cpu.instructions.JmpInstruction;
import org.cpu.instructions.helper.InstructionsHelper;
import org.junit.Assert;
import org.junit.Test;
import org.memory.Address;
import org.memory.MainMemory;

public class AssemblerTest {

    private CPU flashWith(int startingAddress, Integer[] values) {
        MainMemory memory = new MainMemory(2);
        memory.setRangeAt(values, new Address(startingAddress));
        return new CPU(memory);
    }

    @Test
    public void testAssemlberSimple() {
        var assembler = new Assembler();
        var res = assembler.assemble("#main jmp #fn add $3 $5 #fn li $7 99 dbg $7");

        var i = InstructionsHelper.from(res.get(1));
        var i2 = InstructionsHelper.from(res.get(3));

        var cpu = flashWith(Config.START_PC_ADDRESS, res.toArray(new Integer[0]));
        cpu.run();


    }
}
