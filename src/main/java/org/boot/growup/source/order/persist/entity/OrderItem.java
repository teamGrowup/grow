package org.boot.growup.source.order.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.common.enumerate.OrderStatus;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.ProductOption;

import java.time.LocalDateTime;

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
}
