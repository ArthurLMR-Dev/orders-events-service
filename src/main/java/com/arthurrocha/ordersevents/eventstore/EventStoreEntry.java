package com.arthurrocha.ordersevents.eventstore;

import com.arthurrocha.ordersevents.events.EventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "event_store", indexes = @Index(name = "idx_event_store_aggregate_version",
        columnList = "aggregate_id, version"))
public class EventStoreEntry {
    @Id
    private UUID id;
    @Column(name = "event_id", nullable = false, unique = true)
    private UUID eventId;
    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String payload;
    @Column(nullable = false)
    private long version;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public EventStoreEntry(UUID eventId, UUID aggregateId, EventType type, String payload,
                           long version, Instant createdAt) {
        this.id = UUID.randomUUID();
        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.type = type;
        this.payload = payload;
        this.version = version;
        this.createdAt = createdAt;
    }

}
