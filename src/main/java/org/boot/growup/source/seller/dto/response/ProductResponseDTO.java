package org.boot.growup.source.seller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private String result; // 결과 메시지
    private Long productId; // 등록된 상품 ID 추가
}