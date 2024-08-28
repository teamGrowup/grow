package org.boot.growup.order.persist.repository;

import org.boot.growup.order.persist.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndSellerId(Long id, Long sellerId);
    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
}
