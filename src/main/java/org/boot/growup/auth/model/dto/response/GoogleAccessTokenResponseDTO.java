package org.boot.growup.auth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
