package org.boot.growup.common.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {
    private final String grantType;
    private final String accessToken;
    private final String refreshToken;

    public static TokenDTO of(String accessToken, String refreshToken) {
        return TokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

