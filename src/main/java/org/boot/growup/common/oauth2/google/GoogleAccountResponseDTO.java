package org.boot.growup.common.oauth2.google;

public record GoogleAccountResponseDTO(
    String id,
    String email,
    Boolean verifiedEmail,
    String name,
    String givenName,
    String familyName,
    String picture
) {}
