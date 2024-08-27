package org.boot.growup.order.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.auth.service.CustomerService;
import org.boot.growup.auth.service.SellerService;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.order.client.PortOneFeignClient;
import org.boot.growup.order.dto.OrderDTO;
import org.boot.growup.order.dto.OrderItemDTO;
import org.boot.growup.order.dto.request.PatchShipmentRequestDTO;
import org.boot.growup.order.dto.request.ProcessNormalOrderRequestDTO;
import org.boot.growup.order.dto.response.GetOrderResponseDTO;
import org.boot.growup.order.persist.entity.Order;
import org.boot.growup.order.service.OrderService;
import org.boot.growup.product.persist.entity.ProductOption;
import org.boot.growup.product.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderApplication {
    private final CustomerService customerService;
    private final SellerService sellerService;
    private final ProductService productService;
    private final OrderService orderService;
    private final PortOneFeignClient portOneFeignClient;

    @Value("${portone.storeId}")
    private String storeId;

    @Value("${portone.api.secret}")
    private String secretkey;

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

        if (!Objects.equals(portOneFeignClient.getPaymentByPaymentId(merchantUid, storeId, secretkey).getStatus(), "PAID")){
            // rejected 상태로 변경
            orderService.rejectNormalOrder(merchantUid, customer);

            throw new BaseException(ErrorCode.PAY_NOT_SUCCESS);
        }

        // payed 상태로 변경
        orderService.completeOrder(merchantUid, customer);
    }

    public void rejectNormalOrder(String merchantUid) {
        Customer customer = customerService.getCurrentCustomer();

        if (Objects.equals(portOneFeignClient.getPaymentByPaymentId(merchantUid, storeId, secretkey).getStatus(), "PAID")){
            // payed 상태로 변경
            orderService.completeOrder(merchantUid, customer);
            throw new BaseException(ErrorCode.PAY_NOT_SUCCESS);
        }

        // rejected 상태로 변경
        orderService.rejectNormalOrder(merchantUid, customer);
    }

    public void patchPreShipped(Long orderItemId) {
        Seller seller = sellerService.getCurrentSeller();

        orderService.preshippedOrder(orderItemId, seller);
    }


    public void patchShipped(Long orderItemId) {
        Seller seller = sellerService.getCurrentSeller();

        orderService.shippedOrder(orderItemId, seller);
    }

    public void patchShipment(Long orderItemId, PatchShipmentRequestDTO patchShipmentRequestDTO) {
        Seller seller = sellerService.getCurrentSeller();

        orderService.shipmentOrder(orderItemId, seller, patchShipmentRequestDTO);
    }

    public void patchTransit(Long orderItemId) {
        Seller seller = sellerService.getCurrentSeller();

        orderService.transitOrder(orderItemId, seller);
    }

    public void patchArrived(Long orderItemId) {
        Seller seller = sellerService.getCurrentSeller();

        orderService.arrivedOrder(orderItemId, seller);
    }

    public void patchPurchaseConfirmed(String merchantUid, Long orderItemId) {
        Customer customer = customerService.getCurrentCustomer();

        orderService.confirmedPurchaseOrder(merchantUid, orderItemId, customer);
    }

    public GetOrderResponseDTO getOrder(String merchantUid) {
        Customer customer = customerService.getCurrentCustomer();

        Order order = orderService.getOrder(merchantUid, customer);

        return GetOrderResponseDTO.from(order);
    }
}
