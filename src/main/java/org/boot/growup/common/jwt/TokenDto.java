package org.boot.growup.common.jwt;

public record TokenDto (
    String grantType,
    String accessToken,
    String refreshToken
) {}
