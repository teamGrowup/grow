package org.boot.growup.source.seller.persist.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "product_option")
@NoArgsConstructor
@AllArgsConstructor
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productOptionName;
    private int productOptionStock;
    private int productOptionPrice;

    // setter 메서드 추가
    @Setter
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

//    /*
//        재고 수량 업데이트
//     */
//    public void updateStock(int newStock) {
//        this.productOptionStock = newStock;
//    }
//
//    /*
//        가격 업데이트
//     */
//    public void updatePrice(int newPrice) {
//        this.productOptionPrice = newPrice;
//    }
}
