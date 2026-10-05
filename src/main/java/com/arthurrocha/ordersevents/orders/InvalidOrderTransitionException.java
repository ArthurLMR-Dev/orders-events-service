package com.arthurrocha.ordersevents.orders;

public class InvalidOrderTransitionException extends RuntimeException {
    public InvalidOrderTransitionException(OrderStatus current, OrderStatus requested) {
        super("Invalid order transition from " + current + " to " + requested);
    }
}
