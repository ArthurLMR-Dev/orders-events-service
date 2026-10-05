package com.arthurrocha.ordersevents.orders;

import com.arthurrocha.ordersevents.events.EventType;
import com.arthurrocha.ordersevents.events.KafkaTopics;
import com.arthurrocha.ordersevents.events.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderProjectionListener {
    private final OrderViewRepository repository;

    @Transactional
    @KafkaListener(topics = KafkaTopics.ORDERS_EVENTS, groupId = "projection-writer")
    public void handle(OrderEvent event) {
        var current = repository.findById(event.aggregateId()).orElse(null);
        if (current != null && (current.getLastEventId().equals(event.eventId())
                || current.getVersion() >= event.version())) {
            return;
        }
        if (event.type() == EventType.ORDER_CREATED) {
            if (current == null) {
                repository.save(new OrderView(event.aggregateId(), OrderStatus.CREATED, event.total(),
                        event.version(), event.eventId(), event.occurredAt()));
            }
            return;
        }
        if (current == null) {
            throw new OrderNotFoundException(event.aggregateId());
        }
        current.apply(statusFor(event.type()), event.version(), event.eventId(), event.occurredAt());
        repository.save(current);
    }

    private OrderStatus statusFor(EventType type) {
        return switch (type) {
            case ORDER_PAID -> OrderStatus.PAID;
            case ORDER_SHIPPED -> OrderStatus.SHIPPED;
            case ORDER_DELIVERED -> OrderStatus.DELIVERED;
            case ORDER_CANCELLED -> OrderStatus.CANCELLED;
            case ORDER_CREATED -> OrderStatus.CREATED;
        };
    }
}
