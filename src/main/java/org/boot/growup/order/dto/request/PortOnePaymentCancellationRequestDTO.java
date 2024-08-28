package org.boot.growup.order.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PortOnePaymentCancellationRequestDTO {
    private String storeId;
    private String reason;
}
