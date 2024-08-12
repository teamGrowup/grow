package org.boot.growup.source.customer.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class GoogleSignInRequestDTO {
    @NotBlank(message = "인가코드는 필수 입력 값입니다.")
    private String authCode;
}
