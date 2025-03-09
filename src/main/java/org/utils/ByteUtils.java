package org.utils;

public class ByteUtils {

    public static int getNumberFromArray(Byte[] bytes, int startIndex, int endIndex) {
        int res = 0;

        assert (startIndex >= 0 && endIndex <= bytes.length);

        for (int i = startIndex; i < endIndex; i++) {
            res = (res << 8) | bytes[i];
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
