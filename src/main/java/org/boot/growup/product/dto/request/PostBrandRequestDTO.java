package org.boot.growup.product.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostBrandRequestDTO {
    private String name;
    private String description;
}
