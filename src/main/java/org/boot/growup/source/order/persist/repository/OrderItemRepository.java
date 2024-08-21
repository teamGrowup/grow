package org.boot.growup.source.order.persist.repository;

import org.boot.growup.source.order.persist.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
