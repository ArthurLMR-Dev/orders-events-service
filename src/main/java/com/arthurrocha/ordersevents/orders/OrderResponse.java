package com.arthurrocha.ordersevents.orders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(UUID id, OrderStatus status, BigDecimal total, Instant updatedAt) {
}
