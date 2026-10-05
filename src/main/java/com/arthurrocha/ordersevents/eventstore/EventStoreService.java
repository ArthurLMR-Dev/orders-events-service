package com.arthurrocha.ordersevents.eventstore;

import com.arthurrocha.ordersevents.events.OrderEvent;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;
import com.arthurrocha.ordersevents.events.EventType;
import com.arthurrocha.ordersevents.orders.ConcurrentOrderModificationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventStoreService {
    private final EventStoreRepository repository;
    private final ObjectMapper objectMapper;

    public void append(OrderEvent event) {
        if (repository.existsByEventId(event.eventId())) {
            return;
        }
        try {
            repository.saveAndFlush(new EventStoreEntry(event.eventId(), event.aggregateId(), event.type(),
                    serialize(event), event.version(), event.occurredAt()));
        } catch (DataIntegrityViolationException exception) {
            if (isConstraintViolation(exception, "idx_event_store_aggregate_version")) {
                throw new ConcurrentOrderModificationException(event.aggregateId());
            }
            if (!isConstraintViolation(exception, "event_store_event_id_key")) {
                throw exception;
            }
        }
    }

    private boolean isConstraintViolation(DataIntegrityViolationException exception, String constraintName) {
        Throwable cause = exception;
        while (cause != null) {
            if (cause.getMessage() != null && cause.getMessage().contains(constraintName)) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    public Page<EventStoreEntry> history(UUID aggregateId, EventType type, Pageable pageable) {
        return type == null
                ? repository.findByAggregateIdOrderByVersionAsc(aggregateId, pageable)
                : repository.findByAggregateIdAndTypeOrderByVersionAsc(aggregateId, type, pageable);
    }

    private String serialize(OrderEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JacksonException exception) {
            throw new IllegalStateException("Could not serialize order event", exception);
        }
    }
}
