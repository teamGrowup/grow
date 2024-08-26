package org.boot.growup.order.controller;

import org.boot.growup.order.dto.PortOnePaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

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
}