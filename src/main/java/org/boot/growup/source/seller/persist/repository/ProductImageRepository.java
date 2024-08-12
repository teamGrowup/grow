package org.boot.growup.source.seller.persist.repository;


import org.boot.growup.source.seller.persist.entity.BrandImage;
import org.boot.growup.source.seller.persist.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage>  findProductImageByProduct_Id(Long id);
    void deleteProductImageByProduct_Id(Long id);
}
