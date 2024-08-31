package org.boot.growup.order.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.product.persist.entity.Product;

@Data
@Builder
public class GetSearchedProductResponseDTO {
    private Long id;
    private String productName;
    private String brandName;
    private String mainCategoryName;
    private String subCategoryName;
    private Double averageRating;
    private int likeCount;

    public static GetSearchedProductResponseDTO from(Product product) {
        return GetSearchedProductResponseDTO.builder()
                .id(product.getId())
                .productName(product.getName())
                .brandName(product.getBrand().getName())
                .mainCategoryName(product.getSubCategory().getMainCategory().getName())
                .subCategoryName(product.getSubCategory().getName())
                .averageRating(product.getAverageRating())
                .likeCount(product.getLikeCount())
                .build();
    }
}
