package com.arthurrocha.ordersevents.orders;

import java.util.UUID;

public class ConcurrentOrderModificationException extends RuntimeException {
    public ConcurrentOrderModificationException(UUID aggregateId) {
        super("Concurrent modification detected for this order: " + aggregateId);
    }
}
