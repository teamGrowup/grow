package org.boot.growup.review.dto.response;

import lombok.Data;
import lombok.Builder;
import org.boot.growup.review.persist.entity.ReviewImage;

import java.util.List;

@Data
@Builder
public class GetReviewResponseDTO {
    private Long reviewId;
    private String author;
    private String content;
    private Double rating;
    private int likeCount;
    private List<ReviewImageDTO> reviewImageDTO;  // ReviewImageDTO 리스트 추가
    private Long productId; // 추가된 필드
    private String productName; // 추가된 필드

    @Data
    @Builder
    public static class ReviewImageDTO {
        private Long reviewImageId;
        private String originalImageName;
        private String path;

        // Entity를 DTO로 변환하는 메서드 (선택적)
        public static ReviewImageDTO from(ReviewImage reviewImage) {
            return ReviewImageDTO.builder()
                    .reviewImageId(reviewImage.getId())
                    .originalImageName(reviewImage.getOriginalImageName())
                    .path(reviewImage.getPath())
                    .build();
        }
    }
}