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

    // 판매자 ID 추가
    private Long sellerId; // 판매자 ID 필드 추가
    @Data
    @Builder
    public static class ProductOptionDTO {
        @NotBlank(message = "옵션 이름은 필수입니다.")
        private String optionName;
        private int optionStock;
        private int optionPrice;
    }

}
