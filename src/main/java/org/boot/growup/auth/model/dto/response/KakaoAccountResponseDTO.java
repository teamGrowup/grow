package org.boot.growup.auth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class KakaoAccountResponseDTO {
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    private Map<String, String> properties;

    @Data
    public static class KakaoAccount {
        private String email;
    }
}
