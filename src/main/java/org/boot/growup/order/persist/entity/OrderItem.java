package org.boot.growup.order.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.common.constant.Commission;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.constant.OrderStatus;
import org.boot.growup.common.entity.BaseEntity;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.product.persist.entity.Product;
import org.boot.growup.product.persist.entity.ProductOption;
import org.hibernate.envers.AuditOverride;

import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter
@Builder
@Table(name = "order_item")
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class OrderItem extends BaseEntity{
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Column(nullable = true)
    private int netProceed;

    @Column(nullable = true)
    private int commission;

    public static OrderItem of(ProductOption option, Integer count) {
        return OrderItem.builder()
                .productOption(option)
                .productOptionName(option.getOptionName())
                .deliveryFee(option.getProduct().getDeliveryFee())
                .seller(option.getProduct().getSeller())
                .product(option.getProduct())
                .productName(option.getProduct().getName())
                .productOptionPrice(option.getOptionPrice())
                .count(count)
                .seller(option.getProduct().getSeller())
                .build();
    }

    public void ordered(Order order){
        this.order = order;
        order.increaseTotalPrice(calculateOrderItemPrice());
        order.getOrderItems().add(this);
    }

    public void prePaid(){
        this.orderStatus = OrderStatus.PRE_PAID;
    }

    public void payed() {
        if(this.orderStatus != OrderStatus.PRE_PAID){
            throw new BaseException(ErrorCode.PAY_ALREADY_SUCCESS);
        }
        this.commission = Commission.calculateCommission(this.productOptionPrice) * this.count;
        this.netProceed = Commission.calculateNetProceed(this.productOptionPrice) * this.count;
        this.orderStatus = OrderStatus.PAID;
    }

    public void rejected() {
        if(this.orderStatus != OrderStatus.PRE_PAID){
            throw new BaseException(ErrorCode.PAY_ALREADY_SUCCESS);
        }
        this.orderStatus = OrderStatus.REJECTED;
    }

    public void preShipped() {
        // PAID -> PRE-SHIPPED
        if(this.orderStatus == OrderStatus.PAID){
            this.orderStatus = OrderStatus.PRE_SHIPPED;
            return;
        }
        throw new BaseException(ErrorCode.ORDER_ITEM_NOT_PAID_STATUS);
    }

    public void shipped() {
        // PRE_SHIPPED, PAID -> SHIPPED
        if(this.orderStatus == OrderStatus.PAID || this.orderStatus == OrderStatus.PRE_SHIPPED) {
            this.orderStatus = OrderStatus.SHIPPED;
            this.shipped_at = LocalDateTime.now();
            return;
        }
        throw new BaseException(ErrorCode.ORDER_ITEM_NOT_PAID_OR_PRE_SHIPPED_STATUS);
    }

    public void shipment(Delivery delivery) {
        // SHIPPED -> PENDING_SHIPMENT
        if(this.orderStatus == OrderStatus.SHIPPED) {
            this.orderStatus = OrderStatus.PENDING_SHIPMENT;
            this.delivery = delivery;
            return;
        }
        throw new BaseException(ErrorCode.ORDER_ITEM_NOT_SHIPPED_STATUS);
    }

    public void transit() {
        // PENDING_SHIPMENT -> IN_TRANSIT
        if(this.orderStatus == OrderStatus.PENDING_SHIPMENT) {
            this.orderStatus = OrderStatus.IN_TRANSIT;
            return;
        }
        throw new BaseException(ErrorCode.ORDER_ITEM_NOT_SHIPPED_STATUS);
    }

    public void arrived() {
        // IN_TRANSIT -> ARRIVED
        if(this.orderStatus == OrderStatus.IN_TRANSIT) {
            this.orderStatus = OrderStatus.ARRIVED;
            return;
        }
        throw new BaseException(ErrorCode.ORDER_ITEM_NOT_IN_TRANSIT_STATUS);
    }

    private int calculateOrderItemPrice() {
        return (this.deliveryFee + (this.productOptionPrice * this.count));
    }

    public void confirmedPurchase() {
        // ARRIVED -> PURCHASE_CONFIRM
        if(this.orderStatus == OrderStatus.ARRIVED) {
            this.orderStatus = OrderStatus.PURCHASE_CONFIRM;
            this.purchase_confirmed_at = LocalDateTime.now();
            this.seller.increaseNetProceeds(this.netProceed);
            return;
        }
        throw new BaseException(ErrorCode.ORDER_ITEM_NOT_IN_TRANSIT_STATUS);
    }
}
