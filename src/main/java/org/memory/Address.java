package org.memory;

/**
 * Represents a 32bit address
 */
public class Address {
    private int address;

    public Address(int address) {
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    public Byte[] toBytes() {
        Byte[] ret = new Byte[4];

        ret[0] = (byte) ((address & 0xff000000) >>> 24);
        ret[1] = (byte) ((address & 0x00ff0000) >>> 16);
        ret[2] = (byte) ((address & 0x0000ff00) >>> 8);
        ret[3] = (byte) (address & 0x000000ff);

        return ret;
    }
}
