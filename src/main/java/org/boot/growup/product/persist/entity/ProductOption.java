package org.boot.growup.product.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.model.BaseException;

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


    public void decreaseStock(int count){
        if (optionStock < count) {
            throw new BaseException(ErrorCode.PRODUCT_OPTION_NOT_ENOUGH_STOCK);
        }
        this.optionStock -= count;
    }

    public void increaseStock(int count){
        this.optionStock += count;
    }

    public void validate(Integer count) {
        enoughStock(count);
        if (!this.product.getAuthorityStatus().equals(AuthorityStatus.APPROVED)) {
            throw new BaseException(ErrorCode.PRDOUCT_NOT_APPROVED);
        }
    }

    private void enoughStock(int count){
        if (optionStock < count) {
            throw new BaseException(ErrorCode.PRODUCT_OPTION_NOT_ENOUGH_STOCK);
        }
    }
}
