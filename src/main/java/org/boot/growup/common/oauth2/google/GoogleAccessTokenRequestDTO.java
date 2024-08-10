package org.boot.growup.common.oauth2.google;

import lombok.Builder;

@Builder
public record GoogleAccessTokenRequestDTO(
    String decodedAuthCode,
    String clientId,
    String clientSecret,
    String redirectUri,
    String authorizationCode
) {}
