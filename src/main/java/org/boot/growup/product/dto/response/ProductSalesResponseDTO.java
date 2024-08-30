package org.boot.growup.product.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSalesResponseDTO {
    private Long productId;
    private String productName;
    private int quantitySold;
    private int totalRevenue;

    public ProductSalesResponseDTO(Long productId, String productName, int quantitySold, int totalRevenue) {
        this.productId = productId;
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.totalRevenue = totalRevenue;
    }
}