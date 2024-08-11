package org.boot.growup.common.oauth2.google.dto;

public record GoogleAccountResponseDTO(
    String id,
    String email,
    Boolean verifiedEmail,
    String name,
    String givenName,
    String familyName,
    String picture
) {}
