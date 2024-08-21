package org.boot.growup.source.order.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.Regex;

import java.util.List;

@Data
@Builder
public class ProcessOrderRequestDTO {
    private String message;

    @NotBlank(message = "상품 수령자 이름을 명시해야합니다.")
    @Pattern(regexp = Regex.NAME, message = "이름은 한글과 영문만 포함하며, 최대 10글자입니다")
    private String receiverName;

    @NotBlank(message = "상품 배송 주소를 명시해야합니다.")
    private String receiverAddress;

    @NotBlank(message = "상품 수령자의 전화번호를 명시해야합니다.")
    @Pattern(regexp = Regex.PHONE_NUMBER, message = "전화번호는 000-0000-0000 형식이어야 합니다")
    private String receiverPhone;

    @NotBlank(message = "우편 번호를 명시해야합니다.")
    @Pattern(regexp = Regex.POST_CODE, message = "우편번호는 5자리 숫자여야 합니다")
    private String receiverPostCode;

    @NotEmpty(message = "적어도 1개 이상의 상품을 골라야합니다.")
    private List<OrderItemDTO> orderItemDTOs;

    @Data
    @Builder
    public static class OrderItemDTO {
        @NotNull(message = "구매할 상품의 수량을 입력해야합니다.")
        @Min(value = 1, message = "최소 1개 이상의 상품을 선택해야합니다.")
        private int count;

        @NotNull(message = "구매할 상품ID을 입력해야합니다.")
        private Long productId;

        @NotNull(message = "구매할 상품옵션ID를 입력해야합니다.")
        private Long productOptionId;
    }
}