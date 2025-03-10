import org.config.Config;
import org.cpu.CPU;
import org.junit.Assert;
import org.junit.Test;
import org.memory.Address;
import org.memory.MainMemory;

public class InstructionTests {

    private CPU flashWith(int startingAddress, Integer[] values) {
        MainMemory memory = new MainMemory(2);
        memory.setRangeAt(values, new Address(startingAddress));
        return new CPU(memory);
    }

    @Test
    public void testMemory() {
        MainMemory memory = new MainMemory(2);


    }

    @Test
    public void testLI() {
        CPU cpu = flashWith(Config.START_PC_ADDRESS, new Integer[]{
                0x01300001
        });
        cpu.run();
        Assert.assertEquals(0x1, cpu.getRegisters().getRegister((byte)3));
    }

}
