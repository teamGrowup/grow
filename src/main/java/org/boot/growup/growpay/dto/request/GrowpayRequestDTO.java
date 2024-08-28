package org.boot.growup.growpay.dto.request;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class GrowpayRequestDTO {
    private Long growpayId; // 계좌 ID (업데이트할 계좌를 식별하기 위해 사용)
    private String accountName; // 계좌 이름
    private String accountNumber; // 계좌 번호
    private Long customerId; // 고객 ID
    private int amount; // 초기 잔액 또는 업데이트할 잔액
}