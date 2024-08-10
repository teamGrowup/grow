package org.boot.growup.source.seller.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@Table(name = "product_option")
@NoArgsConstructor
@AllArgsConstructor
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id", nullable = false)
    private Long id;

    @Column(name = "product_option_name", nullable = false, length = 100)
    private String optionName;

    @Column(name = "product_option_stock", nullable = false)
    private int optionStock;

    @Column(name = "product_option_price", nullable = false)
    private int optionPrice;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
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
