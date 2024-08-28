package org.boot.growup.order.client;

import org.boot.growup.order.dto.PortOnePaymentCancellationDTO;
import org.boot.growup.order.dto.PortOnePaymentDTO;
import org.boot.growup.order.dto.request.PortOnePaymentCancellationRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "portOneFeignClient", url = "https://api.portone.io")
public interface PortOneFeignClient {
    /*
    PortOne으로부터 주문번호를 통해 결제 성공 여부를 확인함.
     */
    @GetMapping(value = "/payments/{paymentId}")
    PortOnePaymentDTO getPaymentByPaymentId(
            @PathVariable("paymentId") String paymentId,
            @RequestParam("storeId") String storeId,
            @RequestHeader("Authorization") String authorizationHeader
    );

    /*
    PortOne에 결제 취소를 요청함.
     */
    @PostMapping(value = "/payments/{paymentId}/cancel")
    PortOnePaymentCancellationDTO cancelPaymentByPaymentId(
            @PathVariable("paymentId") String paymentId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody PortOnePaymentCancellationRequestDTO dto
    );
}