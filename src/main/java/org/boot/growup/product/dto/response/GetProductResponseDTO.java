package org.boot.growup.product.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.product.persist.entity.Product;

@Data
@Builder
public class GetProductResponseDTO {
    private Long id;
    private String name;
    private Double averageRating;
    private int likeCount;
    private int reviewCount;
    private String subCategoryName;
    private String mainCategoryName;
    private String brandName;
    private String imagePath;

    public static GetProductResponseDTO from(Product product) {
        return GetProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .averageRating(product.getAverageRating())
                .likeCount(product.getLikeCount())
                .reviewCount(product.getReviewCount())
                .subCategoryName(product.getSubCategory().getName())
                .mainCategoryName(product.getSubCategory().getMainCategory().getName())
                .brandName(product.getBrand().getName())
                .imagePath(product.getProductImages().isEmpty() ? null : product.getProductImages().get(0).getPath())
                .build();
    }
}
