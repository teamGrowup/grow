package org.boot.growup.source.seller.persist.entity;

import jakarta.persistence.*;
import lombok.*;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO;

import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
@Builder
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

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
    private Integer likeCount; // 좋아요 수

    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubCategory subCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductOption> productOptions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority_status", nullable = false)
    private AuthorityStatus authorityStatus; // 상품의 허가 상태

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    public static Product from(ProductRequestDTO productRequestDto) {
        return Product.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .authorityStatus(AuthorityStatus.PENDING) // 기본 상태를 PENDING으로 설정
                .subCategory(SubCategory.builder().build())
                .averageRating(0.0) // 초기 평균 평점
                .likeCount(0) // 초기 좋아요 수
                .build();
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    /*
        판매자(대표자) 설정
     */
    public void designateSeller(Seller seller) {
        this.seller = seller;
    }

    /*
        허가 상태 변경
     */
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

    /*
        product명 및 상세 설명 수정
     */
    public void updateProductInfo(String name, String description){
        this.name = name;
        this.description = description;
    }
}

