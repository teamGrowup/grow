package org.boot.growup.auth.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostEmailAuthCodeRequestDTO {
    private String email;
    private String authCode;
}
