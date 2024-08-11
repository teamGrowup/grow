package org.boot.growup.common.oauth2.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccessTokenResponseDTO(
    @JsonProperty("token_type")
    String tokenType,
    @JsonProperty("access_token")
    String accessToken
) {}
