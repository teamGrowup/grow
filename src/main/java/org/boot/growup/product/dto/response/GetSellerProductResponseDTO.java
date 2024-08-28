package org.boot.growup.product.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.constant.Section;
import org.boot.growup.product.persist.entity.ProductImage;
import org.boot.growup.product.persist.entity.ProductOption;

import java.util.List;

@Data
@Builder
public class GetSellerProductResponseDTO {
    private String name;
    private String description;
    private List<ProductImageDTO> productImages;
    private List<GetProductDetailResponseDTO.ProductOptionDTO> productOption;

    @Data
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

    @Data
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
