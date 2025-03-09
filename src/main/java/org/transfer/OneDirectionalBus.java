package org.transfer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class OneDirectionalBus<T> {

    private static final int BUFFER_SIZE = 512;


    private final ReentrantLock lock;
    private final Condition hasElement;
    private final Condition hasRoom;

    private final List<T> buffer;

    public OneDirectionalBus() {
        this.buffer = new ArrayList<>();

        this.lock = new ReentrantLock();
        this.hasElement = lock.newCondition();
        this.hasRoom = lock.newCondition();
    }

    public void put(T value) {
        try {
            lock.lock();

            while (!(buffer.size() >= BUFFER_SIZE )) {
                hasRoom.await();
            }

            buffer.add(value);
            hasElement.notifyAll();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public T get() {
        try {
            lock.lock();

            while (buffer.isEmpty()) {
                hasElement.await();
            }

            hasRoom.notifyAll();

            return buffer.remove(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
