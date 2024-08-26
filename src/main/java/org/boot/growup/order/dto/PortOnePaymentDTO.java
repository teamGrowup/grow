package org.boot.growup.order.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PortOnePaymentDTO {
    private String status;
    private String id;
    private String transactionId;
    private String merchantId;
    private String storeId;
    private String portOneVersion;
    private String requestedAt; // 결제 요청 시점
    private String updatedAt; // 업데이트 시점
    private String statusChangeAt; // 상태 업데이트 시점
    private String orderName;
    private PayAmount amount;
    private String currency;
    private PaymentCancellation[] Cancellations;
    private String cancelledAt; // 결제 취소 시점

    public static class PayAmount{
        private int total; // 총 결제금액
        private int taxFree; // 면세액
        private int discount; // 할인 금액
        private int paid; // 실제 결제 금액
        private int cancelled; // 취소금액
        private int cancelledTaxFree; // 취소금액 중 면세액
    }

    public static class PaymentCancellation{
        private String status; // 결제 취소 상태
        private String id;
        private int totalAmount; // 취소 총 금액
        private int taxFreeAmount; // 취소 금액 중 면세 금액
        private int vatAmount; // 취소 금액 중 부가세액
        private String reason; // 취소 사유
        private String requestedAt; // 취소 요청 시점
    }
}
