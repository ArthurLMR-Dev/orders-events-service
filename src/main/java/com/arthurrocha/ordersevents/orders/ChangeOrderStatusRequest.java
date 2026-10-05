package com.arthurrocha.ordersevents.orders;

import jakarta.validation.constraints.NotNull;

public record ChangeOrderStatusRequest(@NotNull OrderStatus status) {
}
