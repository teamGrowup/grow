package org.boot.growup.auth.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetIsValidEmailResponseDTO {
    private boolean isValidEmail;
}
