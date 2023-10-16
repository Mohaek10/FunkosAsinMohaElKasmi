package dev.mohaek.services;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    private AtomicLong nextId = new AtomicLong(1);

    public Long generateId() {
        return nextId.getAndIncrement();
    }
}