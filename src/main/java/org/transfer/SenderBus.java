package org.transfer;

public class SenderBus<T> {

    private Bus<T> bus;


    public SenderBus(Bus<T> bus) {
        this.bus = bus;
    }

    public void put(T value) {
        bus.getToReciever().put(value);
    }

    public T get() {
        return bus.getToSender().get();
    }
}
