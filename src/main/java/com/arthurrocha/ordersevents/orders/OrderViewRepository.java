package com.arthurrocha.ordersevents.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrderViewRepository extends JpaRepository<OrderView, UUID> {
}
