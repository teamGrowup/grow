package org.boot.growup.source.customer.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OauthSignInRequestDTO {
    @NotBlank(message = "인가코드는 필수 입력 값입니다.")
    private String authCode;
}
