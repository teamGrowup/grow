package org.boot.growup.source.seller.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.persist.entity.Brand;

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
