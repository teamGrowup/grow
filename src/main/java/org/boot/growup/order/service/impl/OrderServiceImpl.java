package org.boot.growup.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.constant.OrderStatus;
import org.boot.growup.common.constant.PayMethod;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.order.dto.OrderDTO;
import org.boot.growup.order.dto.OrderItemCancelDTO;
import org.boot.growup.order.dto.request.PatchShipmentRequestDTO;
import org.boot.growup.order.persist.entity.Delivery;
import org.boot.growup.order.persist.entity.Order;
import org.boot.growup.order.persist.entity.OrderCancel;
import org.boot.growup.order.persist.entity.OrderItem;
import org.boot.growup.order.persist.repository.OrderCancelRepository;
import org.boot.growup.order.persist.repository.OrderItemRepository;
import org.boot.growup.order.persist.repository.OrderRepository;
import org.boot.growup.order.service.OrderService;
import org.boot.growup.product.persist.entity.ProductOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderCancelRepository orderCancelRepository;

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
    public void completeOrder(String merchantUid, Customer customer, int totalPrice) {
        // 1. 주문번호 + 현재 사용자 정보 토대로 Order 객체 조회.
        Order order = orderRepository.findByMerchantUidAndCustomer(merchantUid, customer)
                .orElseThrow(() -> new BaseException(ErrorCode.ORDER_NOT_FOUND));

        // 2. 포트원에서 가져온 실제 결제 금액과, Order 객체의 결제 금액을 비교함.
        if(order.getTotalPrice() != totalPrice){
            throw new BaseException(ErrorCode.PAY_PRICE_DIFFER_ORDER_PRICE);
        }

        // 3. 해당 Order 객체 내 OrderItem들을 모두 Payed 상태로 변경 및 OrderItem당 수수료 및 판매정산금 계산
        order.getOrderItems().forEach(OrderItem::payed);

        // 4. Order 객체 저장
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

    @Override
    public Page<Order> getOrders(Customer customer, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10);

        return orderRepository.findByCustomer(customer, pageable);
    }

    @Override
    public OrderItemCancelDTO getOrderItemCancelDTO(String merchantUid, Customer customer, Long orderItemId) {

        // 1. Fetch Join으로 OrderItem들까지 한번에 가져옴.
        Order order = orderRepository.findByMerchantUidAndCustomerWithOrderItems(merchantUid, customer)
                            .orElseThrow(() -> new BaseException(ErrorCode.ORDER_NOT_FOUND));

        // 2. 특정 orderItem 찾기(단, PAID 혹은 PRE_SHIPPED 상태여야함)
        OrderItem orderItem = order.getOrderItems().stream()
                                    .filter(oi -> oi.getId().equals(orderItemId) && oi.checkCancellable())
                                    .findFirst()
                                    .orElseThrow(() -> new BaseException(ErrorCode.ORDER_ITEM_NOT_FOUND));

        // 3. 해당 orderItem의 cancelPrice 계산하기
        int cancelPrice = orderItem.getCount() * orderItem.getProductOptionPrice() + orderItem.getDeliveryFee();

        // 4. 해당 Order의 currentCancellableAmount 계산하기 (orderstatus가 'pre_paid', 'rejected', 'canceled'가 아닌 것들의 합)
        int currentCancellableAmount = order.getOrderItems().stream()
                .filter(oi -> ((oi.getOrderStatus() != OrderStatus.PRE_PAID) &&
                        (oi.getOrderStatus() != OrderStatus.CANCELED) &&
                        (oi.getOrderStatus() != OrderStatus.REJECTED)))
                .mapToInt(oi -> oi.getProductOptionPrice() * oi.getCount() + oi.getDeliveryFee())
                .sum();
        log.info("cancelPrice -> {}, currentCancellableAmount -> {}", cancelPrice, currentCancellableAmount);
        return OrderItemCancelDTO.builder()
                .orderItem(orderItem)
                .cancelPrice(cancelPrice)
                .currentCancellableAmount(currentCancellableAmount)
                .build();
    }

    @Override
    public void cancelOrderItem(OrderItemCancelDTO orderItemCancelDTO) {
        // 1. 해당 주문항목을 PAID->CANCELED 상태로 변경 및 상품개수 원복
        OrderItem orderItem = orderItemCancelDTO.getOrderItem();
        orderItem.cancel();

        // 2. 주문 취소 객체를 생성하여 저장
        OrderCancel orderCancel = OrderCancel.builder()
                .price(orderItemCancelDTO.getCancelPrice())
                .orderItem(orderItem)
                .build();

        orderCancelRepository.save(orderCancel);
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
        order.designateName(); // 주문명 생성
        order.designateMerchantUid(); // 주문번호 생성
    }

}
