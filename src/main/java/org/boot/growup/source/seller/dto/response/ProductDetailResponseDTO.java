package org.boot.growup.source.seller.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.common.enumerate.Section;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.ProductImage;
import org.boot.growup.source.seller.persist.entity.ProductOption;

import java.util.List;

@Data
@Builder
public class ProductDetailResponseDTO {
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

    // public 생성자
    public ProductDetailResponseDTO(Long id, String name, String description, double averageRating, int likeCount,
                                    AuthorityStatus authorityStatus, Long subCategoryId, Long mainCategoryId,
                                    List<ProductImageDTO> productImages,List<ProductOptionDTO> productOptions) {
        this.productId = id;
        this.name = name;
        this.description = description;
        this.averageRating = averageRating;
        this.likeCount = likeCount;
        this.authorityStatus = authorityStatus;
        this.subCategoryId = subCategoryId;
        this.mainCategoryId = mainCategoryId;
        this.productImages = productImages;
        this.productOptions = productOptions;

    }

    // from 메서드 추가
    public static ProductDetailResponseDTO from(Product product) {
        return ProductDetailResponseDTO.builder()
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
