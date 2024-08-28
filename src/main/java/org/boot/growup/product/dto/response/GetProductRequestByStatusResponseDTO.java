package org.boot.growup.product.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.product.persist.entity.Product;

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
