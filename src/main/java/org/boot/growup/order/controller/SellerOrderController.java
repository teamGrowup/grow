package org.boot.growup.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.boot.growup.order.application.OrderApplication;
import org.boot.growup.order.dto.request.PatchShipmentRequestDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sellers/orders")
@RequiredArgsConstructor
public class SellerOrderController {
    private final OrderApplication orderApplication;

    /**
     * [PATCH]
     * 주문항목 PAID -> PRE-SHIPPED로 상태 변경
     * @header SellerAccesstoken
     * @param orderItemId 주문항목ID
     */
    @PatchMapping("/preshipped/{orderItemId}")
    public void patchPreShipped(@PathVariable Long orderItemId) {
        orderApplication.patchPreShipped(orderItemId);
    }

    /**
     * [PATCH]
     * 주문항목 PAID, PRE-SHIPPED -> SHIPPED로 상태 변경
     * @header SellerAccesstoken
     * @param orderItemId 주문항목ID
     */
    @PatchMapping("/shipped/{orderItemId}")
    public void patchShipped(@PathVariable Long orderItemId) {
        orderApplication.patchShipped(orderItemId);
    }

    /**
     * [PATCH]
     * 주문항목 SHIPPED -> PENDING_SHIPMENT로 상태 변경
     * @header SellerAccesstoken
     * @body PatchShipmentRequestDTO 운송자번호 및 택배사 정보
     * @param orderItemId 주문항목ID
     */
    @PatchMapping("/shipment/{orderItemId}")
    public void patchShipment(@PathVariable Long orderItemId, @Valid @RequestBody PatchShipmentRequestDTO patchShipmentRequestDTO) {
        orderApplication.patchShipment(orderItemId, patchShipmentRequestDTO);
    }

    /**
     * [PATCH]
     * 주문항목 PENDING_SHIPMENT -> IN_TRANSIT로 상태 변경
     * @header SellerAccesstoken
     * @param orderItemId 주문항목ID
     */
    @PatchMapping("/transit/{orderItemId}")
    public void patchTransit(@PathVariable Long orderItemId) {
        orderApplication.patchTransit(orderItemId);
    }

    /**
     * [PATCH]
     * 주문항목 IN_TRANSIT -> ARRIVED로 상태 변경
     * @header SellerAccesstoken
     * @param orderItemId 주문항목ID
     */
    @PatchMapping("/arrived/{orderItemId}")
    public void patchArrived(@PathVariable Long orderItemId) {
        orderApplication.patchArrived(orderItemId);
    }

}
