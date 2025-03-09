package org.memory;

public class MainMemory implements Memory<Byte> {
    @Override
    public Byte getAt(Address address) {
        return 0;
    }

    @Override
    public Byte[] getRangeAt(Address address, int len) {
        return new Byte[0];
    }

    @Override
    public void setAt(Byte value, Address address) {

    }

    @Override
    public void setRangeAt(Byte[] value, Address address, int len) {

    }
}
