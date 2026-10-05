package com.arthurrocha.ordersevents.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderEvent(
        UUID eventId,
        UUID aggregateId,
        EventType type,
        BigDecimal total,
        long version,
        Instant occurredAt) {
}
