package com.arthurrocha.ordersevents.orders;

import com.arthurrocha.ordersevents.events.EventType;
import com.arthurrocha.ordersevents.events.OrderEvent;
import com.arthurrocha.ordersevents.events.OrderEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderViewRepository orderViewRepository;
    @Mock
    private OrderEventPublisher eventPublisher;
    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldPublishCreatedEventWithVersionOne() {
        var request = new CreateOrderRequest(new BigDecimal("49.90"));
        orderService.create(request);

        var captor = ArgumentCaptor.forClass(OrderEvent.class);
        verify(eventPublisher).publish(captor.capture());
        assertThat(captor.getValue().type()).isEqualTo(EventType.ORDER_CREATED);
        assertThat(captor.getValue().version()).isOne();
        assertThat(captor.getValue().total()).isEqualByComparingTo("49.90");
    }

    @Test
    void shouldRejectSkippingAStatus() {
        var id = UUID.randomUUID();
        var view = new OrderView(id, OrderStatus.CREATED, new BigDecimal("10.00"), 1,
                UUID.randomUUID(), java.time.Instant.now());
        when(orderViewRepository.findById(id)).thenReturn(java.util.Optional.of(view));

        assertThatThrownBy(() -> orderService.changeStatus(id,
                new ChangeOrderStatusRequest(OrderStatus.SHIPPED)))
                .isInstanceOf(InvalidOrderTransitionException.class);
    }
}
