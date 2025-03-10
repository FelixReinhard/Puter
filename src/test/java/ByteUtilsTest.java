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

    @Test
    public void testMult() {
        int a = Integer.MAX_VALUE;
        int b = Integer.MAX_VALUE;
        long res = Math.multiplyFull(a, b) + 44;

        int high = (int) (res >>> 32);
        int low = (int) (res & Integer.MAX_VALUE);

        long new_res = ((long) high << 32) | low;

        Assert.assertEquals(res, new_res);
    }

    @Test
    public void testGetArray() {
        int x = ByteUtils.getNumberFromArray(new byte[]{0x12, 0x34, 0x56, 0x78}, 0, 4);
        Assert.assertEquals(0x12345678, x);

        byte[] b = ByteUtils.getArrayFromNumbers(new Integer[]{x, 0x11335577}, 8);
        Assert.assertEquals(0x12, b[0]);
        Assert.assertEquals(0x34, b[1]);
        Assert.assertEquals(0x56, b[2]);
        Assert.assertEquals(0x78, b[3]);

    }
}
