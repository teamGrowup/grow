package org.boot.growup.product.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.common.constant.Section;
import org.boot.growup.product.persist.entity.Product;
import org.boot.growup.product.persist.entity.ProductImage;
import org.boot.growup.product.persist.entity.ProductOption;

import java.util.List;

@Data
@Builder
public class GetProductDetailResponseDTO {
    private Long productId;
    private String name;
    private String description;
    private double averageRating;
    private int likeCount;
    private AuthorityStatus authorityStatus; // 상품의 허가 상태
    private Long subCategoryId;
    private Long mainCategoryId;
    private List<ProductImageDTO> productImages;
    private List<ProductOptionDTO> productOptions;

    public static GetProductDetailResponseDTO from(Product product) {
        return GetProductDetailResponseDTO.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .averageRating(product.getAverageRating())
                .likeCount(product.getLikeCount())
                .authorityStatus(product.getAuthorityStatus())
                .subCategoryId(product.getSubCategory().getId())
                .mainCategoryId(product.getSubCategory().getMainCategory().getId())
                .productImages(product.getProductImages().stream()
                        .map(ProductImageDTO::from)
                        .toList())
                .productOptions(product.getProductOptions().stream()
                        .map(ProductOptionDTO::from)
                        .toList())
                .build();
    }

    @Getter
    @Builder
    public static class ProductImageDTO {
        private String path;
        private String originalImageName;
        private Section section;
        public static ProductImageDTO from(ProductImage productImage) {
            return ProductImageDTO.builder()
                    .path(productImage.getPath())
                    .originalImageName(productImage.getOriginalImageName())
                    .section(productImage.getSection())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ProductOptionDTO {
        private String optionName;
        private int optionStock;
        private int optionPrice;

        public static ProductOptionDTO from(ProductOption productOption) {
            return ProductOptionDTO.builder()
                    .optionName(productOption.getOptionName())
                    .optionStock(productOption.getOptionStock())
                    .optionPrice(productOption.getOptionPrice())
                    .build();
        }
    }
}
