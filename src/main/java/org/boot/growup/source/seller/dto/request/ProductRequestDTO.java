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
    private String name;

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String description;

    private Long subCategoryId;

    private List<ProductOptionDTO> productOptions;


    // 상품 이미지 리스트를 ProductImageDto로 변경
    private List<ProductImageDTO> productImages; // ProductImageDto 리스트로 변경

    // 판매자 ID 추가
    private Long sellerId; // 판매자 ID 필드 추가
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductOptionDTO {
        @NotBlank(message = "옵션 이름은 필수입니다.")
        private String optionName;
        private int optionStock;
        private int optionPrice;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductImageDTO {
        private String originalImageName;
        private String path;

        // 섹션 필드 추가
        @NotBlank(message = "섹션은 필수입니다.") // 유효성 검사 추가
        private String section; // 섹션 필드 추가
    }
}
