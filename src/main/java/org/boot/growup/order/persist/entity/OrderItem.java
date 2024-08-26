package org.boot.growup.order.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.constant.OrderStatus;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.product.persist.entity.Product;
import org.boot.growup.product.persist.entity.ProductOption;

import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter
@Builder
@Table(name = "order_item")
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private int count;

    @Column(nullable = true)
    private LocalDateTime shipped_at;

    @Column(nullable = true)
    private LocalDateTime purchase_confirmed_at;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private int deliveryFee;

    @Column(nullable = false, length = 25)
    private String productName;

    @Column(nullable = false, length = 100)
    private String productOptionName;

    @Column(nullable = false)
    private int productOptionPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    public static OrderItem of(ProductOption option, Integer count) {
        return OrderItem.builder()
                .productOption(option)
                .productOptionName(option.getOptionName())
                .deliveryFee(option.getProduct().getDeliveryFee())
                .product(option.getProduct())
                .productName(option.getProduct().getName())
                .productOptionPrice(option.getOptionPrice())
                .count(count)
                .build();
    }

    public void ordered(Order order){
        this.order = order;
        order.increaseTotalPrice(calculateOrderItemPrice());
        order.getOrderItems().add(this);
    }

    public void prePayed(){
        this.orderStatus = OrderStatus.PRE_PAYED;
    }

    public void payed(){
        if(this.orderStatus == OrderStatus.PRE_PAYED){
            this.orderStatus = OrderStatus.PAYED;
        }
        throw new BaseException(ErrorCode.PAY_ALREADY_SUCCESS);
    }

    public void rejected(){
        if(this.orderStatus == OrderStatus.PRE_PAYED){
            this.orderStatus = OrderStatus.REJECTED;
        }
        throw new BaseException(ErrorCode.PAY_ALREADY_SUCCESS);
    }

    private int calculateOrderItemPrice(){
        log.info("deliveryFee = {}, productOptionPrice = {}, count = {}", deliveryFee, productOptionPrice, count);
        return (this.deliveryFee + (this.productOptionPrice * this.count));
    }
}
