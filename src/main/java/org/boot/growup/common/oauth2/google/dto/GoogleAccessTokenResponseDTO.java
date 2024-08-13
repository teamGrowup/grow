package org.boot.growup.common.oauth2.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleAccessTokenResponseDTO {
    @JsonProperty(value = "access_token")
    private final String accessToken;

    @JsonProperty(value = "expires_in")
    private final String expiresIn;

    private final String scope;

    @JsonProperty(value = "token_type")
    private final String tokenType;

    @JsonProperty(value = "id_token")
    private final String idToken;
}
