package org.boot.growup.order.service;

import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.order.dto.OrderDTO;
import org.boot.growup.order.persist.entity.Order;
import org.boot.growup.product.persist.entity.ProductOption;

import java.util.Map;

public interface OrderService {
    /*
    일반 결제 시도 시 merchantUid를 생성하고 PRE_PAYED 상태의 orderItem을 가진 Order 객체를 반환.
     */
    Order processNormalOrder(OrderDTO orderDTO, Map<ProductOption, Integer> productOptionCountMap, Customer customer);

    /*
    주문번호 + 구매자 정보를 통해 Order를 찾고, OrderItem들을 PAYED 상태로 변경함.
     */
    void completeOrder(String merchantUid, Customer customer);

    /*
    주문번호 + 구매자 정보를 통해 Order를 찾고, OrderItem들을 REJECTED 상태로 변경함.
     */
    void rejectNormalOrder(String merchantUid, Customer customer);
}
