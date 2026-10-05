package com.arthurrocha.ordersevents.eventstore;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import com.arthurrocha.ordersevents.events.EventType;

public interface EventStoreRepository extends JpaRepository<EventStoreEntry, UUID> {
    boolean existsByEventId(UUID eventId);
    Page<EventStoreEntry> findByAggregateIdOrderByVersionAsc(UUID aggregateId, Pageable pageable);
    Page<EventStoreEntry> findByAggregateIdAndTypeOrderByVersionAsc(UUID aggregateId, EventType type,
                                                                      Pageable pageable);
}
