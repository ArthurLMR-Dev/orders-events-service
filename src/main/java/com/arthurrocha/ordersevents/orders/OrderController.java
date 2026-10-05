package com.arthurrocha.ordersevents.orders;

import com.arthurrocha.ordersevents.eventstore.EventHistoryResponse;
import com.arthurrocha.ordersevents.eventstore.EventStoreService;
import com.arthurrocha.ordersevents.events.EventType;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final EventStoreService eventStoreService;

    @PostMapping
    public ResponseEntity<UUID> create(@Valid @RequestBody CreateOrderRequest request) {
        var id = orderService.create(request);
        return ResponseEntity.accepted().location(URI.create("/orders/" + id)).body(id);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable UUID id,
                                              @Valid @RequestBody ChangeOrderStatusRequest request) {
        orderService.changeStatus(id, request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    public OrderResponse find(@PathVariable UUID id) {
        return orderService.find(id);
    }

    @GetMapping("/{id}/history")
    public Page<EventHistoryResponse> history(@PathVariable UUID id,
                                              @RequestParam(required = false) EventType type,
                                              @PageableDefault(size = 20, sort = "version") Pageable pageable) {
        return eventStoreService.history(id, type, pageable).map(EventHistoryResponse::from);
    }
}
