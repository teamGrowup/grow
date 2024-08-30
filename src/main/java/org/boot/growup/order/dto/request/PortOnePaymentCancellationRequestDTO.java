package org.boot.growup.order.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PortOnePaymentCancellationRequestDTO {
    private String storeId;
    private String reason;
    private Integer amount; // 취소 금액
    private Integer currentCancellableAmount; // 해당 주문의 취소 가능 잔액
}
