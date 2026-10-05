package com.arthurrocha.ordersevents.orders;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "orders_view")
public class OrderView {
    @Id
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    @Column(nullable = false)
    private BigDecimal total;
    @Column(nullable = false)
    private long version;
    @Version
    @Column(name = "persistence_version", nullable = false)
    private long persistenceVersion;
    @Column(name = "last_event_id", nullable = false, unique = true)
    private UUID lastEventId;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public OrderView(UUID id, OrderStatus status, BigDecimal total, long version,
                     UUID lastEventId, Instant updatedAt) {
        this.id = id;
        this.status = status;
        this.total = total;
        this.version = version;
        this.lastEventId = lastEventId;
        this.updatedAt = updatedAt;
    }

    public void apply(OrderStatus status, long version, UUID eventId, Instant updatedAt) {
        this.status = status;
        this.version = version;
        this.lastEventId = eventId;
        this.updatedAt = updatedAt;
    }
}
