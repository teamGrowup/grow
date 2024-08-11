package org.boot.growup.source.seller.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.boot.growup.source.seller.constant.AuthorityStatus;

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
