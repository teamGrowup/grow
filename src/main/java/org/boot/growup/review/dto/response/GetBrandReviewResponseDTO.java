package org.boot.growup.review.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetBrandReviewResponseDTO {
    private Long sellerId;
    private List<GetReviewResponseDTO> reviewList;

    public GetBrandReviewResponseDTO(Long sellerId, List<GetReviewResponseDTO> reviewList) {
        this.sellerId = sellerId;
        this.reviewList = reviewList;
    }
}