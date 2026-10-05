package com.arthurrocha.ordersevents.events;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
public class KafkaConfiguration {
    @Bean
    NewTopic ordersEventsTopic() {
        return new NewTopic(KafkaTopics.ORDERS_EVENTS, 3, (short) 1);
    }

    @Bean
    NewTopic ordersEventsDlt() {
        return new NewTopic(KafkaTopics.ORDERS_EVENTS_DLT, 3, (short) 1);
    }

    @Bean
    DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<String, OrderEvent> template) {
        var recoverer = new DeadLetterPublishingRecoverer(template,
                (record, exception) -> new TopicPartition(
                        KafkaTopics.ORDERS_EVENTS_DLT, record.partition()));
        var backOff = new ExponentialBackOff(1_000, 2.0);
        backOff.setMaxElapsedTime(15_000);
        return new DefaultErrorHandler(recoverer, backOff);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, OrderEvent> kafkaListenerContainerFactory(
            DefaultErrorHandler errorHandler) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, OrderEvent>();
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}
