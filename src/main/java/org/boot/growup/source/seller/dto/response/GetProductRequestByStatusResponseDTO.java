package org.boot.growup.source.seller.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.persist.entity.Product;

@Data
@Builder
public class GetProductRequestByStatusResponseDTO {
    private Long productId;
    private String productName;
    private AuthorityStatus authorityStatus;

    public static GetProductRequestByStatusResponseDTO from(Product product) {
        return GetProductRequestByStatusResponseDTO.builder()
                .productId(product.getId())
                .productName(product.getName())
                .authorityStatus(product.getAuthorityStatus())
                .build();
    }
}
