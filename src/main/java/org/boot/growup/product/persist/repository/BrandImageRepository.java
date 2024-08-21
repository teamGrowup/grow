package org.boot.growup.product.persist.repository;

import org.boot.growup.product.persist.entity.BrandImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandImageRepository extends JpaRepository<BrandImage, Long> {
    List<BrandImage> findBrandImageByBrand_Id(Long id);
    void deleteBrandImageByBrand_Id(Long id);
}
