package org.transfer;

public class RecieverBus<T> {

    private Bus<T> bus;

    public RecieverBus(Bus<T> bus) {
        this.bus = bus;
    }

    public void put(T value) {
        bus.getToSender().put(value);
    }

    public T get() {
        return bus.getToReciever().get();
    }
}
