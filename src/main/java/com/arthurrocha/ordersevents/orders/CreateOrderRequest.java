package com.arthurrocha.ordersevents.orders;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateOrderRequest(
        @NotNull @DecimalMin(value = "0.01") BigDecimal total) {
}
