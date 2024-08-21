package org.boot.growup.source.customer.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostAuthCodeRequestDTO {
    private String authCode;
    private String phoneNumber;
}
