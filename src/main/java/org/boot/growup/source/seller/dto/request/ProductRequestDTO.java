package org.boot.growup.source.seller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    @NotBlank(message = "상품 이름은 필수입니다.")
    private String productName;

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String productDescription;

    private Long subCategoryId;

    private List<ProductOptionDto> productOptions;

    // 상품 이미지 리스트를 ProductImageDto로 변경
    private List<ProductImageDto> productImages; // ProductImageDto 리스트로 변경

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductOptionDto {
        @NotBlank(message = "옵션 이름은 필수입니다.")
        private String productOptionName;
        private int productOptionStock;
        private int productOptionPrice;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductImageDto {
        private String originalImageName;
        private String path;
    }
}
