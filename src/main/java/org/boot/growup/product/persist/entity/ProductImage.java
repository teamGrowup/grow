package org.boot.growup.product.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.boot.growup.common.constant.Section;
import org.boot.growup.common.entity.BaseEntity;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_image")
@AuditOverride(forClass = BaseEntity.class)
public class ProductImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id", nullable = false)
    private Long id; // 이미지 ID

    @Column(name = "original_image_name", nullable = false)
    private String originalImageName; // 원본 이미지 이름

    @Column(name = "image_path", nullable = false)
    private String path; // 이미지 경로

    @Enumerated(EnumType.STRING)
    @Column(name = "section", nullable = false)
    private Section section; // Section enum 사용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // 해당 상품과의 관계

    // 상품을 지정하는 메서드
    public void designateProduct(Product product) {
        this.product = product;
    }
}
