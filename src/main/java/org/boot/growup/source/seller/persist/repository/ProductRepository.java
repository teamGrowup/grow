package org.boot.growup.source.seller.persist.repository;


import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.persist.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySeller_Id(Long id);
    Optional<Product> findById(Long id);
    Optional<Product> findByIdAndSeller_Id(Long productId, Long sellerId);
    List<Product> findByAuthorityStatus(AuthorityStatus authorityStatus, Pageable pageable);
}