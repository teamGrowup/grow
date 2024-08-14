package org.boot.growup.source.customer.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailCheckResponseDTO {
    private String validationCode;
}
