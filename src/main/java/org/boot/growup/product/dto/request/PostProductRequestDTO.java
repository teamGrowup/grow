package org.boot.growup.product.dto.request;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Data
@Builder
public class PostProductRequestDTO {
    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String description;

    private int price;

    private int deliveryFee;

    private Long subCategoryId;

    private Long brandId;

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
