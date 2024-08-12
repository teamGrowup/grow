package org.boot.growup.source.seller.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.boot.growup.common.enumerate.AuthorityStatus;

import java.util.List;

@Getter
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

    @Getter
    @Builder
    public static class ProductImageDTO {
        private String path;
        private String originalImageName;
        private String section;
    }

    @Getter
    @Builder
    public static class ProductOptionDTO {
        private String optionName;
        private int optionStock;
        private int optionPrice;
    }
}
