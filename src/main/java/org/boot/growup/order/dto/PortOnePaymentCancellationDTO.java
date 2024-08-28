package org.boot.growup.order.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PortOnePaymentCancellationDTO {
    private String status;
    private String id;
    private int totalAmount;
    private int taxFreeAmount;
    private int vatAmount;
    private String reason; // 취소 사유
    private String receiptUrl; // 취소 영수증 URL
}
