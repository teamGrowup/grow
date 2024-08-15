package org.boot.growup.source.seller.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDTO {
    private String result; // 결과 메시지
    private Long productId; // 등록된 상품 ID 추가
}