package org.boot.growup.order.persist.repository;

import org.boot.growup.order.persist.entity.OrderCancel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCancelRepository extends JpaRepository<OrderCancel, Long> {
}
