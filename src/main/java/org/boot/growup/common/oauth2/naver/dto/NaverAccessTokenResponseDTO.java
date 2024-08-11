package org.boot.growup.common.oauth2.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverAccessTokenResponseDTO(
    @JsonProperty("access_token")
    String accessToken,
    @JsonProperty("refresh_token")
    String refreshToken,
    @JsonProperty("token_type")
    String tokenType,
    @JsonProperty("expires_in")
    int expiresIn,
    String error,
    @JsonProperty("error_description")
    String errorDescription

) {}
