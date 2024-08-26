package org.boot.growup.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.order.application.OrderApplication;
import org.boot.growup.order.dto.request.ProcessNormalOrderRequestDTO;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/customers/orders")
@RequiredArgsConstructor
public class CustomerOrderController {
    private final OrderApplication orderApplication;

    /**
     * [POST]
     * PG사 결제 이전에, 주문 번호를 요청함.
     * @header CustomerAccesstoken
     * @body ProcessNormalOrderRequestDTO 주문 정보
     * @response merchantUid
     */
    @PostMapping("/payments/process")
    public BaseResponse<String> processNormalOrder(@Valid @RequestBody ProcessNormalOrderRequestDTO form) {
        form.getOrderItemDTOs().forEach(m -> log.info(String.valueOf(m.getProductOptionId())));
        return new BaseResponse<>(orderApplication.processNormalOrder(form));
    }

    /**
     * [PATCH]
     * PG사에 결제 성공 사실을 알림.
     * @header CustomerAccesstoken
     * @param merchantUid 주문 번호
     */
    @PatchMapping("/payments/complete/{merchantUid}")
    public void completeNormalOrder(@PathVariable String merchantUid){
        orderApplication.completeNormalOrder(merchantUid);
    }

    /**
     * [PATCH]
     * PG사에 결제 실패 사실을 알림.
     * @header CustomerAccesstoken
     * @param merchantUid 주문 번호
     */
    @PatchMapping("/payments/rejected/{merchantUid}")
    public void rejectNormalOrder(@PathVariable String merchantUid){
        orderApplication.rejectNormalOrder(merchantUid);
    }

}
