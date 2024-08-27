package org.boot.growup.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatchShipmentRequestDTO {
    @NotBlank(message = "운송장 번호를 입력해야합니다.")
    private String trackingNumber;

    @NotNull(message = "택배사 코드를 입력해야합니다.")
    private int carrierCode;
}
