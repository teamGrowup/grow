package org.boot.growup.growpay.dto.response;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;
import org.boot.growup.common.constant.TransactionStatus;

@Data
@Builder
public class GrowpayHistoryResponseDTO {
    private Long id;
    private int amount;
    private LocalDateTime createdAt;
    private TransactionStatus transactionStatus;
    private Long growpayId; // 연결된 GrowPay ID
}
