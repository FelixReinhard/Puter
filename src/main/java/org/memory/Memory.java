package org.memory;

public interface Memory<T> {

    T getAt(Address address);
    T[] getRangeAt(Address address, int len);

    void setAt(T value, Address address);
    void setRangeAt(T[] value, Address address, int len);
}
