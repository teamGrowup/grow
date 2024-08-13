package org.boot.growup.source.seller.persist.repository;


import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySeller_Id(Long id);
}