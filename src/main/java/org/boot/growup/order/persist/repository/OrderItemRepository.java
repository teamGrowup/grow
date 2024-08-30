package org.boot.growup.order.persist.repository;

import org.boot.growup.common.constant.OrderStatus;
import org.boot.growup.order.persist.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndSellerId(Long id, Long sellerId);
    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
    // 판매자 ID와 주문 상태를 기반으로 주문 항목을 조회하는 메서드 추가
    List<OrderItem> findBySellerIdAndOrderStatus(Long sellerId, OrderStatus orderStatus);
}
