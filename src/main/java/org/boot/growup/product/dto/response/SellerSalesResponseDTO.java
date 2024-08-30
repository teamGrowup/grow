package org.boot.growup.product.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SellerSalesResponseDTO {
    private Long sellerId;
    private String sellerName;
    private List<ProductSalesResponseDTO> products;
    private int totalQuantity;
    private int totalRevenue;

    public SellerSalesResponseDTO(Long sellerId, String sellerName, List<ProductSalesResponseDTO> products, int totalQuantity, int totalRevenue) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.products = products;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }
}



