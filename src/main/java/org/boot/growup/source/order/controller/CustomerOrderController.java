package org.boot.growup.source.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.source.order.application.OrderApplication;
import org.boot.growup.source.order.dto.request.ProcessOrderRequestDTO;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/customers/orders/")
@RequiredArgsConstructor
public class CustomerOrderController {
    private final OrderApplication orderApplication;

    // 주문번호 요청
    @PostMapping("/payments/process")
    public BaseResponse<String> processOrder(@Valid @RequestBody ProcessOrderRequestDTO form) {
        return new BaseResponse<>(orderApplication.processOrder(form));
    }

//    // pay 사실을 알리는 요청
//    @PostMapping("/payments/complete")
//    public void completeOrder(@RequestBody CompleteOrderRequestDTO form){
//        orderApplication.completeOrder(form);
//    }

}
