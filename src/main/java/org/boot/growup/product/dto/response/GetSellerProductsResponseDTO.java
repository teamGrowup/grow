package org.boot.growup.product.dto.response;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetSellerProductsResponseDTO {
    private List<GetSellerProductResponseDTO> products; // 여러 상품 리스트
}