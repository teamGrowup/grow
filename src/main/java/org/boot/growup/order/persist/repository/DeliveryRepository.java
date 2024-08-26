package org.boot.growup.order.persist.repository;

import org.boot.growup.order.persist.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
