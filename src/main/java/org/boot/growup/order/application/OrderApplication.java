package org.boot.growup.order.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.auth.service.CustomerService;
import org.boot.growup.order.dto.OrderDTO;
import org.boot.growup.order.dto.OrderItemDTO;
import org.boot.growup.order.dto.request.ProcessNormalOrderRequestDTO;
import org.boot.growup.order.persist.entity.Order;
import org.boot.growup.order.service.OrderService;
import org.boot.growup.product.persist.entity.ProductOption;
import org.boot.growup.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderApplication {
    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderService orderService;

    @Transactional
    public String processNormalOrder(ProcessNormalOrderRequestDTO processNormalOrderRequestDTO) {
        // TODO : 0. 현재 요청자 계정 조회. & customer가 구매할 권리가 있는지 확인해야함.
        Customer customer = customerService.getCurrentCustomer();

        OrderDTO orderDTO = processNormalOrderRequestDTO.getOrderDTO();
        List<OrderItemDTO> orderItemDTOs = processNormalOrderRequestDTO.getOrderItemDTOs();

        // 1. 상품옵션, 수량 MAP 생성
        Map<ProductOption, Integer> productOptionCountMap = productService.getProductOptionCountMap(orderItemDTOs);

        // 2. Order 객체 생성
        Order order = orderService.processNormalOrder(orderDTO, productOptionCountMap, customer);

        return order.getMerchantUid();
    }

    public void completeNormalOrder(String merchantUid) {
        Customer customer = customerService.getCurrentCustomer();

        // payed 상태로 변경
        orderService.completeOrder(merchantUid, customer);
    }

    public void rejectNormalOrder(String merchantUid) {
        Customer customer = customerService.getCurrentCustomer();

        // rejected 상태로 변경
        orderService.rejectNormalOrder(merchantUid, customer);
    }
}
