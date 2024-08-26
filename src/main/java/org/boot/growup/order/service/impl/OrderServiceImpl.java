package org.boot.growup.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.constant.PayMethod;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.order.dto.OrderDTO;
import org.boot.growup.order.persist.entity.Order;
import org.boot.growup.order.persist.entity.OrderItem;
import org.boot.growup.order.persist.repository.OrderRepository;
import org.boot.growup.order.service.OrderService;
import org.boot.growup.product.persist.entity.ProductOption;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public Order processNormalOrder(OrderDTO orderDTO, Map<ProductOption, Integer> productOptionCountMap, Customer customer) {
        // 1. orderDTO로 Order 생성.
        Order order = Order.of(orderDTO, customer, PayMethod.CARD);

        // 2. 생성된 Order의 주문 번호 생성 로직 실행.
        place(productOptionCountMap, order);

        // 3. Order 객체 저장
        orderRepository.save(order);

        return order;
    }

    @Override
    public void completeOrder(String merchantUid, Customer customer) {
        // 1. 주문번호 + 현재 사용자 정보 토대로 Order 객체 조회.
        Order order = orderRepository.findByMerchantUidAndCustomer(merchantUid, customer)
                .orElseThrow(() -> new BaseException(ErrorCode.ORDER_NOT_FOUND));

        // 2. 해당 Order 객체 내 OrderItem들을 모두 Payed 상태로 변경.
        order.getOrderItems().forEach(OrderItem::payed);

        // 3. Order 객체 저장
        orderRepository.save(order);
    }

    @Override
    public void rejectNormalOrder(String merchantUid, Customer customer) {
        // 1. 주문번호 + 현재 사용자 정보 토대로 Order 객체 조회.
        Order order = orderRepository.findByMerchantUidAndCustomer(merchantUid, customer)
                .orElseThrow(() -> new BaseException(ErrorCode.ORDER_NOT_FOUND));

        // 2. 해당 Order 객체 내 OrderItem들을 모두 Payed 상태로 변경.
        order.getOrderItems().forEach(OrderItem::rejected);

        // 3. Order 객체 저장
        orderRepository.save(order);
    }

    private void place(Map<ProductOption, Integer> productOptionCountMap, Order order) {
        // 1. 주문상품의 허가상태 확인 및 주문상품옵션 재고 확인
        validate(productOptionCountMap);

        // 2. PRE_PAYED 상태의 orderItem 생성, 상품옵션 재고량 감소 및 총주문금액 계산
        ordered(productOptionCountMap, order);
    }

    private void validate(Map<ProductOption, Integer> productOptionCountMap) {
        productOptionCountMap.forEach(ProductOption::validate);
    }

    private void ordered(Map<ProductOption, Integer> productOptionCountMap, Order order) {
        productOptionCountMap.forEach((option, count) -> {
            OrderItem orderItem = OrderItem.of(option, count); // orderItem들을 만듦.
            orderItem.prePayed(); // orderItem들을 PRE_PAYED로 변경
            option.decreaseStock(count); // productoption n개 만큼 재고량 감소시킴.
            orderItem.ordered(order); // 총주문금액 계산 ('개별상품 배송비 + 개별 상품옵션 가격*수량'들의 총합) 및 order 객체에 연결
        });
        order.designateMerchantUid(); // 주문번호 생성
    }

}
