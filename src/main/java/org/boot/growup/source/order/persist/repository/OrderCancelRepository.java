package org.boot.growup.source.order.persist.repository;

import org.boot.growup.source.order.persist.entity.OrderCancel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCancelRepository extends JpaRepository<OrderCancel, Long> {
}
