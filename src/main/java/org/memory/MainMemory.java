package org.memory;

/**
 * Represents a Ram Stick
 */
public class MainMemory implements Memory<Integer> {

    private final Integer[] words;

    /**
     * How big in KiB
     * Allocates this memory.
     * @param size
     */
    public MainMemory(int size) {
        this.words = new Integer[1024 * size];
    }

    @Override
    public Integer getAt(Address address) {
        assert (address.getAddress() < words.length);
        return words[address.getAddress()];
    }

    @Override
    public Integer[] getRangeAt(Address address, int len) {
        assert (address.getAddress() + len < words.length);

        Integer[] ret = new Integer[len];
        for (int i = address.getAddress(); i < address.getAddress() + len; i++) {
            ret[i - address.getAddress()] = words[i];
        }
        return ret;
    }

    @Override
    public void setAt(Integer value, Address address) {
        assert (address.getAddress() < words.length);
        words[address.getAddress()] = value;
    }

    @Override
    public void setRangeAt(Integer[] value, Address address) {
        assert (address.getAddress() + value.length < words.length);
        for (int i = 0; i < value.length; i++) {
            words[address.getAddress() + i] = value[i];
        }
    }

}
