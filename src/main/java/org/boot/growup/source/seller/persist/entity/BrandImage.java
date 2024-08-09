package org.boot.growup.source.seller.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "brand_image")
@NoArgsConstructor
@AllArgsConstructor
public class BrandImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_image_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String originalImageName;

    @Column(nullable = false)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    public void designateBrand(Brand brand) {
        this.brand = brand;
    }
}
