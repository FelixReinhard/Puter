package org.transfer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A Message Passing Type to let different components communicate.
 */
public class Bus<T> {
    private final OneDirectionalBus<T> toSender;
    private final OneDirectionalBus<T> toReciever;

    private final SenderBus<T> sender;
    private final RecieverBus<T> reciever;

    public Bus() {
        this.toSender = new OneDirectionalBus<>();
        this.toReciever = new OneDirectionalBus<>();

        this.sender = new SenderBus<>(this);
        this.reciever = new RecieverBus<>(this);
    }

    public OneDirectionalBus<T> getToReciever() {
        return toReciever;
    }

    public OneDirectionalBus<T> getToSender() {
        return toSender;
    }

    public SenderBus<T> getSender() {
        return sender;
    }

    public RecieverBus<T> getReciever() {
        return reciever;
    }
}
