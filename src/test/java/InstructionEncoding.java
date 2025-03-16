import org.cpu.instructions.*;
import org.cpu.instructions.helper.InstructionsHelper;
import org.junit.Assert;
import org.junit.Test;

public class InstructionEncoding {

    @Test
    public void addTest() {
        var add = new AddInstruction((byte) 3, (byte) 5);
        int instr = add.getInstruction();

        var add2 = InstructionsHelper.from(new Integer[]{instr}, 4);
        Assert.assertTrue(add2 instanceof AddInstruction);
        Assert.assertEquals( 3, ((AddInstruction)add2).getRegister1());
        Assert.assertEquals( 5, ((AddInstruction)add2).getRegister2());
    }


    @Test
    public void subTest() {
        var sub = new SubInstruction((byte) 3, (byte) 5);
        int instr = sub.getInstruction();

        var add2 = InstructionsHelper.from(new Integer[]{instr}, 4);
        Assert.assertTrue(add2 instanceof SubInstruction);
        Assert.assertEquals( 3, ((SubInstruction)add2).getRegister1());
        Assert.assertEquals( 5, ((SubInstruction)add2).getRegister2());
    }

    @Test
    public void moveTest() {
        var sub = new MoveInstruction((byte) 3, (byte) 5);
        int instr = sub.getInstruction();

        var add2 = InstructionsHelper.from(new Integer[]{instr}, 4);
        Assert.assertTrue(add2 instanceof MoveInstruction);
        Assert.assertEquals( 3, ((MoveInstruction)add2).getRegister1());
        Assert.assertEquals( 5, ((MoveInstruction)add2).getRegister2());
    }

    @Test
    public void switchTest() {
        var sub = new SwitchInstruction((byte) 3, (byte) 5);
        int instr = sub.getInstruction();

        var add2 = InstructionsHelper.from(new Integer[]{instr}, 4);
        Assert.assertTrue(add2 instanceof SwitchInstruction);
        Assert.assertEquals( 3, ((BinaryInstruction)add2).getRegister1());
        Assert.assertEquals( 5, ((BinaryInstruction)add2).getRegister2());
    }
}
