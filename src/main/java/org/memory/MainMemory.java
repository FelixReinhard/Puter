package org.memory;

/**
 * Represents a Ram Stick
 */
public class MainMemory implements Memory<Byte> {

    private final Byte[] bytes;

    /**
     * How big in KiB
     * Allocates this memory.
     * @param size
     */
    public MainMemory(int size) {
        this.bytes = new Byte[1024 * size];
    }

    @Override
    public Byte getAt(Address address) {
        assert (address.getAddress() < bytes.length);
        return bytes[address.getAddress()];
    }

    @Override
    public Byte[] getRangeAt(Address address, int len) {
        assert (address.getAddress() + len < bytes.length);

        Byte[] ret = new Byte[len];
        for (int i = address.getAddress(); i < address.getAddress() + len; i++) {
            ret[i - address.getAddress()] = bytes[i];
        }
        return ret;
    }

    @Override
    public void setAt(Byte value, Address address) {
        assert (address.getAddress() < bytes.length);
        bytes[address.getAddress()] = value;
    }

    @Override
    public void setRangeAt(Byte[] value, Address address) {
        assert (address.getAddress() + value.length < bytes.length);
        for (int i = 0; i < value.length; i++) {
            bytes[address.getAddress() + i] = value[i];
        }
    }
}
