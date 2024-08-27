package org.boot.growup.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.constant.PayMethod;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.order.dto.OrderDTO;
import org.boot.growup.order.dto.request.PatchShipmentRequestDTO;
import org.boot.growup.order.persist.entity.Delivery;
import org.boot.growup.order.persist.entity.Order;
import org.boot.growup.order.persist.entity.OrderItem;
import org.boot.growup.order.persist.repository.OrderItemRepository;
import org.boot.growup.order.persist.repository.OrderRepository;
import org.boot.growup.order.service.OrderService;
import org.boot.growup.product.persist.entity.ProductOption;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

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

        // 2. 해당 Order 객체 내 OrderItem들을 모두 Payed 상태로 변경 및 OrderItem당 수수료 및 판매정산금 계산
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

    @Override
    public void preshippedOrder(Long orderItemId, Seller seller) {
        // 1. orderItemId 및 Seller로 OrderItem 조회
        OrderItem orderItem = orderItemRepository.findByIdAndSellerId(orderItemId, seller.getId())
                                    .orElseThrow(() -> new BaseException(ErrorCode.ORDER_ITEM_NOT_FOUND));


        log.info("orderItem -> {}", orderItem.getOrderStatus());
        // 2. preshipped() 상태로 변경
        orderItem.preShipped();

        // 3. OrderItem 객체 저장
        orderItemRepository.save(orderItem);
    }

    @Override
    public void shippedOrder(Long orderItemId, Seller seller) {
        // 1. orderItemId 및 Seller로 OrderItem 조회
        OrderItem orderItem = orderItemRepository.findByIdAndSellerId(orderItemId, seller.getId())
                .orElseThrow(() -> new BaseException(ErrorCode.ORDER_ITEM_NOT_FOUND));

        // 2. shipped() 상태로 변경
        orderItem.shipped();

        // 3. OrderItem 객체 저장
        orderItemRepository.save(orderItem);
    }

    @Override
    public void shipmentOrder(Long orderItemId, Seller seller, PatchShipmentRequestDTO patchShipmentRequestDTO) {
        // 1. orderItemId 및 Seller로 OrderItem 조회
        OrderItem orderItem = orderItemRepository.findByIdAndSellerId(orderItemId, seller.getId())
                .orElseThrow(() -> new BaseException(ErrorCode.ORDER_ITEM_NOT_FOUND));

        // 2. Delivery 객체 생성
        Delivery delivery = Delivery.of(patchShipmentRequestDTO, orderItem);

        // 3. shipment() 상태로 변경 및 배송 정보 설정
        orderItem.shipment(delivery);

        // 4. OrderItem 객체 저장
        orderItemRepository.save(orderItem);
    }

    @Override
    public void transitOrder(Long orderItemId, Seller seller) {
        // 1. orderItemId 및 Seller로 OrderItem 조회
        OrderItem orderItem = orderItemRepository.findByIdAndSellerId(orderItemId, seller.getId())
                .orElseThrow(() -> new BaseException(ErrorCode.ORDER_ITEM_NOT_FOUND));

        // 2. transit() 상태로 변경
        orderItem.transit();

        // 3. OrderItem 객체 저장
        orderItemRepository.save(orderItem);
    }

    @Override
    public void arrivedOrder(Long orderItemId, Seller seller) {
        // 1. orderItemId 및 Seller로 OrderItem 조회
        OrderItem orderItem = orderItemRepository.findByIdAndSellerId(orderItemId, seller.getId())
                .orElseThrow(() -> new BaseException(ErrorCode.ORDER_ITEM_NOT_FOUND));

        // 2. arrived() 상태로 변경
        orderItem.arrived();

        // 3. OrderItem 객체 저장
        orderItemRepository.save(orderItem);
    }

    @Override
    public void confirmedPurchaseOrder(String merchantUid, Long orderItemId, Customer customer) {
        // 0. customer + merchantUid로 order 조회
        Order order = orderRepository.findByMerchantUidAndCustomer(merchantUid, customer)
                .orElseThrow(() -> new BaseException(ErrorCode.ORDER_NOT_FOUND));

        // 1. orderItemId 및 현재 Customer의 OrderId로 OrderItem 조회
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(orderItemId, order.getId())
                .orElseThrow(() -> new BaseException(ErrorCode.ORDER_ITEM_NOT_FOUND));

        // 3. PURCHASE_CONFIRM() 상태로 변경
        orderItem.confirmedPurchase();

        // 4. OrderItem 객체 저장
        orderItemRepository.save(orderItem);
    }

    @Override
    public Order getOrder(String merchantUid, Customer customer) {
        return orderRepository.findByMerchantUidAndCustomer(merchantUid, customer)
                .orElseThrow(() -> new BaseException(ErrorCode.ORDER_NOT_FOUND));
    }

    private void place(Map<ProductOption, Integer> productOptionCountMap, Order order) {
        // 1. 주문상품의 허가상태 확인 및 주문상품옵션 재고 확인
        validate(productOptionCountMap);

        // 2. PRE_PAID 상태의 orderItem 생성, 상품옵션 재고량 감소 및 총주문금액 계산
        ordered(productOptionCountMap, order);
    }

    private void validate(Map<ProductOption, Integer> productOptionCountMap) {
        productOptionCountMap.forEach(ProductOption::validate);
    }

    private void ordered(Map<ProductOption, Integer> productOptionCountMap, Order order) {
        productOptionCountMap.forEach((option, count) -> {
            OrderItem orderItem = OrderItem.of(option, count); // orderItem들을 만듦.
            orderItem.prePaid(); // orderItem들을 PRE_PAID로 변경
            option.decreaseStock(count); // productoption n개 만큼 재고량 감소시킴.
            orderItem.ordered(order); // 총주문금액 계산 ('개별상품 배송비 + 개별 상품옵션 가격*수량'들의 총합) 및 order 객체에 연결
        });
        order.designateMerchantUid(); // 주문번호 생성
    }

}
