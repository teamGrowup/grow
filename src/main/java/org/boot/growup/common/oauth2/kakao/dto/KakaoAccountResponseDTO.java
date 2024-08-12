package org.boot.growup.common.oauth2.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
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
