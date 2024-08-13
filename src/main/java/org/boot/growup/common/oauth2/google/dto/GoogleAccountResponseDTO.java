package org.boot.growup.common.oauth2.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleAccountResponseDTO {
    private final String id;

    private final String email;

    @JsonProperty("verified_email")
    private final Boolean verifiedEmail;

    private final String name;

    @JsonProperty("given_name")
    private final String givenName;

    @JsonProperty("family_name")
    private final String familyName;

    private final String picture;
}

