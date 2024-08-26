package org.boot.growup.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDTO {
    @NotNull(message = "구매할 상품의 수량을 입력해야합니다.")
    @Min(value = 1, message = "최소 1개 이상의 상품을 선택해야합니다.")
    private int count;

    @NotNull(message = "구매할 상품옵션ID를 입력해야합니다.")
    private Long productOptionId;
}
