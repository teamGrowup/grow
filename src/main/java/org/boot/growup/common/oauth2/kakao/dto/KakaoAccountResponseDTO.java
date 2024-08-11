package org.boot.growup.common.oauth2.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record KakaoAccountResponseDTO(
        Long id,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount,
        Map<String, String> properties
) {}


