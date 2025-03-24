import org.cpu.instructions.*;
import org.cpu.instructions.helper.InstructionsHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class InstructionEncoding {

    @Test
    public void testAllBinary() {
        // Optcode to Class
        HashMap<Integer, Class<? extends BinaryInstruction>> all = new HashMap<>();
        byte reg1 = 2;
        byte reg2 = 4;

        all.put(0x50, AddInstruction.class);
        all.put(0x51, SubInstruction.class);
        all.put(0x52, MoveInstruction.class);
        all.put(0x53, SwitchInstruction.class);
        all.put(0x54, MultInstruction.class);
        all.put(0x55, DivInstruction.class);
        all.put(0x56, AndInstruction.class);
        all.put(0x57, OrInstruction.class);
        all.put(0x58, XorInstruction.class);
        all.put(0x59, SlInstruction.class);
        all.put(0x5a, SrInstruction.class);
        all.put(0x5b, LandInstruction.class);
        all.put(0x5c, LorInstruction.class);

        for (Map.Entry<Integer, Class<? extends BinaryInstruction>> entry : all.entrySet()) {
            try {
                var c = entry.getValue().getConstructor(byte.class, byte.class);
                var instance = c.newInstance(reg1, reg2);

                Assert.assertEquals(String.format("%s Wrong optcode",
                        instance.getClass().getName()),
                        entry.getKey() << 24, instance.getInstruction() & 0xff000000
                );
                Assert.assertEquals(String.format("%s Wrong first register", instance.getClass().getName()),
                        reg1,
                        instance.getRegister1()
                );

                Assert.assertEquals(String.format("%s Wrong second register", instance.getClass().getName()),
                        reg2,
                        instance.getRegister2()
                );

                var instruction = InstructionsHelper.from(new Integer[]{instance.getInstruction()}, 4);
                Assert.assertEquals(
                        String.format("Wrong parsed by Instruction Helper %s != %s", instruction.getClass().getName(), instruction.getClass().getName()),
                        instruction.getClass().getName(),
                        instance.getClass().getName()
                );

                Assert.assertEquals(String.format("%s Wrong first register", instance.getClass().getName()),
                        reg1,
                        ((BinaryInstruction)instruction).getRegister1()
                );

                Assert.assertEquals(String.format("%s Wrong second register", instance.getClass().getName()),
                        reg2,
                        ((BinaryInstruction)instruction).getRegister2()
                );


            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

    }

    @Test
    public void testUnary() {
        byte reg1 = 2;

        var flp = new FlpInstruction(reg1);
        var not = new NotInstruction(reg1);

        var flp2 = InstructionsHelper.from(new Integer[]{flp.getInstruction()}, 4);
        var not2 = InstructionsHelper.from(new Integer[]{not.getInstruction()}, 4);

        Assert.assertEquals(flp.getInstruction(), flp2.getInstruction());
        Assert.assertEquals(not.getInstruction(), not2.getInstruction());

    }

    @Test
    public void testLoad() {
        byte reg1 = 4;
        byte reg2 = 10;
        int offset = 400;

        var li = new LIInstruction(reg1, offset);
        var lw = new LwInstruction(reg1, reg2, (short)offset);
        var lhw = new LhwInstruction(reg1, reg2, (short)offset);
        var lb = new LbInstruction(reg1, reg2, (short)offset);

        Assert.assertEquals(li.getInstruction(), InstructionsHelper.from(new Integer[]{li.getInstruction()}, 4).getInstruction());
        Assert.assertEquals(lw.getInstruction(), InstructionsHelper.from(new Integer[]{lw.getInstruction()}, 4).getInstruction());
        Assert.assertEquals(lhw.getInstruction(), InstructionsHelper.from(new Integer[]{lhw.getInstruction()}, 4).getInstruction());
        Assert.assertEquals(lb.getInstruction(), InstructionsHelper.from(new Integer[]{lb.getInstruction()}, 4).getInstruction());
    }

    @Test
    public void testCmp() {
        byte reg1 = 4;
        byte reg2 = 7;

        var cmp = new CmpInstruction(reg1, reg2);
        Assert.assertEquals(cmp.getInstruction(), InstructionsHelper.from(new Integer[]{cmp.getInstruction()}, 4).getInstruction());
    }
}
