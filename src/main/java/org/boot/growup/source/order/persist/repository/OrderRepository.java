package org.boot.growup.source.order.persist.repository;

import org.boot.growup.source.order.persist.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
