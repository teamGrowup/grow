package org.boot.growup.source.seller.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.source.seller.constant.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String productDescription;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private SubCategory subCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductOption> productOptions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @Enumerated(EnumType.STRING)
    private AuthorityStatus authorityStatus; // 상품의 허가 상태

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();

    public static Product from(ProductRequestDTO productRequestDto) {
        return Product.builder()
                .productName(productRequestDto.getProductName())
                .productDescription(productRequestDto.getProductDescription())
                .authorityStatus(AuthorityStatus.PENDING) // 기본 상태를 PENDING으로 설정
                .build();
    }

    /*
        상품 옵션 추가
     */
    public void addProductOption(ProductOption productOption) {
        productOptions.add(productOption);
        productOption.setProduct(this); // 양방향 연관관계 설정
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

    /*
        상품 옵션 초기화
     */
    public void initProductOptions(List<ProductOption> options) {
        this.productOptions.clear();
        this.productOptions.addAll(options);
    }
}
