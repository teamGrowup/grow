package org.boot.growup.source.order.persist.repository;

import org.boot.growup.source.order.persist.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
