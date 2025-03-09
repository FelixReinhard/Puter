package org.cpu.instructions;

import org.utils.ByteUtils;

import java.nio.ByteBuffer;
import java.util.List;

public class InstructionsHelper {

    public static Instruction fromBytes(Byte[] bytes) {
        return switch (bytes[0]) {
            case 0x01 -> new LIInstruction((byte) ((bytes[1] & 0b11110000) >>> 4), ByteUtils.getNumberFromArray(bytes, 1, 5) & 0x0fffffff);
            case 0x50 -> new AddInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x51 -> new SubInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x52 -> new MoveInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x53 -> new SwitchInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            default -> null;
        };

    }

    public static int getInstructionSize(byte optcode) {
        if (optcode > 0x00 && optcode <= 0x02) {
            return 5;
        }
        if (optcode >= 0x50 && optcode <= 0x54) {
            return 2;
        }
        return 1;
    }
}
