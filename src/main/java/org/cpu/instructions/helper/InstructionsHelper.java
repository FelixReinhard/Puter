package org.cpu.instructions.helper;

import org.cpu.instructions.*;
import org.utils.ByteUtils;

public class InstructionsHelper {

    public static Instruction from(Integer[] words, int amount) {

        byte[] bytes = ByteUtils.getArrayFromNumbers(words, amount);

        return switch (bytes[0]) {
            case 0x01 -> new LIInstruction((byte) ((bytes[1] & 0b11110000) >>> 4), ByteUtils.getNumberFromArray(bytes, 1, 4) & 0x0fffff);

            case 0x02 -> new LwInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111), (short)ByteUtils.getNumberFromArray(bytes, 2, 4));
            case 0x03 -> new LhwInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111), (short)ByteUtils.getNumberFromArray(bytes, 2, 4));
            case 0x04 -> new LbInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111), (short)ByteUtils.getNumberFromArray(bytes, 2, 4));
            case 0x05 -> new SwInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111), (short)ByteUtils.getNumberFromArray(bytes, 2, 4));

            case 0x50 -> new AddInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x51 -> new SubInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x52 -> new MoveInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x53 -> new SwitchInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x54 -> new MultInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x55 -> new DivInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x56 -> new AndInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x57 -> new OrInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x58 -> new XorInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x59 -> new SlInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x5a -> new SrInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x5b -> new LandInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));
            case 0x5c -> new LorInstruction((byte) ((bytes[1] & 0b11110000) >>> 4),  (byte) (bytes[1] & 0b00001111));


            case 0x60 -> new FlpInstruction((byte) ((bytes[1] & 0b11110000) >>> 4));
            case 0x61 -> new NotInstruction((byte) ((bytes[1] & 0b11110000) >>> 4));

            /* 0x70 deprecated and removed */
            case 0x71 -> new JmpInstruction((byte)(bytes[1] & 0xf0), ByteUtils.getNumberFromArray(bytes, 1, 4) & 0x0fffffff);
            case 0x72 -> new JeqInstruction((byte)(bytes[1] & 0xf0), ByteUtils.getNumberFromArray(bytes, 1, 4) & 0x0fffffff);
            case 0x73 -> new JneInstruction((byte)(bytes[1] & 0xf0), ByteUtils.getNumberFromArray(bytes, 1, 4) & 0x0fffffff);
            case 0x74 -> new JlsInstruction((byte)(bytes[1] & 0xf0), ByteUtils.getNumberFromArray(bytes, 1, 4) & 0x0fffffff);
            case 0x75 -> new JgeInstruction((byte)(bytes[1] & 0xf0), ByteUtils.getNumberFromArray(bytes, 1, 4) & 0x0fffffff);

            case 0x10 -> new DbgInstruction((byte)(bytes[1] & 0xf0));

            default -> null;
        };

    }

    public static int getInstructionSize(byte optcode) {
        return 4;
//        if(optcode >= 0x01 && optcode <= 0x04) {
//            return 4;
//        }
//        else if (optcode >= 0x50 && optcode <= 0x5c) {
//            return 2;
//        }
//        else if (optcode >= 0x60 && optcode <= 0x70) {
//            return 1;
//        }
//        return 1;
    }
}
