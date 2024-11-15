package org.boot.growup.product.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.product.persist.entity.BrandImage;

import java.util.List;

@Data
@Builder
public class GetSellerBrandResponseDTO {
    private String name;
    private String description;
    private List<BrandImageDTO> brandImages;

    @Data
    @Builder
    public static class BrandImageDTO {
        private String path;
        private String originalImageName;

        public static BrandImageDTO from(BrandImage brandImage) {
            return BrandImageDTO.builder()
                    .path(brandImage.getPath())
                    .originalImageName(brandImage.getOriginalImageName())
                    .build();
        }
    }
}
