package com.arthurrocha.ordersevents.events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void publish(OrderEvent event) {
        kafkaTemplate.send(KafkaTopics.ORDERS_EVENTS,
                event.aggregateId().toString(), event)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Failed to publish order event eventId={} aggregateId={}",
                                event.eventId(), event.aggregateId(), exception);
                    }
                });
    }
}
