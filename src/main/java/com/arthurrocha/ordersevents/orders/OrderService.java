package com.arthurrocha.ordersevents.orders;

import com.arthurrocha.ordersevents.events.EventType;
import com.arthurrocha.ordersevents.events.OrderEvent;
import com.arthurrocha.ordersevents.events.OrderEventPublisher;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderViewRepository orderViewRepository;
    private final OrderEventPublisher eventPublisher;

    public UUID create(CreateOrderRequest request) {
        var id = UUID.randomUUID();
        eventPublisher.publish(new OrderEvent(UUID.randomUUID(), id, EventType.ORDER_CREATED,
                request.total(), 1, Instant.now()));
        return id;
    }

    public void changeStatus(UUID id, ChangeOrderStatusRequest request) {
        var current = orderViewRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (!isAllowed(current.getStatus(), request.status())) {
            throw new InvalidOrderTransitionException(current.getStatus(), request.status());
        }
        eventPublisher.publish(new OrderEvent(UUID.randomUUID(), id,
                eventTypeFor(request.status()), current.getTotal(), current.getVersion() + 1, Instant.now()));
    }

    public OrderResponse find(UUID id) {
        var view = orderViewRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        return new OrderResponse(view.getId(), view.getStatus(), view.getTotal(), view.getUpdatedAt());
    }

    private boolean isAllowed(OrderStatus current, OrderStatus requested) {
        return switch (current) {
            case CREATED -> requested == OrderStatus.PAID || requested == OrderStatus.CANCELLED;
            case PAID -> requested == OrderStatus.SHIPPED || requested == OrderStatus.CANCELLED;
            case SHIPPED -> requested == OrderStatus.DELIVERED || requested == OrderStatus.CANCELLED;
            case DELIVERED, CANCELLED -> false;
        };
    }

    private EventType eventTypeFor(OrderStatus status) {
        return switch (status) {
            case PAID -> EventType.ORDER_PAID;
            case SHIPPED -> EventType.ORDER_SHIPPED;
            case DELIVERED -> EventType.ORDER_DELIVERED;
            case CANCELLED -> EventType.ORDER_CANCELLED;
            case CREATED -> throw new IllegalArgumentException("An order cannot transition to CREATED");
        };
    }
}
