package com.arthurrocha.ordersevents.eventstore;

import com.arthurrocha.ordersevents.events.KafkaTopics;
import com.arthurrocha.ordersevents.events.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventStoreListener {
    private final EventStoreService eventStoreService;

    @Transactional
    @KafkaListener(topics = KafkaTopics.ORDERS_EVENTS, groupId = "event-store-writer")
    public void handle(OrderEvent event) {
        eventStoreService.append(event);
        log.atInfo().addKeyValue("eventId", event.eventId())
                .addKeyValue("aggregateId", event.aggregateId())
                .addKeyValue("eventType", event.type())
                .log("Order event persisted");
    }
}
