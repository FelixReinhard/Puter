import org.config.Config;
import org.cpu.CPU;
import org.junit.Assert;
import org.junit.Test;
import org.memory.Address;
import org.memory.MainMemory;

public class InstructionTests {

    private CPU flashWith(int startingAddress, Byte[] values) {
        MainMemory memory = new MainMemory(2);
        memory.setRangeAt(values, new Address(startingAddress));
        return new CPU(memory);
    }

    @Test
    public void testLI() {
        CPU cpu = flashWith(Config.START_PC_ADDRESS, new Byte[]{0x01, 0b00110000, 0x00, 0x04, 0x00});
        cpu.run();
        Assert.assertEquals(1024, cpu.getRegisters().getRegister((byte)3));
    }

    @Test
    public void testAdd() {
        CPU cpu = flashWith(Config.START_PC_ADDRESS, new Byte[]{0x01, (byte)(3 << 4), 0x00, 0x00, 0x10, 0x50, (byte)((3 << 4) | 3)});
        cpu.run();

        Assert.assertEquals(32, cpu.getRegisters().getRegister((byte)3));
    }
}
