package com.arthurrocha.ordersevents;

import org.springframework.boot.SpringApplication;

public class TestOrdersEventsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(OrdersEventsServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
