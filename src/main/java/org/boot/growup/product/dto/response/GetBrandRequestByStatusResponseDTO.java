package org.boot.growup.product.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.product.persist.entity.Brand;

@Data
@Builder
public class GetBrandRequestByStatusResponseDTO {
    private Long brandId;
    private String brandName;
    private AuthorityStatus authorityStatus;

    public static GetBrandRequestByStatusResponseDTO from(Brand brand) {
        return GetBrandRequestByStatusResponseDTO.builder()
                .brandId(brand.getId())
                .brandName(brand.getName())
                .authorityStatus(brand.getAuthorityStatus())
                .build();
    }
}
