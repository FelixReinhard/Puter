package org.utils;

public class ByteUtils {

    /**
     * Takes the first amount bytes of those number into a byte array
     * Little endian btw
     * @param words
     * @param amount
     * @return
     */
    public static byte[] getArrayFromNumbers(Integer[] words, int amount) {
        byte[] ret = new byte[amount];
        int j = 0;
        for (int i = 0; i < amount; i++) {
            ret[i] = (byte)((words[j] << ((i % 4) * 8) ) >>> 24);
            if ((i + 1) % 4 == 0) j++;
        }
        return ret;
    }

    public static int getNumberFromArray(Byte[] bytes, int startIndex, int endIndex) {
        int res = 0;

        assert (startIndex >= 0 && endIndex <= bytes.length);

        for (int i = startIndex; i < endIndex; i++) {
            res = (res << 8) | bytes[i];
        }
        return res;
    }

    public static int getNumberFromArray(byte[] bytes, int startIndex, int endIndex) {
        int res = 0;

        assert (startIndex >= 0 && endIndex <= bytes.length);

        for (int i = startIndex; i < endIndex; i++) {
            res = (res << 8) | (0xff & bytes[i]);
        }
        return res;
    }


    public static int getNumberFromArray(char[] bytes, int startIndex, int endIndex) {
        int res = 0;

        assert (startIndex >= 0 && endIndex <= bytes.length);

        for (int i = startIndex; i < endIndex; i++) {
            res = (res << 8) | bytes[i];
        }
        return res;
    }

    public static int mask(int value, int mask) {
        return value & mask;
    }
}
