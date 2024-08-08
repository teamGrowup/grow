package org.boot.growup.source.seller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandPostDTO {
    private String name;
    private String description;
}
