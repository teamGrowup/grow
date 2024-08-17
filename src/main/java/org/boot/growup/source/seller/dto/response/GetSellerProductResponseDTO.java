package org.boot.growup.source.seller.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.enumerate.Section;
import org.boot.growup.source.seller.persist.entity.ProductImage;

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
