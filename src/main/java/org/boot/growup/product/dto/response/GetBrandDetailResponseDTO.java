package org.boot.growup.product.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.product.persist.entity.Brand;
import org.boot.growup.product.persist.entity.BrandImage;

import java.util.List;

@Data
@Builder
public class GetBrandDetailResponseDTO {
    private String name;
    private String description;
    private int likeCount;
    private List<BrandDetailDTO> images;

    @Data
    @Builder
    public static class BrandDetailDTO {
        private String originalImageName;
        private String path;

        public static BrandDetailDTO from(BrandImage m) {
            return BrandDetailDTO.builder()
                    .originalImageName(m.getOriginalImageName())
                    .path(m.getPath())
                    .build();
        }
    }

    public static GetBrandDetailResponseDTO of(Brand brand, List<BrandImage> brandImages) {
        return GetBrandDetailResponseDTO.builder()
                .name(brand.getName())
                .description(brand.getDescription())
                .likeCount(brand.getLikeCount())
                .images(brandImages.stream().map(
                        BrandDetailDTO::from
                ).toList())
                .build();
    }
}
