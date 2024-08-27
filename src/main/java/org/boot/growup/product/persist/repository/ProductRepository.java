package org.boot.growup.product.persist.repository;


import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.product.persist.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
    Optional<Product> findByIdAndSeller_Id(Long productId, Long sellerId);
    List<Product> findBySeller_Id(Long sellerId); // 판매자 ID로 상품 목록 조회
    List<Product> findByAuthorityStatus(AuthorityStatus authorityStatus, Pageable pageable);
    List<Product> findByBrandId(Long brandId); // 브랜드 ID로 상품 조회
}