package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.source.seller.persist.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByName(String name);
    Optional<Brand> findBySeller_Id(Long id);
}
