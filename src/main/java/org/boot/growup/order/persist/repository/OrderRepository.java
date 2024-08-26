package org.boot.growup.order.persist.repository;

import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.order.persist.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByMerchantUidAndCustomer(String merchantUid, Customer customer);
}
