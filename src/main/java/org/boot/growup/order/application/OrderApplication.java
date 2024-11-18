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
import org.boot.growup.order.dto.*;
import org.boot.growup.order.dto.request.PatchShipmentRequestDTO;
import org.boot.growup.order.dto.request.PortOnePaymentCancellationRequestDTO;
import org.boot.growup.order.dto.request.ProcessNormalOrderRequestDTO;
import org.boot.growup.order.dto.response.GetOrderHistoryResponseDTO;
import org.boot.growup.order.dto.response.GetOrderResponseDTO;
import org.boot.growup.order.dto.response.ProcessNormalOrderResponseDTO;
import org.boot.growup.order.persist.entity.Order;
import org.boot.growup.order.service.OrderService;
import org.boot.growup.product.persist.entity.ProductOption;
import org.boot.growup.product.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
    public ProcessNormalOrderResponseDTO processNormalOrder(ProcessNormalOrderRequestDTO processNormalOrderRequestDTO) {
        // TODO : 0. 현재 요청자 계정 조회. & customer가 구매할 권리가 있는지 확인해야함.
        Customer customer = customerService.getCurrentCustomer();

        OrderDTO orderDTO = processNormalOrderRequestDTO.getOrderDTO();
        List<OrderItemDTO> orderItemDTOs = processNormalOrderRequestDTO.getOrderItemDTOs();

        // 1. 상품옵션, 수량 MAP 생성
        Map<ProductOption, Integer> productOptionCountMap = productService.getProductOptionCountMap(orderItemDTOs);

        // 2. Order 객체 생성
        Order order = orderService.processNormalOrder(orderDTO, productOptionCountMap, customer);

        return ProcessNormalOrderResponseDTO.of(order, customer);
    }

    public void completeNormalOrder(String merchantUid) {
        Customer customer = customerService.getCurrentCustomer();

        // 포트원에 해당 주문번호가 결제 성공했는지 및 실제 결제 금액과 일치하는지 확인해야함.
        PortOnePaymentDTO paymentResult = portOneFeignClient.getPaymentByPaymentId(merchantUid, storeId, secretkey);

        // 결제 성공 여부 확인
        if (!Objects.equals(paymentResult.getStatus(), "PAID")){
            throw new BaseException(ErrorCode.PAY_NOT_SUCCESS);
        }

        // 실제 결제 금액 일치하는지 확인
        try {
            // payed 상태로 변경
            orderService.completeOrder(merchantUid, customer, paymentResult.getAmount().getTotal());
        }catch (BaseException e) {
            if(e.getErrorCode().equals(ErrorCode.PAY_PRICE_DIFFER_ORDER_PRICE)){
                // 결제 취소 API 호출
                PortOnePaymentCancellationDTO res =
                        portOneFeignClient.cancelPaymentByPaymentId(
                                merchantUid,
                                secretkey,
                                PortOnePaymentCancellationRequestDTO.builder()
                                        .storeId(storeId)
                                        .reason("주문금액과 결제금액이 다름.")
                                        .build()
                        );

                log.info("결제 취소 res -> {}", res.toString());
            }
            throw e;
        }
    }

    public void rejectNormalOrder(String merchantUid) {
        Customer customer = customerService.getCurrentCustomer();

        // 포트원에 해당 주문번호가 결제 성공했는지 및 실제 결제 금액과 일치하는지 확인해야함.
        PortOnePaymentDTO paymentResult = portOneFeignClient.getPaymentByPaymentId(merchantUid, storeId, secretkey);

        if (Objects.equals(paymentResult.getStatus(), "PAID")){
            throw new BaseException(ErrorCode.PAY_ALREADY_SUCCESS);
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

    public Page<GetOrderHistoryResponseDTO> getOrderHistory(int pageNo) {
        Customer customer = customerService.getCurrentCustomer();

        Page<Order> orders = orderService.getOrders(customer, pageNo);

        return GetOrderHistoryResponseDTO.pageFrom(orders);
    }

    @Transactional
    public void cancelOrderItem(String merchantUid, Long orderItemId) {
        Customer customer = customerService.getCurrentCustomer();

        // 1. 특정 주문항목 가져옴. & orderItem이 PAID 혹은 PRE_SHIPPED 상태인지 확인 & 취소 금액 확인 : (주문항목가격*수량)+배달비
        OrderItemCancelDTO orderItemCancelDTO = orderService.getOrderItemCancelDTO(merchantUid, customer, orderItemId);

        // 2. PortOne에 해당 주문항목가격만큼 환불을 요청함.
        try {
            portOneFeignClient.cancelPaymentByPaymentId(
                    merchantUid,
                    secretkey,
                    PortOnePaymentCancellationRequestDTO.builder()
                            .storeId(storeId)
                            .reason("주문 취소")
                            .amount(orderItemCancelDTO.getCancelPrice())
                            .currentCancellableAmount(orderItemCancelDTO.getCurrentCancellableAmount())
                            .build()
            );
        }catch (Exception e){
            // 3-1. 실패 시 주문 취소 실패 응답.
            log.info(e.getMessage());
            throw new BaseException(ErrorCode.CANCEL_ORDER_ITEM_FAIL);
        }

        // 4. 해당 주문항목을 PAID->CANCELED 상태로 변경하고 주문 취소 객체를 생성하여 저장
        orderService.cancelOrderItem(orderItemCancelDTO);
    }
}
