package org.boot.growup.common.jwt;

import lombok.Builder;

@Builder
public record TokenDto (
    String grantType,
    String accessToken,
    String refreshToken
) {}
