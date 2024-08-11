package org.boot.growup.source.seller.persist.repository;


import org.boot.growup.source.seller.persist.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    void deleteProductImageByProduct_Id(Long id);
}
