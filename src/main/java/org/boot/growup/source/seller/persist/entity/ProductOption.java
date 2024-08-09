package org.boot.growup.source.seller.persist.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}