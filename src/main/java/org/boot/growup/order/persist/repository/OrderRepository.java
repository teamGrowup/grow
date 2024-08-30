package org.boot.growup.order.persist.repository;

import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.order.persist.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByMerchantUidAndCustomer(String merchantUid, Customer customer);
    Page<Order> findByCustomer(Customer customer, Pageable pageable);

    @Query("select o from Order o join fetch o.orderItems oi where o.merchantUid =:merchantUid and o.customer =:customer")
    Optional<Order> findByMerchantUidAndCustomerWithOrderItems(
            @Param("merchantUid") String merchantUid,
            @Param("customer") Customer customer
    );

}
