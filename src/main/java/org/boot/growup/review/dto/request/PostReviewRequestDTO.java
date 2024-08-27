package org.boot.growup.review.dto.request;

import lombok.Data;
import lombok.Builder;

import jakarta.validation.constraints.*;

@Data
@Builder
public class PostReviewRequestDTO {
    @NotBlank
    private String author;

    @NotBlank
    private String content;

    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "5.0", inclusive = true)
    private Double rating; // 0.0 < rating <= 5.0

    private Long orderItemId;  // 주문 항목 ID
}
