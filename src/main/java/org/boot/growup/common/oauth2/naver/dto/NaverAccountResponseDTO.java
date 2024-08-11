package org.boot.growup.common.oauth2.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverAccountResponseDTO(
    @JsonProperty("response")
    Response response
) {}
