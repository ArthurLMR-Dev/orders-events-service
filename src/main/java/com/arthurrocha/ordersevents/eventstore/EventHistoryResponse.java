package com.arthurrocha.ordersevents.eventstore;

import com.arthurrocha.ordersevents.events.EventType;
import java.time.Instant;
import java.util.UUID;

public record EventHistoryResponse(UUID eventId, EventType type, String payload,
                                   long version, Instant createdAt) {
    public static EventHistoryResponse from(EventStoreEntry entry) {
        return new EventHistoryResponse(entry.getEventId(), entry.getType(), entry.getPayload(),
                entry.getVersion(), entry.getCreatedAt());
    }
}
