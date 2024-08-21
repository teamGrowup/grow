package org.boot.growup.product.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.constant.Section;
import org.boot.growup.product.persist.entity.ProductImage;

import java.util.List;

@Data
@Builder
public class GetSellerProductResponseDTO {
    private String name;
    private String description;
    private double price;
    private List<ProductImageDTO> productImages;

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
}
