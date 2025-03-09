import org.junit.Assert;
import org.junit.Test;
import org.memory.Address;
import org.utils.ByteUtils;

public class ByteUtilsTest {

    @Test
    public void testFromBytes() {
        // Use char for unsigned byte.
        char[] arr = {0x00, 0x24, 0};
        Assert.assertEquals(9216, ByteUtils.getNumberFromArray(arr, 0, 3));

        arr = new char[]{ 0xff, 0xff, 0x00};

        Assert.assertEquals(16776960, ByteUtils.getNumberFromArray(arr, 0, 3));

    }
    @Test
    public void testMask() {
        Assert.assertEquals(0x40, 0xffffff4f & 0xf0);
        Assert.assertEquals((int)Math.pow(2, 27) - 1, 0x07ffffff);
    }

    @Test
    public void testAdressToBytes() {
        Address a = new Address(Integer.MAX_VALUE);
        Byte[] bytes = a.toBytes();
        Assert.assertEquals((byte)0xff, (byte)bytes[2]);
        Assert.assertEquals(-1, ByteUtils.getNumberFromArray(bytes, 0, 4));
    }
}
