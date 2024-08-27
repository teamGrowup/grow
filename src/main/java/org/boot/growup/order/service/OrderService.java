package org.boot.growup.order.service;

import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.order.dto.OrderDTO;
import org.boot.growup.order.dto.request.PatchShipmentRequestDTO;
import org.boot.growup.order.persist.entity.Order;
import org.boot.growup.product.persist.entity.ProductOption;

import java.util.Map;

public interface OrderService {
    /*
    일반 결제 시도 시 merchantUid를 생성하고 PRE_PAYED 상태의 orderItem을 가진 Order 객체를 반환.
     */
    Order processNormalOrder(OrderDTO orderDTO, Map<ProductOption, Integer> productOptionCountMap, Customer customer);

    /*
    주문번호 + 구매자 정보를 통해 Order를 찾고, 실제 결제 금액과 일치하는지 확인 후, OrderItem들을 PAYED 상태로 변경 및 수수료, 정산금을 계산함.
     */
    void completeOrder(String merchantUid, Customer customer, int totalPrice);

    /*
    주문번호 + 구매자 정보를 통해 Order를 찾고, OrderItem들을 REJECTED 상태로 변경함.
     */
    void rejectNormalOrder(String merchantUid, Customer customer);

    /*
    OrderItemId 및 Seller를 통해 OrderItem을 찾고, 해당 OrderItem을 PRE_SHIPPED 상태로 변경함.
     */
    void preshippedOrder(Long orderItemId, Seller seller);

    /*
    OrderItemId 및 Seller를 통해 OrderItem을 찾고, 해당 OrderItem을 SHIPPED 상태로 변경함.
     */
    void shippedOrder(Long orderItemId, Seller seller);

    /*
    OrderItemId 및 Seller를 통해 OrderItem을 찾고, 해당 OrderItem을 Delivery 등록 후 PENDING-SHIPMENT 상태로 변경함.
     */
    void shipmentOrder(Long orderItemId, Seller seller, PatchShipmentRequestDTO patchShipmentRequestDTO);

    /*
    OrderItemId 및 Seller를 통해 OrderItem을 찾고, 해당 OrderItem을 IN-TRANSIT 상태로 변경함.
     */
    void transitOrder(Long orderItemId, Seller seller);

    /*
    OrderItemId 및 Seller를 통해 OrderItem을 찾고, 해당 OrderItem을 ARRIVED 상태로 변경함.
     */
    void arrivedOrder(Long orderItemId, Seller seller);

    /*
    OrderItemId 및 Customer를 통해 OrderItem을 찾고 해당 OrderItem을 PURCHASE_CONFIRM 상태로 변경 및 판매자 총 정산금 증액
     */
    void confirmedPurchaseOrder(String merchantUid, Long orderItemId, Customer customer);

    /*
    merchantUid와 customer 정보로 Order를 가져옴.
     */
    Order getOrder(String merchantUid, Customer customer);
}
