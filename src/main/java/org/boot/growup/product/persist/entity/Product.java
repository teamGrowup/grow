package org.boot.growup.product.persist.entity;

import jakarta.persistence.*;
import lombok.*;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.common.entity.BaseEntity;
import org.boot.growup.product.dto.request.PostProductRequestDTO;
import org.hibernate.envers.AuditOverride;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 100)
    private String name;

    @Column(name = "product_description", nullable = false, length = 500)
    private String description;

    @Column(name = "average_rating")
    private Double averageRating; // 평균 평점

    @Column(name = "like_count")
    private int likeCount; // 좋아요 수

    @Column(name = "delivery_fee")
    private int deliveryFee; // 배송비

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubCategory subCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductOption> productOptions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority_status", nullable = false)
    private AuthorityStatus authorityStatus; // 상품의 허가 상태

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    public static Product of(PostProductRequestDTO postProductRequestDto, Brand brand, SubCategory subCategory) {
        return Product.builder()
                .name(postProductRequestDto.getName())
                .description(postProductRequestDto.getDescription())
                .deliveryFee(postProductRequestDto.getDeliveryFee())
                .authorityStatus(AuthorityStatus.PENDING) // 기본 상태를 PENDING으로 설정
                .subCategory(subCategory)
                .brand(brand)
                .averageRating(0.0) // 초기 평균 평점
                .likeCount(0) // 초기 좋아요 수
                .build();
    }

    /*
    판매자(대표자) 설정
     */
    public void designateSeller(Seller seller) {
        this.seller = seller;
    }

    public void approve() {
        this.authorityStatus = AuthorityStatus.APPROVED;
    }

    public void deny() {
        this.authorityStatus = AuthorityStatus.DENIED;
    }

    public void pending() {
        this.authorityStatus = AuthorityStatus.PENDING;
    }

    public void initAverageRating() {
        this.averageRating = 0.0; // 초기 평균 평점
    }

    public void initLikeCount() {
        this.likeCount = 0; // 초기 좋아요 수
    }

    public void initDeliveryFee() {
        this.deliveryFee = 0; // 초기 배송비
    }

    // 좋아요 수 증가
    public void likeCountPlus() {
        this.likeCount++;
    }

    // 좋아요 수 감소
    public void likeCountMinus() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    /*
    상품 옵션 초기화
     */
    public void initProductOptions(List<ProductOption> options) {
        this.productOptions.clear();
        if (options != null) {
            this.productOptions.addAll(options);
            for (ProductOption option : options) {
                option.setProduct(this); // 양방향 관계 설정
            }
        }
    }

    public void patchProductInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void patchSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public void patchBrand(Brand brand) {
        this.brand = brand;
    }
}

