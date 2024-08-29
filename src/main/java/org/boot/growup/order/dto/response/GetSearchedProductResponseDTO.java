package org.boot.growup.order.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.product.persist.entity.Product;

@Data
@Builder
public class GetSearchedProductResponseDTO {
    private Long id;
    private String productName;
    private Double averageRating;
    private int likeCount;

    public static GetSearchedProductResponseDTO from(Product product) {
        return GetSearchedProductResponseDTO.builder()
                .id(product.getId())
                .productName(product.getName())
                .averageRating(product.getAverageRating())
                .likeCount(product.getLikeCount())
                .build();
    }
}
